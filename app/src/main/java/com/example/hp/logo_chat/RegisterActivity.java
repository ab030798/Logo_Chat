package com.example.hp.logo_chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText e1, e2, e3;
    private Button b;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);

        toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editText2);
        e3 = findViewById(R.id.editText3);
        b = findViewById(R.id.button3);
        mAuth = FirebaseAuth.getInstance();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = e1.getText().toString();
                String email = e2.getText().toString();
                String password = e3.getText().toString();
                if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    progressDialog.setTitle("Registring User");
                    progressDialog.setMessage("Pleasw Wait ");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register_user(name,email,password);
                }


            }



        });


    }

    private void register_user(final String name, String email, String password) {
        // register_user(name,email,password);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser crrent_user =  FirebaseAuth.getInstance().getCurrentUser();
                    String uid = crrent_user.getUid();
                    database = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                  //  String device_token = FirebaseInstanceId.getInstance().getToken();
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name",name);
                    userMap.put("status","hi  i am there using logo chat");
                    userMap.put("image","default");
                    userMap.put("thumb_image","default");
                //    userMap.put("device_token", device_token);
                    database.setValue(userMap).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<Void>() {
                        @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Intent i= new Intent(RegisterActivity.this,MystartActivity.class);
                               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              startActivity(i);
                               finish();
                               }

                       }
                  });


                    //   Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.hide();
                    Toast.makeText(RegisterActivity.this, "Can not Sign in Please Check the Form ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}