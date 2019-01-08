package com.example.hp.logo_chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingActivity extends AppCompatActivity {
  private DatabaseReference databaseReference;
  private FirebaseUser firebaseUser;
  private CircleImageView circleImageView;
  private TextView mName;
  private TextView mStatus;

  private Button na;
  private  Button st;
  private static final int GALLERY_PICK=1;

    private StorageReference mStorageRef;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
       circleImageView = findViewById(R.id.main_circleimageview);
        mName = findViewById(R.id.setting_textView1);
        mStatus = findViewById(R.id.setting_textView2);
        na = findViewById(R.id.setting_button1);
        st = findViewById(R.id.setting_button2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
//       st.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {
          //      Intent intent = new Intent(SettingActivity.this,StatusActivity.class);
            //    startActivity(intent);
          //  }
       // });




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_Uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_Uid);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               //  Toast.makeText(SettingActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();
             String kname = dataSnapshot.child("name").getValue().toString();
               String mimage = dataSnapshot.child("image").getValue().toString();
               String kstatus = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
               mName.setText(kname);
               mStatus.setText(kstatus);
               if (mimage.equals("default")) {
                  // Picasso.get().load(mimage).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.download).into(circleImageView);

                   Picasso.get().load(mimage).resize(100,100).centerCrop().into(circleImageView);
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     st.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String status_value = mStatus.getText().toString();
             Intent i = new Intent(SettingActivity.this,StatusActivity.class);
             i.putExtra("status_value",status_value);
             startActivity(i);
             finish();

         }
     });
     na.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view){
             Intent gallaryIntent = new Intent();
             gallaryIntent.setType("Images/*");
             gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(Intent.createChooser(gallaryIntent,"SELECT IMAGE"),GALLERY_PICK);
          /*   CropImage.activity()
                     .setGuidelines(CropImageView.Guidelines.ON)
                     .start(SettingActivity.this);*/

         }
     });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(500,500)
                    .start(this);

            //  Toast.makeText(SettingActivity.this,imageUri,Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog = new ProgressDialog(SettingActivity.this);
                mProgressDialog.setTitle("Uploading Image");
                mProgressDialog.setMessage("please Wait");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                Uri resultUri = result.getUri();
                File thumb_filepath = new File(resultUri.getPath());

                String cuurent_user_id = firebaseUser.getUid();
                Bitmap thumb_bitmap = null;
try {


    thumb_bitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(75).compressToBitmap(thumb_filepath);
} catch (IOException e){
    e.printStackTrace();
}
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();



                StorageReference filepath = mStorageRef.child("Profile_Images").child( cuurent_user_id +".jpg");
                final StorageReference thumbfilepath = mStorageRef.child("profile_Image").child("thumbs").child(cuurent_user_id + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(SettingActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){
                          //  Toast.makeText(SettingActivity.this,"working",Toast.LENGTH_LONG).show();
                       //     String download_Url = task.getDownloadUrl().toString();
                   //  String download_Url  = task.getDownloadUrl().toString();
                            final String downloadUri = task.getResult().toString();
                            UploadTask uploadTask = thumbfilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String thumb_url = thumb_task.getResult().toString();

                                    if (thumb_task.isSuccessful()){
                                        Map Update_hashmap = new HashMap();
                                        Update_hashmap.put("image",downloadUri);
                                        Update_hashmap.put("thumb_image",thumb_url);
                                        databaseReference.updateChildren(Update_hashmap).addOnCompleteListener(SettingActivity.this,new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(SettingActivity.this,"sucessfullu uploading",Toast.LENGTH_LONG).show();


                                                }



                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(SettingActivity.this,"error in uploading thumbnil",Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else{

                            Toast.makeText(SettingActivity.this,"error in uploading",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10 );
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
