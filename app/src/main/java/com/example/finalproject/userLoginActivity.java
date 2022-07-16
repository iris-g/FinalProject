package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userLoginActivity extends AppCompatActivity {
        Button login ;
        Button signUp ;
        TextInputEditText email;
        TextInputEditText password;
         int userId;
         AppDataBase db;
        private LoginTable getAllData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login= findViewById(R.id.logIn_button);
        email=findViewById(R.id.userEmail);
        password=findViewById(R.id.password);
        signUp=findViewById(R.id.sign_up);
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
         // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference root = database.getReference();
        root.setValue("anme");
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty( email.getText())) {
                  email.setError("Please Enter Your E-mail Address");
                }
                 if (TextUtils.isEmpty( password.getText())) {
                   password.setError("Please Enter Your Password");
                }
                else {
                    try {
                        getAllData = db.listDao().getUserDetails(email.getText().toString(), password.getText().toString());
                        if (getAllData.getEmail().equals(email.getText().toString()) && getAllData.getPassword().equals(password.getText().toString())) {
                            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                            mainActivity.putExtra("email", email.getText().toString() );
                            startActivity(mainActivity);

                        }
                    }catch(Exception e){

                        Context context = getApplicationContext();
                        CharSequence text = "Wrong email or password ";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
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
