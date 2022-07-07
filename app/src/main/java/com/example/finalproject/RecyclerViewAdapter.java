package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ViewModelStoreOwner, LifecycleOwner {

    ViewModel model;
    private int selectedPos =  RecyclerView.NO_POSITION;;
    Context context;
    ArrayList<Item> shownItems;
    TextView items;

    public RecyclerViewAdapter(ViewModel model, Context context) {
        this.model= new ViewModelProvider((FragmentActivity)context).get(ViewModel.class);
        this.context=context;
        shownItems = new ArrayList<Item>();

        //generate live data objects
        model.getSelectedRow();
        model.getItems();
        model.getItemsCount();

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
    //change - updates shown countries
    public void updateItemsList(String name, String description ) {

        shownItems.add(shownItems.size(), new Item(name,description));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.row_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
        holder.itemView.setSelected(selectedPos == position);
        selectedPos=model.getSelectedRow().getValue();
        //int current = model.getSelectedRow().getValue();
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
            tvName.setText(item.getName());
            tvShort.setText(item.getDetails());
            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view )
                {
                    OnItemLongClick(position);
                  //  ((MainActivity)context) .setInitCountry(null);

                    return false;

                }

            });


        }
        private void OnItemLongClick(int position) {
            notifyDataSetChanged();
            Item item1 = shownItems.get(position);
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
}
