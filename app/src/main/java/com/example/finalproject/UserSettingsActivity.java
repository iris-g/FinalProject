package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.finalproject.databinding.ActivityUserSettingsBinding;
import com.github.drjacky.imagepicker.ImagePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends DrawerBaseActivity {

    private static final int PERMISSION_CODE = 1001;
    private static final int IMAGE_PICK_CODE = 1000;
    ActivityUserSettingsBinding activityUserSettingsBinding;

    Dialog dialog;
    Button btnOrange,btnYellow,btnGreen,btnBlue,btnPink,btnPurple;
    CircleImageView userImg;
    ImageButton btnAddPic;
    Uri imageUri;

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
        app_preferences = this.getSharedPreferences("UserSettingsActivity", Context.MODE_PRIVATE);
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
                showDialog();


            }

            private void showDialog() {
                dialog = new Dialog(UserSettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_add_img_layout);

                CircleImageView fromGallery = dialog.findViewById(R.id.galleryBtn);
                fromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                                //permission isn't granted, request it
                                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions,PERMISSION_CODE);
                            }else {
                                //permission allready granted
                                pickImageFromGallery();
                            }
                        }
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);



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

    private void pickImageFromGallery() {
        //intent to pick picture
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            //SET IMAGE TO IMAGE VIEW
            imageUri= data.getData();
            //userImg.setImageURI(imageUri);
           // super.onActivityResult(requestCode, resultCode, data);
            userImg.setBorderColor(getColor(R.color.t_pink));
            dialog.dismiss();

        }
    }

    public void saveData() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("color",appColor);
        editor.apply();
    }
}
