package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CreateListActivity extends AppCompatActivity {
    List<String> shoppingList;
    TextView listTitle ;
    Button createBtn ;
    AppDataBase db;
    ListView listView;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter adapter;
    private CollectionReference userShoppingListsRef;
    private FirebaseFirestore rootRef;
    FirebaseAuth auth ;
    String user_name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        createBtn = findViewById(R.id.create_button);
        auth = FirebaseAuth.getInstance();
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        rootRef = FirebaseFirestore.getInstance();
        String emailAdd = null;

        listTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputText = listTitle.getText().toString().trim();

                createBtn.setEnabled(!inputText.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String inputText = listTitle.getText().toString();
                FirebaseUser fUser = auth.getCurrentUser();

                /* need to add a check if the list already exists  */
                if( !inputText.equals("")) {
                    userShoppingListsRef = rootRef.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists");
                    String shoppingListId = userShoppingListsRef.document().getId();
                    ShoppingListModel shoppingListModel = new ShoppingListModel(shoppingListId, inputText, user_name);
                    userShoppingListsRef.document(shoppingListId).set(shoppingListModel).addOnSuccessListener(aVoid -> Log.d("TAG", "Shopping List successfully created!"));
                    Intent editListActivity = new Intent(getApplicationContext(),EditListActivity.class);
                    editListActivity.putExtra("name", inputText );
                    editListActivity.putExtra("shoppingList", shoppingListModel );
                    startActivity(editListActivity);
                    //finish();


                }


            }
        });
    }


}
