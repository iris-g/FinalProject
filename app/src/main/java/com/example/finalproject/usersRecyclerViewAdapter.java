package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class usersRecyclerViewAdapter extends RecyclerView.Adapter<usersRecyclerViewAdapter.myViewHolder> implements ViewModelStoreOwner, LifecycleOwner {
    usersViewModel model;
    ImageView deleteIcon;
    //vars
    Context context;
    ArrayList<User> shownUsers;

    //firebase
    FirebaseFirestore rootRef;
    CollectionReference usersRef;
    SharedPreferences app_preferences;
    int appColor;
    private int selectedPos =  RecyclerView.NO_POSITION;;
    FirebaseAuth auth;




    public usersRecyclerViewAdapter(usersViewModel model, Context context) {
        this.model= new ViewModelProvider((FragmentActivity)context).get(usersViewModel.class);
        this.context=context;
        shownUsers = new ArrayList<User>();
        //generate live data objects
        model.getUsers();
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
    public void updateUsersList(User user ) {

        shownUsers.add(shownUsers.size(), user);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=inflater.inflate(R.layout.item_view,parent,false);



        /**getting the selected color by user from the SP*/
        app_preferences = context.getSharedPreferences("UserSettingsActivity", Context.MODE_PRIVATE);
        appColor = app_preferences.getInt("color", -9516113);

        /**setting color to the shopping liast items according to the color in the SP file*/
        itemView.findViewById(R.id.card_color).setBackgroundColor(appColor);
        myViewHolder viewHolder = new myViewHolder(itemView);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull  myViewHolder  holder, int position) {
        holder.bindData(position);
        holder.itemView.setSelected(selectedPos == position);
        selectedPos=model.getSelectedRow().getValue();
        CheckBox chkYourCheckBox ;
        chkYourCheckBox= holder.itemView.findViewById(R.id.checkBox);
        User user = shownUsers.get(position);
        // chkYourCheckBox.setChecked(item.isChecked);



        if(selectedPos == position)
            holder.itemView.setBackgroundColor(Color.WHITE);
        else
            holder.itemView.setBackgroundColor(Color.rgb(91,165,230));
    }

    @Override
    public int getItemCount() {
        return this.shownUsers.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvShort;
        View itemView;



        myViewHolder(View itemView) {
            super(itemView);
            this.itemView =itemView;
            itemView.setClickable(true);
            tvName = (TextView)itemView.findViewById(R.id.name);
            tvShort=(TextView)itemView.findViewById(R.id.shorty);
            deleteIcon=itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(this);



        }


        public void bindData(final int position) {
            try {
                final User user = shownUsers.get(position);
                model.setItemsCount(shownUsers.size());
                if (user.getName() != null && user.getEmail() != null) {
                    tvName.setText(user.getName());
                    tvShort.setText(user.getEmail());
                }
            }catch(Exception e){


            }
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyDataSetChanged();
                    User user = shownUsers.get(position);
                    /*   delete friend from data base  */
                    deleteUser(user.getName());
                    String name = user.name;
                    /*   remove friend from shown users in recycler view */
                    shownUsers.remove(user);
                    model.setUsers(shownUsers);
                }
            });
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
//            notifyDataSetChanged();
//            User user = shownUsers.get(position);
//            /*   delete friend from data base  */
//            deleteUser(user.getName());
//            String name = user.name;
//            /*   remove friend from shown users in recycler view */
//            shownUsers.remove(user);
//            model.setUsers(shownUsers);
//            model.setItemsCount(shownUsers.size());
//            notifyDataSetChanged();
//            model.setSelectedRow(-1);
//            notifyItemChanged(selectedPos);
//            notifyDataSetChanged();
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



    /*   delete a user from friends list using a query that finds that user by its name  */
    public void deleteUser(String userName) {
        rootRef.collection("Users/"+auth.getCurrentUser().getEmail()+"/Notifications")
                // where we are storing our items
                .whereEqualTo("name",userName) .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && !value.getDocuments().isEmpty()) {
                            List<DocumentSnapshot> documents = value.getDocuments();
                            for (DocumentSnapshot document : documents) {
                                DocumentReference documentReference = document.getReference();
                                documentReference.delete();
                            }
                        }
                    }
                });;



    }


}
