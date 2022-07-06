package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {
    List<String> shoppingList;
    TextView listTitle ;
    Button createBtn = findViewById(R.id.create_button);
    DatabaseReference refernce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        shoppingList= new ArrayList<>();
        refernce = FirebaseDatabase.getInstance().getReference().child("Time");


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText = listTitle.getText().toString();
                if( !inputText.equals("")) {



                }
                /* if we reached last intro screen and next pressed open main layout  */
                    Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainActivity);

                    finish();



            }
        });
    }


}
