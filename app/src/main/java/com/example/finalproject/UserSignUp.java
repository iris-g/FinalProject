package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
      //  FirebaseDatabase ref = FirebaseDatabase.getInstance();
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

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser fUser = auth.getCurrentUser();
                                        User user = new User(strName, strEmail);
                                        ref.child("Users").child(fUser.getUid()).setValue(user);
                                        Toast.makeText(UserSignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(mainActivity);
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


            }
        });


    }

}
