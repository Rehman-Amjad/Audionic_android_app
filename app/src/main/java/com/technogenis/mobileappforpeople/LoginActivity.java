package com.technogenis.mobileappforpeople;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText ed_email,ed_password;
    TextView register_text;
    Button btn_login;
    String email,password;
    FirebaseAuth mAuth;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth =  FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        initViews();

        btn_login.setOnClickListener(v -> {
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            if (isValid(email,password)){
                signInWithEmailPasswords(email,password);
            }
        });

        register_text.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        });
    }

    private void signInWithEmailPasswords(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }else {
                            Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private boolean isValid(String email, String password)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            ed_email.setError("invalid Email");
            ed_email.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            ed_password.setError("invalid password");
            ed_password.requestFocus();
            return false;
        }

        return true;
    }

    private void initViews()
    {
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        register_text = findViewById(R.id.register_text);
    }
}