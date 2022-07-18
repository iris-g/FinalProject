package com.example.finalproject;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_layout);
        emailBtn = findViewById(R.id.email_btn);
        rootRef =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fUser=auth.getCurrentUser();
        userEmail= fUser.getEmail();
        shoppingList = new ShoppingListModel("cdjnrhyi5","me","wow");
         shoppingListId = "vzSXYlsH2jnC5ncoTnpS";
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareShoppingList();

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
}
