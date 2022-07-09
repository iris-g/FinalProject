package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {
    List<String> shoppingList;
    TextView listTitle ;
    Button createBtn ;
    AppDataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        shoppingList= new ArrayList<>();
       // refernce = FirebaseDatabase.getInstance().getReference().child("Time");
         createBtn = findViewById(R.id.create_button);
         db  = AppDataBase.getDbInstance(this.getApplicationContext());

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputText = listTitle.getText().toString();
                ShoppingList list = new ShoppingList();
                list.name=inputText;
                db.listDao().insertItems(list);
                finish();

                /* need to add a check if the list already exists  */
                if( !inputText.equals("")) {
                    listDao listDao = db.listDao();
                    Intent editListActivity = new Intent(getApplicationContext(),EditListActivity.class);
                    List<ShoppingList> lists = listDao.getAllItemsList();
                    editListActivity.putExtra("name", inputText );
                    startActivity(editListActivity);
                    //finish();


                }




            }
        });
    }


}
