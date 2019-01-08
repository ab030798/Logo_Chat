package com.example.hp.logo_chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MystartActivity extends AppCompatActivity {
    Button b ,b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystart);
        b=findViewById(R.id.button2);
        b1 = findViewById(R.id.button5);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MystartActivity.this,RegisterActivity.class);
                startActivity(i);

            }
        });

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MystartActivity.this,LoginActivity.class);
               startActivity(intent);
           }
       });
    }
}
