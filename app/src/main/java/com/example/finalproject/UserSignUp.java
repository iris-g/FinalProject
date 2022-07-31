package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class UserSignUp extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;

    Button signUp ;
    private LoginTable userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**setting the content to "Sign Up" activity (new user want to register)*/
        setContentView(R.layout.sign_up);

        /**finding all of the widgets in the layout*/
        /**edit text fields*/
        name=findViewById(R.id.name_txt);
        email= findViewById(R.id.txtEmailAddress);
        password=findViewById(R.id.txtPassword);

        /**sign up button*/
        signUp=findViewById(R.id.signUp);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
      //  FirebaseDatabase ref = FirebaseDatabase.getInstance();

        /**setting listener to "Sign Up" button*/
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trim() - removes whitespace from both ends of a string
                String strEmail = email.getText().toString().trim();
                String strPassword = password.getText().toString().trim();
                String strName = name.getText().toString();

                LoginTable data = new LoginTable();

                /**checking for empty fields in 'Sign Up' form*/

                if (TextUtils.isEmpty(strEmail)) {
                  email.setError("Please Enter Your E-mail Address");
                }
                 if (TextUtils.isEmpty(strPassword)) {
                    password.setError("Please Enter Your Password");
                }
                if (TextUtils.isEmpty(strName)) {
                    name.setError("Please Enter Your name ");
                }
                else {

                    FirebaseAuth auth = FirebaseAuth.getInstance();

                    /**creating new user with email & password*/
                    auth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        /**getting the current logged-in user*/
                                        FirebaseUser fUser = auth.getCurrentUser();

                                        /**User is a class defined by us*/
                                        User user = new User(strName, strEmail);

                                        /**DatabaseReference ref*/
                                        /**red.child("Users") creates a table called 'Users'*/
                                        /**getUid() - getting the currently logged-in user*/

                                        /**we are setting a value to 'User' struct */
                                        ref.child("Users").child(fUser.getUid()).setValue(user);
                                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                        //set and store user display name
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(strName).build();

                                        currentUser.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAG", "User profile updated.");
                                                        }
                                                    }
                                                });

                                        Toast.makeText(UserSignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);

                                        /**starting the main activity*/
                                        startActivity(mainActivity);
                                    }


                                    else {
                                        Context context = getApplicationContext();
                                        CharSequence text = "User already exists with ths email!";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                                    }
                                }
                            });


                        /*
                    //check if user already exists with this email
                    userData= db.listDao().getUserByEmail(strEmail);
                    if(userData== null ) {//user not exists with this email
                        data.setEmail(strEmail);
                        data.setPassword(strPassword);
                        data.setName(strName);
                        db.listDao().insertDetails(data);
                        finish();
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);

                    }
                    else {
                        Context context = getApplicationContext();
                        CharSequence text = "User already exists with ths email!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                    /*
                         */
                }

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(strName).build();
            }
        });
    }
}
