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
    //widgets
    List<String> shoppingList;
    TextView listTitle ;
    Button createBtn ;
    ListView listView;
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter adapter;

    //db
    private CollectionReference userShoppingListsRef;
    private FirebaseFirestore rootRef;
    FirebaseAuth auth ;
    FirebaseUser fUser ;

    //vars
    String user_name=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_list_title);
        listTitle=findViewById(R.id.listTitleEditText);
        createBtn = findViewById(R.id.create_button);
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();
        fUser =auth.getCurrentUser();

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

                if( !inputText.equals("")) {

                  ShoppingListModel newList =  createList(inputText);

                  /*  if user presses create button edit list activity is created  */
                    Intent editListActivity = new Intent(getApplicationContext(),EditListActivity.class);
                    editListActivity.putExtra("name", inputText );
                    startActivity(editListActivity);

                }


            }
        });
    }

    /*  create new list and insert into DB using users email */
    public ShoppingListModel createList(String name) {
        userShoppingListsRef = rootRef.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists");
        String shoppingListId = userShoppingListsRef.document().getId();
        ShoppingListModel shoppingListModel = new ShoppingListModel(shoppingListId, name, user_name);
        userShoppingListsRef.document(shoppingListId).set(shoppingListModel).addOnSuccessListener(aVoid -> Log.d("TAG", "Shopping List successfully created!"));
        return shoppingListModel;



    }

}
