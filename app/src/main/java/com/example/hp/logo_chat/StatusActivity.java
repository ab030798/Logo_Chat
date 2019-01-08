package com.example.hp.logo_chat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputLayout mstatus;
    private Button mchange;
    private DatabaseReference mstatusdatabase;
    private FirebaseUser mcuurentuser;
    private ProgressDialog mprogressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
         mstatus = findViewById(R.id.status_input);
         mchange = findViewById(R.id.status_button);
         mcuurentuser = FirebaseAuth.getInstance().getCurrentUser();
         String uid = mcuurentuser.getUid();
         mstatusdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


        toolbar = findViewById(R.id.status_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String status_value = getIntent().getStringExtra("status_value");
        mstatus.getEditText().setText(status_value);


        mchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mprogressDialog= new ProgressDialog(StatusActivity.this);

                mprogressDialog.setTitle("Saving Changes");
                mprogressDialog.setMessage("Pleasw Wait");
                mprogressDialog.setCanceledOnTouchOutside(false);

                mprogressDialog.show();


                String status = mstatus.getEditText().getText().toString();
                mstatusdatabase.child("status").setValue(status).addOnCompleteListener(StatusActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mprogressDialog.dismiss();

                        }else {
                            Toast.makeText(getApplicationContext(),"some error in saving changes",Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });


    }
}
