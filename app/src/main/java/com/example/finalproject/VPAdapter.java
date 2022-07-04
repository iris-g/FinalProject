package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ViewPagerItem;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    TabLayout tabIndicator;

    public VPAdapter(List<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = (ArrayList<ViewPagerItem>) viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.info1,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        holder.imageView.setImageResource(viewPagerItem.ScreenImg);
        holder.tcHeading.setText(viewPagerItem.Title);
        holder.tvDesc.setText(viewPagerItem.Description);


    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tcHeading, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.intro_img);
            tcHeading = itemView.findViewById(R.id.intro_title);
            tvDesc = itemView.findViewById(R.id.intro_description);
            tabIndicator = itemView.findViewById(R.id.tab_indicator);
        }
    }

}