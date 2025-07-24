package com.technogenis.mobileappforpeople;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CardView phone_card,whatsapp_card,sms_card,lightOn_card,lightOff_card,fanOn_card,fanOff_card,bellOn_card
                ,bellOff_card,switchOn_card,switchOff_card,setting_card;
    String value;
    TextToSpeech speech;
    SeekBar seekPitch,seekSpeed;
    String spokenText;
    String number,message,feature;
    TextView text;
    ToggleButton toggle;
    boolean isCall = false,isVoiceFeatureActivated = true;
    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone_card = findViewById(R.id.phone_card);
        whatsapp_card = findViewById(R.id.whatsapp_card);
        sms_card = findViewById(R.id.sms_card);
        lightOn_card = findViewById(R.id.lightOn_card);
        lightOff_card = findViewById(R.id.lightOff_card);
        fanOn_card = findViewById(R.id.fanOn_card);
        fanOff_card = findViewById(R.id.fanOff_card);
        bellOn_card = findViewById(R.id.bellOn_card);
        bellOff_card = findViewById(R.id.bellOff_card);
        switchOn_card = findViewById(R.id.switchOn_card);
        switchOff_card = findViewById(R.id.switchOff_card);
        setting_card = findViewById(R.id.setting_card);
        text = findViewById(R.id.text);
        toggle = findViewById(R.id.toggle);

        seekPitch=findViewById(R.id.seekPitch);
        seekSpeed=findViewById(R.id.seekSpeed);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS

            }, 100);
        }

        textSpeech();
        displaySpeechRecognizer();


        SharedPreferences preferences = getSharedPreferences("NM",MODE_PRIVATE);
        number = preferences.getString("number","");
        message = preferences.getString("message","");

        feature = preferences.getString("feature","");
        if (feature!=null)
        {
            if (feature.equals("true"))
            {
                isVoiceFeatureActivated = true;
                toggle.setActivated(true);
            }else{
                isVoiceFeatureActivated = false;
                toggle.setActivated(false);
            }
        }


        setting_card.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
        });

        phone_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "calling";
                startVoiceCommand(command);
                callToPhone(number);
            }

        });

        whatsapp_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "whasapp is on";
                startVoiceCommand(command);
                whatsapp(number);
            }
        });

        sms_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "Message Sending";
                startVoiceCommand(command);
                sendSMS();
            }
        });

        lightOn_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "Light is on";
                startVoiceCommand(command);
               lightSend("1");
            }
        });

        lightOff_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "light is off";
                startVoiceCommand(command);
                lightSend("0");
            }
        });

        fanOn_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "fan is on";
                startVoiceCommand(command);
                fanValue("1");
            }
        });

        fanOff_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "fan is off";
                startVoiceCommand(command);
                fanValue("0");
            }
        });

        bellOn_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "bell is on";
                startVoiceCommand(command);
                bellValue("1","bell is on");
            }
        });
        bellOff_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "bell is off";
                startVoiceCommand(command);
                bellValue("0","bell is on");
            }
        });

        switchOn_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "Switch is on";
                startVoiceCommand(command);
                switchValue("1","bell is on");
            }
        });

        switchOff_card.setOnClickListener(v -> {
            if (isVoiceFeatureActivated)
            {
                displaySpeechRecognizer();
            }else{
                String command = "Switch is off";
                startVoiceCommand(command);
                switchValue("0","bell is on");
            }
        });

        toggle.setActivated(true);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("NM",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked)
                {
                    editor.putString("feature","true");
                    editor.apply();
                    isVoiceFeatureActivated = true;
                    String command = "Voice Feature is on";
                    startVoiceCommand(command);
                }else{
                    editor.putString("feature","false");
                    editor.apply();
                    isVoiceFeatureActivated = false;
                    String command = "Voice Feature is off";
                    startVoiceCommand(command);
                }
            }
        });
    }

    private void lightSend(String value)
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commands");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("LightInfo").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void smsSend() {
    }

    private void whatsappCall()
    {

    }

    private void phoneCall()
    {

        displaySpeechRecognizer();
    }

    private void textSpeech()
    {
        speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS)
                {
                    int result = speech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(MainActivity.this, "Language not found", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK)
        {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            spokenText = results.get(0);
            // Do something with spokenText.


            if (spokenText.equals("light on"))
            {
                String command = "light is on";
                startVoiceCommand(command);
                value = "1";
                lightSend(value);
            }

            if (spokenText.equals("light off"))
            {
                String command = "light is off";
                startVoiceCommand(command);
                value = "0";
                lightSend(value);
            }

            if(spokenText.equals("whatsApp") || spokenText.equals("WhatsApp"))
            {

                whatsapp(number);
            }


            if (spokenText.equals("fan on") || spokenText.equals("Fan on"))
            {
                String command = "fan is on";
                startVoiceCommand(command);
                value = "1";
                fanValue(value);
            }

            if (spokenText.equals("fan off") || spokenText.equals("fan of") || spokenText.equals("Fan off") || spokenText.equals("Fan of"))
            {
                String command = "fan is off";
                startVoiceCommand(command);
                value = "0";
                fanValue(value);
            }

            if (spokenText.equals("bell on") || spokenText.equals("Bell on"))
            {
                String command = "bell on";
//                startVoiceCommand(command);
                value = "1";
                bellValue(value,command);
            }

            if (spokenText.equals("bell off") || spokenText.equals("bell of") || spokenText.equals("Bell off") || spokenText.equals("Bell of"))
            {
                String command = "bell off";
                startVoiceCommand(command);
                value = "0";
                bellValue(value,command);
            }

            if (spokenText.equals("switch on") || spokenText.equals("Switch on"))
            {
                String command = "switch on";
                startVoiceCommand(command);
                value = "1";
                switchValue(value,command);
            }

            if (spokenText.equals("switch off") || spokenText.equals("switch of") || spokenText.equals("Switch off") || spokenText.equals("Switch of"))
            {
                String command = "switch off";
                startVoiceCommand(command);
                value = "0";
                switchValue(value,command);
            }

            if (spokenText.equals("SMS") || spokenText.equals("sms") ||
                    spokenText.equals("send message"))
            {
                if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                {
                    sendSMS();
                }else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                }

            }

            if (spokenText.equals("phone call") || spokenText.equals("phonecall"))
            {
                if (number!=null)
                {
                    callToPhone(number);
                }else{
                    startVoiceCommand("phone number not found");
                }

            }
        }


    }

    private void switchValue(String value,String commad)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commands");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("SwitchInfo").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful())
                   {
//                       startVoiceCommand(commad);
                   }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startVoiceCommand("something went wrong");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void bellValue(String value,String command)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commands");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("BellInfo").setValue(value)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
//                                    startVoiceCommand(command);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startVoiceCommand("something went wrong");
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//        if(requestCode==100 && resultCode==RESULT_OK && data!=null) {
//            imageuri = data.getData();
//            imgBitmap = (Bitmap) data.getExtras().get("data");
//            image.setImageBitmap(imgBitmap);
//
//        }

    private void whatsapp(String phone)
    {
        try {
            String text = message;// Replace with your message.
//           String toNumber = "xxxxxxxxxx"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fanValue(String value)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Commands");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reference.child("FanInfo").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void callToPhone(String phoneNumber)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ phoneNumber));//change the number
        startActivity(callIntent);
    }

    private void displaySpeechRecognizer()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void startVoiceCommand(String command)
    {
        float pitch = (float) seekPitch.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        float speed = (float) seekSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        speech.setPitch(pitch);
        speech.setSpeechRate(speed);
        speech.speak(command,TextToSpeech.QUEUE_FLUSH,null);
    }

    public void sendSMS()
    {
        String SMSMessage = message;

        try { SmsManager smgr=SmsManager.getDefault();
            smgr.sendTextMessage(number,null,SMSMessage,null,null);
            Toast.makeText(MainActivity.this, " SMS send Successfully", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "SMS Failed to send", Toast.LENGTH_SHORT).show();
        }
    }
}