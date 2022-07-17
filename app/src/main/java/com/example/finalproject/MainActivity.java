package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    ActivityMainBinding activityMainBinding;
    AppDataBase db;
    ListView listView;
    TextView textView;
    List<String> shoppingList;
    ViewModel model;
    private CollectionReference userShoppingListsRef;
    private FirebaseFirestore rootRef;
    String user_name=null;
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    ArrayList<Type> mArrayList =new ArrayList<Type>();
    Map<String, Object> dataMap = new HashMap<>();
    ArrayAdapter<String> adapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        final String[] s = new String[1];
        allocateActivityTitle(" ");
      setContentView(activityMainBinding.getRoot());
        textView= findViewById(R.id.textView);
        listView = findViewById(R.id.list_view);
        model = new ViewModelProvider(this).get(ViewModel.class);
        shoppingList= new ArrayList<>();
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        listDao listDao = db.listDao();
        rootRef = FirebaseFirestore.getInstance();
        String emailAdd = null;
        //get currently connected user
        FirebaseUser fUser = auth.getCurrentUser();
        userShoppingListsRef = rootRef.collection("shoppingLists").document(  fUser.getEmail()).collection("userShoppingLists");
        String shoppingListId = userShoppingListsRef.document().getId();
        FirebaseFirestore  dataBase = FirebaseFirestore.getInstance();
        //load users lists from database
        getData( fUser.getEmail());

        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.listview_color_and_text, R.id.item_text, shoppingList);
        listView.setAdapter(adapter);
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
                ShoppingList toRemove = new ShoppingList();
               // shoppingList.remove( lists.size()-1);
                toRemove.name=sList;
                db.listDao().deleteLists(toRemove);
                adapter.notifyDataSetChanged();
                return false;
            }

        });


        Button addBtn = findViewById(R.id.btn_add);
        String finalEmailAdd = emailAdd;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent ListActivity = new Intent(getApplicationContext(),CreateListActivity.class);
                       ListActivity.putExtra("email", finalEmailAdd);
                   startActivity(ListActivity);
                    //finish();
                }

        });
    //}

    //    public void showToast()
//    {
//        LayoutInflater layoutInflater = getLayoutInflater();
//        View layout = layoutInflater.inflate(R.layout.toast_layout,
//                (ViewGroup) findViewById(R.id.toast_root));
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
    }
private void getData(String email){
   FirebaseFirestore db = FirebaseFirestore.getInstance();
   db.collection("shoppingLists").document(email).collection("userShoppingLists").get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>(){


       @Override
       public void onComplete(@NonNull Task<QuerySnapshot> task) {
           if(task.isSuccessful()){
               for(QueryDocumentSnapshot doc :task.getResult())
               {
                   dataMap=doc.getData();
                   shoppingList.add(String.valueOf(dataMap.get("shoppingListName")));
                 Log.d("Tag","on complete"+doc.getData())  ;
               }
               adapter.notifyDataSetChanged();
               textView.setText("Create new list or view lists");

           }
           else{
               Log.d("Tag",task.getException().getMessage())  ;

           }
       }
   });


}

}