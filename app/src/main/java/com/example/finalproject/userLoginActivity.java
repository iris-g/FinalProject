package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
        Button login ;
        Button signUp ;
        TextInputEditText email;
        TextInputEditText password;
        int userId;
        AppDataBase db;
        FirebaseAuth auth;
        private LoginTable getAllData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login= findViewById(R.id.logIn_button);
        email=findViewById(R.id.userEmail);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.sign_up);
        final String[] emailStr = new String [1];
        final   String [] passwordStr=new String [1];
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        FirebaseAuth auth = FirebaseAuth.getInstance();
         // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference();
        root.setValue("user");

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
                    //check if email and password are correct

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        FirebaseUser fUser = auth.getCurrentUser();
                        auth.signInWithEmailAndPassword(emailStr[0],passwordStr[0]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(userLoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
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



        });



        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent userSignup = new Intent(getApplicationContext(),UserSignUp.class);
                startActivity(userSignup);

            }
        });

    }
}
