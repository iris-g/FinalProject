package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class EditListActivity extends AppCompatActivity {


    //widgets
    RecyclerViewAdapter adapter;
    ViewModel model;
    RecyclerView rvItems;
    Button addBtn ;
    EditText itemName;
    EditText itemDescription;
    TextView listName;
    TextView items;
    CheckBox chkYourCheckBox;

    //vars
    String lName;
    Map<String, Object> dataMap = new HashMap<>();
    List<Item> itemsList;
    String listId = null;
    String productId=null;

    //data base
    FirebaseAuth auth;
    private CollectionReference userShoppingListsRef;
    private CollectionReference itemsRef;
    private View mParentLayout;
    AppDataBase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        //widgets
        addBtn = findViewById(R.id.add_btn);
        rvItems=findViewById(R.id.recycler_view);
        items=findViewById(R.id.items_num);
        listName=findViewById(R.id.list_name);
        itemDescription=findViewById(R.id.description_data);
        itemName= findViewById(R.id.item_name);
        mParentLayout = findViewById(android.R.id.content);

        //vars
        final String[] name = new String[1];
        final String[] additionalData = new String[1];
        itemsList = new ArrayList<Item>();

        //db
        model = new ViewModelProvider(this).get(ViewModel.class);
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        auth = FirebaseAuth.getInstance();

        //create and set adapter for recycler view
        adapter = new RecyclerViewAdapter(model,this);
        rvItems.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItems.getContext(),
                manager.getOrientation());
        rvItems.addItemDecoration(dividerItemDecoration);

        //get list name and update UI
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            lName=(String) bundle.get("name");
            listName.setText(lName);

            //check if list llready has items in DB
            //getListId(lName);
            getListItems(lName);
            //if there are items in list update the adapter
            if(itemsList.size() > 0)
            {
                for(int i=0;i<itemsList.size();i++)
                adapter.updateItemsList(itemsList.get(i).getName(), itemsList.get(i).getDetails(),itemsList.get(i).productId);
                items.setText(String.valueOf(adapter.getItemCount()));

            }
        }

        // Update the UI if number of items changed using live data
        model.getItemsCount().observe(this, itemsNum -> {

            items.setText(String.valueOf(itemsNum));

        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*create dialog for adding items*/
                AlertDialog.Builder builder = new AlertDialog.Builder(EditListActivity.this);
                View mView =  getLayoutInflater().inflate(R.layout.dialog, null);
                final EditText name_input = (EditText) mView.findViewById(R.id.item_name);
                final EditText data_input = (EditText) mView.findViewById(R.id.description_data);
                name_input.setInputType(InputType.TYPE_CLASS_TEXT );
                data_input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setCancelable(false);
                builder.setView(mView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                            name[0] = name_input.getText().toString();
                            additionalData[0] = data_input.getText().toString();
                            /** create and insert item to data base **/
                            createNewItem( name[0], additionalData[0]);
                            adapter.updateItemsList(name[0], additionalData[0],productId);
                            adapter.notifyDataSetChanged();
                            /** update items number in live data **/
                            items.setText(String.valueOf(adapter.getItemCount()));

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

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

                        Item item = new Item(String.valueOf(dataMap.get("name")), String.valueOf(dataMap.get("details")),String.valueOf(dataMap.get("itemId")));
                        itemsList.add(item);
                        adapter.updateItemsList(item.getName(),item.getDetails(), String.valueOf(dataMap.get("itemId")));
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
