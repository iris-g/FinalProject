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
    Button createBtn ;
    DatabaseReference refernce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        shoppingList= new ArrayList<>();
       // refernce = FirebaseDatabase.getInstance().getReference().child("Time");
         createBtn = findViewById(R.id.create_button);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText = listTitle.getText().toString();
                /* need to add a check if the list already exists  */
                if( !inputText.equals("")) {

                    Intent editListActivity = new Intent(getApplicationContext(),EditListActivity.class);
                    startActivity(editListActivity);
                    finish();


                }




            }
        });
    }


}