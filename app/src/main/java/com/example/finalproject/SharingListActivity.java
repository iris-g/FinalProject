package com.example.finalproject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SharingListActivity extends AppCompatActivity {
    Button emailBtn ;
    FirebaseFirestore rootRef;
    private String userEmail;
    FirebaseAuth auth;
    FirebaseUser fUser;
    ShoppingListModel shoppingList;
    String shoppingListId;
    private CollectionReference itemsRef;
    Map<String, Object> dataMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_layout);
        emailBtn = findViewById(R.id.email_btn);
        rootRef =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fUser=auth.getCurrentUser();
        ViewModel model  = new ViewModelProvider(this).get(ViewModel.class);
        userEmail= fUser.getEmail();

        /** get reference to products collection  **/
        itemsRef = rootRef.collection("products");


        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get list name and update UI
               ArrayList<Item> items ;
                Bundle bundle = getIntent().getExtras();
                if(bundle != null)
                {
                    String lName=(String) bundle.get("list");
                    shoppingListId=getShoppingListId(lName);


                }



            }

        });
    }

    private void shareShoppingList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SharingListActivity.this);
        builder.setTitle("Share Shopping List");
        builder.setMessage("Please insert your friend's email");

        EditText editText = new EditText(SharingListActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editText.setHint("Type an email address");
        editText.setHintTextColor(Color.GRAY);
        builder.setView(editText);

        builder.setPositiveButton("Add", (dialogInterface, i) -> {
            String friendEmail = editText.getText().toString().trim();
            rootRef.collection("shoppingLists").document(friendEmail)
                    .collection("userShoppingLists").document(shoppingListId)
                    .set(shoppingList).addOnSuccessListener(aVoid -> {
                        Map<String, Object> users = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        map.put(userEmail, true);
                        map.put(friendEmail, true);
                        users.put("users", map);
                        rootRef.collection("shoppingLists").document(userEmail)
                                .collection("userShoppingLists").document(shoppingListId)
                                .update(users);
                        rootRef.collection("shoppingLists").document(friendEmail)
                                .collection("userShoppingLists").document(shoppingListId)
                                .update(users);
                    });
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /** get all items in a list by lists name   **/
    private String getShoppingListId(String listName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fUser = auth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final String[] listId = new String[1];
        itemsRef = rootRef.collection("shoppingLists");
        db.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists").whereEqualTo("shoppingListName",listName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc :task.getResult())
                    {
                        dataMap=doc.getData();
                        listId[0] =(String.valueOf(dataMap.get("shoppingListId")));

                    }
                    shoppingListId=listId[0];
                    shoppingList = new ShoppingListModel(listId[0],listName, fUser.getDisplayName());
                    shareShoppingList();



                }
                else{
                    Log.d("Tag",task.getException().getMessage())  ;

                }
            }
        });

    return listId[0];

    }
}
