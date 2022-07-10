package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CreateListActivity extends AppCompatActivity {
    List<String> shoppingList;
    TextView listTitle ;
    Button createBtn ;
    AppDataBase db;
    ListView listView;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        createBtn = findViewById(R.id.create_button);
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        createBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String inputText = listTitle.getText().toString();
                ShoppingList list = new ShoppingList();
                list.name=inputText;
                db.listDao().insertLists(list);
                finish();

                /* need to add a check if the list already exists  */
                if( !inputText.equals("")) {

                    Intent editListActivity = new Intent(getApplicationContext(),EditListActivity.class);
                    editListActivity.putExtra("name", inputText );
                    startActivity(editListActivity);
                    //finish();


                }


            }
        });
    }


}
