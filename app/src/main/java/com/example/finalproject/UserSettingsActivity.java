package com.example.finalproject;

import static com.yalantis.ucrop.util.FileUtils.getPath;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.databinding.ActivityUserSettingsBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends DrawerBaseActivity {

    private static final int PERMISSION_CODE = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    int SELECT_PICTURE = 200;
    ActivityUserSettingsBinding activityUserSettingsBinding;

    Dialog dialog;
    Button btnOrange,btnYellow,btnGreen,btnBlue,btnPink,btnPurple;
    CircleImageView userImg;
    ImageButton btnAddPic , btnDelete , btnEditName;
    TextView btnCancel, btnSave;

    EditText editUser;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    int appColor;

    CircleImageView fromGallery , fromCamera;

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
        userImg = findViewById(R.id.user_image_small);
        userImg.setBorderColor(appColor);

        /**finding 'btnAddPic'*/
        btnAddPic = findViewById(R.id.btnAddPic);

        /**finding 'btnEditName'*/
        btnEditName = findViewById(R.id.btnEditName);

        /**finding the color buttons*/
        btnOrange = findViewById(R.id.btnOrange);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);
        btnPink = findViewById(R.id.btnPink);
        btnPurple = findViewById(R.id.btnPurple);

        /**Add picture button listener*/
        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPic();
            }

            private void showDialogPic() {
                dialog = new Dialog(UserSettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_add_img_layout);

                /**DELETE button*/
                btnDelete = dialog.findViewById(R.id.deleteBtn);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       userImg.setImageResource(R.drawable.android_avatar);
                        dialog.dismiss();
                    }
                });

                /**PIC FROM GALLERY button*/
                fromGallery = dialog.findViewById(R.id.galleryBtn);
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

                /**PIC FROM CAMERA button*/
                fromCamera = dialog.findViewById(R.id.cameraBtn);
                fromCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                            if(checkSelfPermission(Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_DENIED){
                                //permission isn't granted, request it
                                String[] permissions = {Manifest.permission.CAMERA};
                                requestPermissions(permissions,PERMISSION_CODE);
                            }else {
                                //permission allready granted
                                pickImageFromCamera();
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
        /**END of add picture button listener*/

        /**edit user name*/
        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEditName();
            }

            private void showDialogEditName() {
                dialog = new Dialog(UserSettingsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.bottom_edit_user_name_layout);

                //
                /**CANCEL button*/
                btnCancel= dialog.findViewById(R.id.cancelBtn);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserSettingsActivity.this.getWindow().
                                setSoftInputMode(WindowManager.
                                        LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.dismiss();

                    }
                });

                /**SAVE button*/
                btnSave= dialog.findViewById(R.id.saveBtn);
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

//                /**showing the keyboard*/
                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

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
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void pickImageFromCamera() {
        Intent i = new Intent();
       i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(Intent.createChooser(i, "take Picture"), REQUEST_IMAGE_CAPTURE);


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
            case REQUEST_IMAGE_CAPTURE : {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromCamera();
                }
            }

        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String picturePath = getPath( UserSettingsActivity.this, selectedImageUri );
                if (null != selectedImageUri) {
                    userImg.setImageURI(Uri.parse(picturePath));
                }
            }
            if (requestCode == 100) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                if (null != bitmap) {
                    userImg.setImageBitmap(bitmap);
                }
            }
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
