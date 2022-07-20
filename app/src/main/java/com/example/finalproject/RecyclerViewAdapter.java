package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ViewModelStoreOwner, LifecycleOwner {

    ViewModel model;
    private int selectedPos =  RecyclerView.NO_POSITION;;
    Context context;
    ArrayList<Item> shownItems;
    CollectionReference itemsRef;
    TextView items;
    FirebaseFirestore rootRef;
    ArrayList<String> checkBoxes = new ArrayList<>();
    SharedPreferences app_preferences;
    int appColor;
    FirebaseAuth auth;

    public RecyclerViewAdapter(ViewModel model, Context context) {
        this.model= new ViewModelProvider((FragmentActivity)context).get(ViewModel.class);
        this.context=context;
        shownItems = new ArrayList<Item>();
        //generate live data objects
        model.getSelectedRow();
        model.getItems();
        model.getItemsCount();

        rootRef = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return null;
    }

    public void updateItemsList(Item item ) {

         shownItems.add(shownItems.size(), item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.row_item,parent,false);


        /**getting the selected color by user from the SP*/
        app_preferences = context.getSharedPreferences("UserSettingsActivity", Context.MODE_PRIVATE);
        appColor = app_preferences.getInt("color", -9516113);

        /**setting color to the shopping liast items according to the color in the SP file*/
        itemView.findViewById(R.id.card_color).setBackgroundColor(appColor);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
        holder.itemView.setSelected(selectedPos == position);
        selectedPos=model.getSelectedRow().getValue();
        CheckBox chkYourCheckBox ;
        chkYourCheckBox= holder.itemView.findViewById(R.id.checkBox);
        Item item = shownItems.get(position);
        chkYourCheckBox.setChecked(item.isChecked);


        chkYourCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(chkYourCheckBox.isChecked()) {
                    updateCheckedState(item,true);

                }
                else  {
                    updateCheckedState(item,false);
                }


            }

        });
        if(selectedPos == position)
            holder.itemView.setBackgroundColor(Color.WHITE);
        else
            holder.itemView.setBackgroundColor(Color.rgb(91,165,230));
    }

    @Override
    public int getItemCount() {
        return this.shownItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvShort;
        View itemView;



        ViewHolder(View itemView) {
            super(itemView);
            this.itemView =itemView;
            itemView.setClickable(true);
            tvName = (TextView)itemView.findViewById(R.id.name);
            tvShort=(TextView)itemView.findViewById(R.id.shorty);
            itemView.setOnClickListener(this);



        }


        public void bindData(final int position) {
           final Item item = shownItems.get(position);
            model.setItemsCount(shownItems.size());
            tvName.setText(item.getName());
            tvShort.setText(item.getDetails());
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view )
                {
                    OnItemLongClick(position);

                    return false;

                }

            });


        }
        private void OnItemLongClick(int position) {
            notifyDataSetChanged();
            Item item1 = shownItems.get(position);
           deleteItem(item1.getProductId());
            String name = item1.name;

            shownItems.remove(item1);
            model.setItems(shownItems);
            model.setItemsCount(shownItems.size());
            notifyDataSetChanged();
            model.setSelectedRow(-1);
            notifyItemChanged(selectedPos);
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View view) {

            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();


            //update live data
            model.setSelectedRow(selectedPos);
            notifyItemChanged(selectedPos);
        }
    }

    public void deleteItem(String itemId) {
        itemsRef=  rootRef.collection("products");
        // where we are storing our items
        itemsRef. document(itemId).delete();

    }

    /** update checked field in data base  **/
    public void updateCheckedState(Item item , boolean val) {

        /** get reference to products collection  **/
        itemsRef = rootRef.collection("products");
         String  productId = item.getProductId();
        /** update checked=val in  db  **/
        itemsRef.document(productId).update("checked",val).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                }
                else{

                }
            }
        });
    }

}
