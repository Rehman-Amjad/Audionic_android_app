package com.technogenis.mobileappforpeople;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class SettingActivity extends AppCompatActivity {

    ImageView back_image;
    Button btn_save,btn_saveMessage;
    EditText ed_number,ed_message;
    String number,message;
    CardView logout_card;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();

        back_image = findViewById(R.id.back_image);
        btn_save = findViewById(R.id.btn_save);
        ed_number = findViewById(R.id.ed_number);
        logout_card = findViewById(R.id.logout_card);
        ed_message = findViewById(R.id.ed_message);
        btn_saveMessage = findViewById(R.id.btn_saveMessage);


        SharedPreferences pref = getSharedPreferences("NM",MODE_PRIVATE);
        if (pref!=null)
        {
            ed_number.setText(pref.getString("number",""));
            ed_message.setText(pref.getString("message",""));
        }

        logout_card.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(SettingActivity.this,LoginActivity.class));
            finish();
        });

        back_image.setOnClickListener(v -> {
           startActivity(new Intent(SettingActivity.this,MainActivity.class));
           finish();
        });

        btn_save.setOnClickListener(v -> {
            number = ed_number.getText().toString().trim();
           if (number.isEmpty() || number.length() < 13 )
           {
               Toast.makeText(this, "Enter valid Number", Toast.LENGTH_SHORT).show();
           }else{
               SharedPreferences preferences = getSharedPreferences("NM",MODE_PRIVATE);
               SharedPreferences.Editor editor = preferences.edit();
               editor.putString("number",number);
               editor.apply();
               Toast.makeText(this, "number saved", Toast.LENGTH_SHORT).show();
           }

        });

        btn_saveMessage.setOnClickListener(v -> {
            message = ed_message.getText().toString().trim();
            if (message.isEmpty())
            {
                Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show();
            }else{
                SharedPreferences preferences = getSharedPreferences("NM",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("message",message);
                editor.apply();
                Toast.makeText(this, "message saved", Toast.LENGTH_SHORT).show();
            }

        });
    }
}