package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userLoginActivity extends AppCompatActivity {

    //widgets
    Button login ;
    TextView signUp;
    TextInputEditText email;
    TextInputEditText password;
    //vars
    int userId;
    String[] emailStr ;
    String [] passwordStr;
    //firebase
    FirebaseAuth auth;
    FirebaseDatabase  database;
    private LoginTable getAllData;
    DatabaseReference root;
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login= findViewById(R.id.logIn_button);
        email=findViewById(R.id.userEmail);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.sign_up);
        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        root = database.getReference();
        root.setValue("user");

        emailStr = new String [1];
        passwordStr=new String [1];

        /*   set login on click listener         */
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                emailStr[0] =email.getText().toString();
                passwordStr[0]=password.getText().toString();
                if (TextUtils.isEmpty( email.getText())) {
                  email.setError("Please Enter Your E-mail Address");
                }
                 if (TextUtils.isEmpty( password.getText())) {
                   password.setError("Please Enter Your Password");
                }
                else {

                    /*  perform firebase authentication and create new Activity if successful    */
                     setupFirebaseAuth();

                   }



                }



        });



        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent userSignup = new Intent(getApplicationContext(),UserSignUp.class);
                startActivity(userSignup);

            }
        });


    }
    /*
          ----------------------------- Firebase setup ---------------------------------
       */
    private void setupFirebaseAuth(){

        auth.signInWithEmailAndPassword(emailStr[0],passwordStr[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    fUser = auth.getCurrentUser();
                    Toast.makeText(userLoginActivity.this, "Welcome " +fUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                    mainActivity.putExtra("email", email.getText().toString() );
                    mainActivity.putExtra("name",fUser.getDisplayName());

                    startActivity(mainActivity);
                }else{
                    Toast.makeText(userLoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
