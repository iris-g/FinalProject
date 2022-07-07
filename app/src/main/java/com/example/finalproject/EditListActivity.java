package com.example.finalproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditListActivity extends AppCompatActivity {
    Button addBtn ;
    private RecyclerViewAdapter adapter;
    ViewModel model;
    private RecyclerView rvItems;
    EditText itemName;
    EditText itemDescription;
    TextView listName;
    TextView items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn = findViewById(R.id.add_btn);
        rvItems=findViewById(R.id.recycler_view);
        items=findViewById(R.id.items_num);
        final String[] name = new String[1];
        final String[] additionalData = new String[1];
        model = new ViewModelProvider(this).get(ViewModel.class);
        listName=findViewById(R.id.list_name);

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
            String s =(String) bundle.get("name");
            listName.setText(s);
        }
        // Update the UI if number of items changed
        model.getItemsCount().observe(this, itemsNum -> {

            items.setText(String.valueOf(itemsNum));

        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemDescription=findViewById(R.id.description_data);
                itemName= findViewById(R.id.item_name);

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
                            adapter.updateItemsList(name[0], additionalData[0]);
                            items.setText(String.valueOf(adapter.getItemCount()));

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }



        });
    }












}
