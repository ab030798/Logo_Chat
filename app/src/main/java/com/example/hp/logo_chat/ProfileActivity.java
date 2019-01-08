package com.example.hp.logo_chat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private TextView e1;
    private  TextView e2;
    private  TextView e3;
    private ImageView profileimage;
    private Button bt;
    private DatabaseReference muserdatabase;
    private DatabaseReference mfriendsdtabase;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mprogrsdialouge;
    private String mCurrent_state;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
       final String user_id = getIntent().getStringExtra("user_id");
       muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
       mfriendsdtabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
       mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
       e1 =(TextView)findViewById(R.id.profiletextview1);
       e2 = (TextView) findViewById(R.id.profiletextview2);
       e3 = (TextView) findViewById(R.id.profiletextView3);
       profileimage = (ImageView)findViewById(R.id.profileimage1);
       bt = (Button)findViewById(R.id.profilebutton1);
       mCurrent_state = "not_friends";
       mprogrsdialouge = new ProgressDialog(this);
       mprogrsdialouge.setTitle("Loading Data");
       mprogrsdialouge.setMessage("please Wait");
       mprogrsdialouge.setCanceledOnTouchOutside(false);
       mprogrsdialouge.show();
       muserdatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String displayname = dataSnapshot.child("name").getValue().toString();
               String status = dataSnapshot.child("status").getValue().toString();
               String image = dataSnapshot.child("image").getValue().toString();
               e1.setText(displayname);
               e2.setText(status);
               //Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.download).into(profileimage);
               Picasso.get().load(image).into(profileimage);
               mfriendsdtabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       if (dataSnapshot.hasChild(user_id)){


                           String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                           if (req_type.equals("received")){


                               mCurrent_state="req_received";
                               bt.setText("Accept friend request");


                           }
                           else if (req_type.equals("sent")){
                               mCurrent_state="req_sent";
                               bt.setText("Cancel Freind Request");

                           }

                       }

                       mprogrsdialouge.dismiss();


                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });








           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       bt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               bt.setEnabled(false);
               if (mCurrent_state.equals("not_friends")){
                   mfriendsdtabase.child(mCurrentUser.getUid()).child(user_id).child("request_type").setValue("sent")
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               mfriendsdtabase.child(user_id).child(mCurrentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void aVoid) {
                                       bt.setEnabled(true);
                                       mCurrent_state="req_sent";
                                       bt.setText("cancel friend request");
                                    //   Toast.makeText(ProfileActivity.this,"send succesfully",Toast.LENGTH_LONG).show();

                                   }
                               });

                           }
                           else {
                               Toast.makeText(ProfileActivity.this,"fialed friend request",Toast.LENGTH_LONG).show();
                           }

                       }
                   });

               }
               if(mCurrent_state.equals("req_sent")){
                   mfriendsdtabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           mfriendsdtabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   bt.setEnabled(true);
                                   mCurrent_state="not_friends";
                                   bt.setText("send friend request");

                               }
                           });



                       }
                   });
               }

           }
       });

    }
}
