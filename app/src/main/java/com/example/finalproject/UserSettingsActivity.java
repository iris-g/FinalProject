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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.databinding.ActivityUserSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends DrawerBaseActivity {

    private static final int PERMISSION_CODE = 1001;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    int SELECT_PICTURE = 200;

    /**in order to bind the drawer(side menu)*/
    ActivityUserSettingsBinding activityUserSettingsBinding;

    Dialog dialog;
    Button btnOrange,btnYellow,btnGreen,btnBlue,btnPink,btnPurple;
    CircleImageView userImg;
    ImageButton btnAddPic , btnDelete , btnEditName;
    TextView btnCancel, btnSave;
    TextView userName;
    EditText editUser;
    FirebaseAuth auth;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;

    int appColor;

    CircleImageView fromGallery , fromCamera;

    /**firebase*/
    FirebaseUser fUser ;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserSettingsBinding = ActivityUserSettingsBinding.inflate(getLayoutInflater());
        setContentView(activityUserSettingsBinding.getRoot());
        allocateActivityTitle("");

        /**---Reading--- from Shared Preference*/
        app_preferences = this.getSharedPreferences("UserSettingsActivity", Context.MODE_PRIVATE);
        appColor = app_preferences.getInt("color", -9516113);

        String uri = "@drawable/android_avatar";
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable res = getResources().getDrawable(imageResource);

        /**finding the user profile picture*/
        userImg = findViewById(R.id.user_image_big);
        userImg.setImageDrawable(res);
        userImg.setBorderColor(appColor);

        /**finding 'btnAddPic'*/
        btnAddPic = findViewById(R.id.btnAddPic);

        /**finding 'btnEditName'*/
        btnEditName = findViewById(R.id.btnEditName);

        /**finding 'userNameTextView'*/
        /**get userName from DB and set textView'*/
        userName=findViewById(R.id.userNameTextView);

        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        String usersName = fUser.getDisplayName();
        userName.setText(usersName);


        /**get users picture from DB and update image'*/
        if (currentUser.getPhotoUrl() != null) {
            userImg.setImageURI(Uri.parse(String.valueOf(currentUser.getPhotoUrl())));
       }

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
                        Uri path = Uri.parse("android.resource://com.example.finalproject/" + R.drawable.android_avatar);
                        /**firebase*/
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(path).build();
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        currentUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User profile updated.");
                                        }
                                    }
                                });

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
                editUser=dialog.findViewById(R.id.userNameEditText);

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

                        FirebaseAuth auth = FirebaseAuth.getInstance();

                        userName.setText(editUser.getText().toString());
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(String.valueOf(editUser.getText())).build();
                        fUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                            }
                        });

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
                userImg.setBorderColor(appColor = getColor(R.color.t_pink));
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

                    /**firebase*/
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(picturePath)).build();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                    }
                                }
                            });
                }
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                if (null != bitmap) {
                    userImg.setImageBitmap(bitmap);

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                    String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap,"val",null);

                    /**firebase*/
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(path)).build();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                    }
                                }
                            });
                    /**end of firebase*/
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

        /**refreshing the activity so the nav drawer will updated also
         * (border color, image , user name)*/
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}
