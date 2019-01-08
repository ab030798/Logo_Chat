package com.example.hp.logo_chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference muserdatabase;
    private  FirebaseRecyclerOptions<Users> options;
    private  FirebaseRecyclerAdapter<Users,UserViewHolder> adapter;
    TextView textView;
    TextView textView1;
    LinearLayoutManager layoutManager;
    LayoutInflater inflater;
    View view;
    Query personsQuery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar)findViewById(R.id.user_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        muserdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        personsQuery = muserdatabase.orderByKey();

        mUserList  = (RecyclerView) findViewById(R.id.main_recycle);

         layoutManager= new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(layoutManager);

    }



    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(personsQuery,Users.class).build();
        adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {
             holder.setName(model.getName());
             holder.setStatus(model.getStatus());
             holder.setUserImage(model.getThumb_image(),getApplicationContext());
             final String user_id = getRef(position).getKey();

             holder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent profileIntent = new Intent(UsersActivity.this,ProfileActivity.class);
                     profileIntent.putExtra("user_id",user_id);
                     startActivity(profileIntent);
                 }
             });

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               inflater = LayoutInflater.from(parent.getContext());
                  view =      inflater.inflate(R.layout.users_single_layout,parent,false);
                return new UserViewHolder(view);
            }
        };

mUserList.setAdapter(adapter);
adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public  class UserViewHolder extends RecyclerView.ViewHolder{
      View mview;

        public UserViewHolder(View itemView) {
            super(itemView);

            mview = itemView;
        }

public void setName(String name){
            textView = mview.findViewById(R.id.use_single_textview1);
            textView.setText(name);
}
public void setStatus (String status){
       textView1 = mview.findViewById(R.id.user_single_textview2);
       textView1.setText(status);
}

public void setUserImage(String thumb_image, Context context){
    CircleImageView circleImageView = mview .findViewById(R.id.user_single_image);
    Picasso.get().load(thumb_image).placeholder(R.drawable.download).into(circleImageView);
}
        }
    }

