package com.example.hp.logo_chat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
  private Toolbar toolbar;
  private ViewPager viewPager;
  private  SectionPageAdapter msectionadapter;
  private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
      toolbar = (Toolbar)findViewById(R.id.main1);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LOGO CHAT");
        viewPager=findViewById(R.id.main_tabpager);
        msectionadapter = new SectionPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(msectionadapter);
        tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
           sendtostart();
        }

    }

    private void sendtostart() {
        Intent intent=new Intent(MainActivity.this,MystartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu ,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId()==R.id.main_logout)
         {
             FirebaseAuth.getInstance().signOut();
             sendtostart();
         }


          if (item.getItemId()==R.id.main_setting)
          {
              Intent intent = new Intent(MainActivity.this,SettingActivity.class);
              startActivity(intent);
          }

          if(item.getItemId()==R.id.main_user){
              Intent intent = new Intent(MainActivity.this,UsersActivity.class);
              startActivity(intent);

          }

         return true;
    }
}
