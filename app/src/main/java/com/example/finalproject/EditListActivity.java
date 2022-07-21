package com.example.finalproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.databinding.ActivityMain1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditListActivity extends DrawerBaseActivity {


    //widgets
    RecyclerViewAdapter adapter;
    ViewModel model;
    RecyclerView rvItems;
    Button addBtn ;
    ImageButton sendBtn;
    EditText itemName;
    EditText itemDescription;
    TextView listName;
    TextView items;


    Dialog dialog;
    /**in order to bind the drawer(side menu)*/
    ActivityMain1Binding activityMain1Binding;

    //vars
    String lName;
    Map<String, Object> dataMap = new HashMap<>();
    List<Item> itemsList;
    String listId = null;
    String productId;
    String[] name ;
    String[] additionalData ;

    //data base
    FirebaseAuth auth;
    private CollectionReference userShoppingListsRef;
    private CollectionReference itemsRef;
    private View mParentLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMain1Binding = activityMain1Binding.inflate(getLayoutInflater());
        setContentView(activityMain1Binding.getRoot());

        //widgets
        addBtn = findViewById(R.id.add_btn);
        rvItems=findViewById(R.id.recycler_view1);
        items=findViewById(R.id.items_num);
        listName=findViewById(R.id.list_name);
        itemDescription=findViewById(R.id.description_data);
        itemName= findViewById(R.id.item_name);
        sendBtn= findViewById(R.id.sendBtn);
        mParentLayout = findViewById(android.R.id.content);


        //vars
        name = new String[1];
        additionalData = new String[1];
        itemsList = new ArrayList<Item>();

        //db
        model = new ViewModelProvider(this).get(ViewModel.class);
        auth = FirebaseAuth.getInstance();

        //create and set adapter for recycler view
        adapter = new RecyclerViewAdapter(model,this);
        rvItems.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);


        //get list name and update UI
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            lName=(String) bundle.get("name");
            listName.setText(lName);

            //check if list llready has items in DB
            getListItems(lName);


        }

        // Update the UI if number of items changed using live data
        model.getItemsCount().observe(this, itemsNum -> {

            items.setText(String.valueOf(itemsNum));

        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new Dialog(EditListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);

                /**New item name*/
                final EditText name_input = dialog.findViewById(R.id.item_name);

                /**New item description*/
                final EditText data_input = dialog.findViewById(R.id.description_data);

                /**add item button*/
                final ImageButton okBtn = dialog.findViewById(R.id.ok_button);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                            name[0] = name_input.getText().toString();
                            additionalData[0] = data_input.getText().toString();
                            /** create and insert item to data base **/
                            createNewItem( name[0], additionalData[0]);
                            adapter.notifyDataSetChanged();
                            /** update items number in live data **/
                            items.setText(String.valueOf(adapter.getItemCount()));
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingActivity = new Intent(getApplicationContext(),SharingListActivity.class);
                sharingActivity.putExtra("list",lName);
                startActivity(sharingActivity);

            }
        });
    }


    /** create new item with name and description and insert into DB  **/
    public void createNewItem(String title, String content) {

        FirebaseUser fUser = auth.getCurrentUser();
        String u = fUser.getDisplayName();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

        /** get reference to products collection  **/
        itemsRef = rootRef.collection("products");
         productId = itemsRef.document().getId();
        Item itemModel = new Item(title, content,productId,lName );

        /** insert into db  **/
        itemsRef.document(productId).set(itemModel).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Item i = new Item(name[0], additionalData[0],productId,lName);
                    adapter.updateItemsList(i);
                    makeSnackBarMessage("Created new item");

                }
                else{
                    makeSnackBarMessage("Failed. Check log.");
                }
            }
        });
    }

    /** get all items in a list by lists name and update adapter  **/
    private void getListItems(String listName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fUser = auth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        itemsRef = rootRef.collection("products");
        db.collection("products").whereEqualTo("listName", listName).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>(){


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc :task.getResult())
                    {
                        dataMap=doc.getData();
                        Item item = new Item(String.valueOf(dataMap.get("name")), String.valueOf(dataMap.get("details")),String.valueOf(dataMap.get("productId")), String.valueOf(dataMap.get("listName")));
                        item.setChecked((Boolean) dataMap.get("checked"));
                        itemsList.add(item);
                        adapter.updateItemsList(item);
                        Log.d("Tag","on complete"+doc.getData())  ;
                    }
                    adapter.notifyDataSetChanged();


                }
                else{
                    Log.d("Tag",task.getException().getMessage())  ;

                }
            }
        });


    }

    /** not used **/
    private void getListId(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fUser = auth.getCurrentUser();
        db.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists").whereEqualTo("shoppingListName",name).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>(){


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc :task.getResult())
                    {
                        dataMap=doc.getData();
                        listId=(String.valueOf(dataMap.get("shoppingListId")));
                        getListItems(lName);
                        adapter.notifyDataSetChanged();
                        Log.d("Tag","on complete"+doc.getData())  ;
                    }
                    adapter.notifyDataSetChanged();


                }
                else{
                    Log.d("Tag",task.getException().getMessage())  ;

                }
            }
        });


    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }



}
