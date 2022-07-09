package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.example.finalproject.databinding.ActivityUserSettingsBinding;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends DrawerBaseActivity {

   ActivityUserSettingsBinding activityUserSettingsBinding;

    Button btnOrange,btnYellow,btnGreen,btnBlue,btnPink,btnPurple;
    CircleImageView userImg;
    ImageButton btnAddPic;


    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    int appColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserSettingsBinding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(activityUserSettingsBinding.getRoot());
        allocateActivityTitle("");

        /**---Reading--- from Shared Preference*/
        app_preferences = this.getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        appColor = app_preferences.getInt("color", 0);

        /**finding the user profile picture*/
        userImg = findViewById(R.id.user_image);
        userImg.setBorderColor(appColor);

        /**btnAddPic*/
        btnAddPic = findViewById(R.id.btnAddPic);

        /**finding the color buttons*/
        btnOrange = findViewById(R.id.btnOrange);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnPink = findViewById(R.id.btnPink);
        btnPurple = findViewById(R.id.btnPurple);

        /**Add picture button*/
        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor( appColor = getColor(R.color.t_orange));
                saveData();
            }
        });

        /**orange button*/
        btnOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(appColor = getColor(R.color.t_orange));
                saveData();
            }
        });

        /**yellow button*/
        btnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(appColor = getColor(R.color.t_yellow));
                saveData();
            }
        });

        /**green button*/
        btnGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(appColor = getColor(R.color.t_green));
                saveData();
            }
        });

        /**blue button*/
        btnBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(appColor = getColor(R.color.t_blue));
                saveData();
            }
        });

        /**pink button*/
        btnPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(getColor(appColor = R.color.t_pink));
                saveData();
            }
        });

        /**purple button*/
        btnPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImg.setBorderColor(appColor = getColor(R.color.t_purple));
                saveData();
            }
        });
    }


    public void saveData() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("color",appColor);
        editor.apply();
    }
}
