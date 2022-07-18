package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FrameLayout container;
    ImageButton homeBtn;
    CircleImageView userImg;
    SharedPreferences app_preferences;
    int appColor;

    @Override
    public void setContentView(View view) {
        drawerLayout= (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer_base,null);
        container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);


        /**HOME button*/
        homeBtn = drawerLayout.findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DrawerBaseActivity.this, MainActivity.class);
                DrawerBaseActivity.this.startActivity(myIntent);
            }
       });

        //making the navigation bar clickable
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);

        app_preferences = this.getSharedPreferences("UserSettingsActivity", Context.MODE_PRIVATE);
        appColor = app_preferences.getInt("color", 0);

        /**finding the user profile picture*/
//        userImg= drawerLayout.findViewById(R.id.user_image);
//        userImg.setBorderColor(appColor);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_profile:
                startActivity(new Intent(this, UserSettingsActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.nav_log_out:
                signOut();
                Intent userLogin = new Intent(getApplicationContext(),userLoginActivity.class);
                startActivity(userLogin);
                break;
            case R.id.nav_friends:
                Intent sharingActivity = new Intent(getApplicationContext(),SharingListActivity.class);
                startActivity(sharingActivity);
                break;


        }
        return false;
    }


//    @Override
    public void onBackPressed() {
        //if the drawer is open
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(titleString);
        }
    }
    private void signOut(){
        Log.d("TAG", "signOut: signing out");
       FirebaseAuth.getInstance().signOut();
    }

}
