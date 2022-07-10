package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends DrawerBaseActivity {
    ActivityMainBinding activityMainBinding;
    AppDataBase db;
    ListView listView;
    TextView textView;
    List<String> shoppingList;
    ViewModel model;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        allocateActivityTitle(" ");
      setContentView(activityMainBinding.getRoot());
      //////get exisiting lists from DB
        textView= findViewById(R.id.textView);
         listView = findViewById(R.id.list_view);
        shoppingList= new ArrayList<>();
        db  = AppDataBase.getDbInstance(this.getApplicationContext());
        listDao listDao = db.listDao();
        model = new ViewModelProvider(this).get(ViewModel.class);

        //list of names of all existing lists for this user
        List<ShoppingList> lists = listDao.getAllItemsList();

        if(lists.size()>0 )
            textView.setText("Create new list or view lists");
            //add all lists names  from DB to a list of strings
        for(int i=0; i < lists.size();i++)
          shoppingList.add(lists.get(i).name);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.item_view, R.id.itemTextView, shoppingList);
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
                shoppingList.remove( lists.size()-1);
                toRemove.name=sList;
                db.listDao().deleteLists(toRemove);
                adapter.notifyDataSetChanged();
                return false;
            }

        });


        Button addBtn = findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent ListActivity = new Intent(getApplicationContext(),CreateListActivity.class);
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


}