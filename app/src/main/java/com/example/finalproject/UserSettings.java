package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettings extends AppCompatActivity {

    Button btnOrange,btnYellow,btnGreen,btnBlue,btnPink,btnPurple;
    CircleImageView userImg;
    ImageButton btnAddPic;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int appColor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**---Reading--- from Shared Preference*/
       // app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // appColor = app_preferences.getInt("color", -9391916);

        setContentView(R.layout.user_settings);


        /**finding the user profile picture*/
        userImg = findViewById(R.id.userImage);
     //   userImg.setBorderColor(appColor);

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
