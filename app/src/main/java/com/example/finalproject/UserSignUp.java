package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserSignUp extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText password;
    AppDataBase db;
    Button signUp ;
    private LoginTable userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        name=findViewById(R.id.name_txt);
        email= findViewById(R.id.txtEmailAddress);
        password=findViewById(R.id.txtPassword);
        signUp=findViewById(R.id.signUp);
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String strEmail = email.getText().toString().trim();
                String strPassword = password .getText().toString().trim();
                String strName = name.getText().toString();
                LoginTable data = new LoginTable();

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
                }


            }
        });


    }

}
