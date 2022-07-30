package com.example.finalproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends DrawerBaseActivity {

    //widgets
    ActivityMainBinding activityMainBinding;
    ListView listView;
    TextView textView;
    Button addBtn ;
    ViewModel model;

    //vars
    List<String> shoppingList;
    HashMap<String, String> listsData;//a map to save list name as key with list id as value
    String user_name=null;
    ArrayList<Type> mArrayList =new ArrayList<Type>();

    //firebase
    private CollectionReference userShoppingListsRef;
    private FirebaseFirestore rootRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    FirebaseAuth auth ;
    FirebaseFirestore db;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Map<String, Object> dataMap = new HashMap<>();
    ArrayAdapter<String> adapter;
    HashMap<String, String> createdData =new HashMap<>();//a map to save list name as key with list creator as value
    FirebaseUser fUser;
    static  boolean isFirstTime=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        //widgets
        textView= findViewById(R.id.textView);
        listView = findViewById(R.id.list_view);
        addBtn = findViewById(R.id.btn_add);

        allocateActivityTitle(" ");
        model = new ViewModelProvider(this).get(ViewModel.class);

        //vars
        shoppingList= new ArrayList<String>();
        listsData = new HashMap<String, String>();
        final String[] s = new String[1];

        //firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();
        //get currently connected user
        fUser = auth.getCurrentUser();
        //a refernce to users shopping lists
        userShoppingListsRef = rootRef.collection("shoppingLists").document(  fUser.getEmail()).collection("userShoppingLists");
        String shoppingListId = userShoppingListsRef.document().getId();
        FirebaseFirestore  dataBase = FirebaseFirestore.getInstance();


        /*  if activity is created the first time START foreground service- listen to friend notifications  */
        if (isFirstTime) {
            //the app is being launched for first time, do something
            Log.d("TAG", "First time");
            //START foreground service- listen to friend notifications
            Intent serviceIntent = new Intent(this.getBaseContext(),ForegroundService.class);
            this.getBaseContext().startService(serviceIntent);
            // first time task
            // record the fact that the app has been started at least once
            isFirstTime=false;
        }
        else
        {

            //second time launch..
        }


        //load users lists from database
        getData(fUser.getEmail());

        //set adapter
        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listview_color_and_text, R.id.item_text, shoppingList);


        /* if add button pressed open the create list activity  */
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent ListActivity = new Intent(getApplicationContext(),CreateListActivity.class);
                    startActivity(ListActivity);

                }

        });

    }

    /*  get all users shopping lists using his email and update adapter*/
    private void getData(String email){

       db.collection("shoppingLists").document(email).collection("userShoppingLists").get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>(){

           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for(QueryDocumentSnapshot doc :task.getResult())
                   {
                       dataMap=doc.getData();
                       /*  insert into list data the lists name with lists id as value */
                       listsData.put(String.valueOf(dataMap.get("shoppingListName")),String.valueOf(dataMap.get("shoppingListId")));
                       createdData.put(String.valueOf(dataMap.get("shoppingListName")),String.valueOf(dataMap.get("createdBy")));
                       shoppingList.add(String.valueOf(dataMap.get("shoppingListName")));
                     Log.d("Tag","on complete"+doc.getData())  ;
                   }
                   adapter.notifyDataSetChanged();
                   String[] array = shoppingList.toArray(new String[0]);
                   listView.setAdapter(new customAdapter(getApplicationContext(), array,createdData ));
                   /* on long click remove list from DB and update array adapter  */
                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                           String list = shoppingList.get(position);
                           Intent edit = new Intent(getApplicationContext(),EditListActivity.class);
                           edit.putExtra("name", list );

                           startActivity(edit);
                       }
                   });
                   listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                       @Override
                       public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                      int pos, long id) {
                           String sList = shoppingList.get(pos);
                           deleteList(sList);
                           adapter.notifyDataSetChanged();

                           return true;
                       }

                   });

                   adapter.notifyDataSetChanged();
                   textView.setText("Create new list or view lists");

               }
               else{
                   Log.d("Tag",task.getException().getMessage())  ;

               }
           }
       });
    }


    /*remove list from DB and update adapter */
    private void deleteList(String lName){
        String listId =listsData.get(lName);
        db.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists").document(listId).delete();
        shoppingList.remove(lName);
        getData(fUser.getEmail());
        adapter.notifyDataSetChanged();



    }

}