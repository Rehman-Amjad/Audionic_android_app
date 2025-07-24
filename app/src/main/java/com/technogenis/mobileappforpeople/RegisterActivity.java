package com.technogenis.mobileappforpeople;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ImageView back_image;
    TextView back_text;
    EditText ed_fullName,ed_email,ed_password,ed_con_password;
    Button btn_register;
    String fullName,email,password,conPassword;
    LoadingBar loadingBar = new LoadingBar(this);
    ErrorTost errorTost = new ErrorTost(this);

    String userUID;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        initViews();

        back_image.setOnClickListener(v -> onBackPressed());
        back_text.setText("Register Now");

        btn_register.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please wait");
            fullName = ed_fullName.getText().toString().trim();
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            conPassword = ed_con_password.getText().toString().trim();

            if (isValid(fullName,password)){

                if (password.equals(conPassword)) {
                    createAccountWithEmailPassword(email,password);
                }else{
                    loadingBar.HideDialog();
                    errorTost.showErrorMessage("password doest not match");
                }

            }
        });


    }

    private void createAccountWithEmailPassword(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            savedDataToFirebase();
                        } else {
                            // If sign in fails, display a message to the user.
                            loadingBar.HideDialog();
                            errorTost.showErrorMessage("Something went wrong!");

                        }
                    }
                });
    }

    private void savedDataToFirebase()
    {
        mUser = mAuth.getCurrentUser();
        assert mUser != null;
        userUID = mUser.getUid();

        Map<String,Object> map = new HashMap<>();
        map.put("fullName",fullName);
        map.put("email",email);
        map.put("password",password);
        map.put("userUID",userUID);
        map.put("accountStatus","active");


        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userUID)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loadingBar.HideDialog();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                        errorTost.showErrorMessage("Something went wrong");
                    }
                });
    }

    private boolean isValid(String fullName,String password)
    {
        if (fullName.isEmpty()){
            ed_fullName.setError("Field Required");
            ed_fullName.requestFocus();
            loadingBar.HideDialog();
            return false;
        }

        if (password.isEmpty() || password.length() < 8){
            ed_password.setError("Field Required");
            ed_password.requestFocus();
            loadingBar.HideDialog();
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            ed_email.setError("invalid Email");
            ed_email.requestFocus();
            loadingBar.HideDialog();
            return false;
        }


        return  true;
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        back_text = findViewById(R.id.back_text);
        ed_fullName = findViewById(R.id.ed_fullName);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_con_password = findViewById(R.id.ed_con_password);
        btn_register = findViewById(R.id.btn_register);
    }
}