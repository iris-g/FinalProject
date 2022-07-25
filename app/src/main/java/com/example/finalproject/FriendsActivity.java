package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity  {
    //widgets
    usersRecyclerViewAdapter adapter;
    RecyclerView rvItems;
    TextView msgText;
    usersViewModel model;

    //vars
    Map<String, Object> dataMap = new HashMap<>();
    List<User> userList;
    String from ;
    ArrayList<User>  friends = new ArrayList<>();

    //fire base
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase dataBase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        rvItems=findViewById(R.id.recycler_view1);
        msgText=findViewById(R.id.message);
        auth = FirebaseAuth.getInstance();
        model = new ViewModelProvider(this).get(usersViewModel.class);
        adapter = new usersRecyclerViewAdapter(model,this);
        rvItems.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(manager);
        msgText.setText("No friends yet");
        getFreindsRequests(auth.getCurrentUser().getEmail());



    }
    /* get all friends that share their list with the user                  */
    private void getFreindsRequests(final String currentUserId){
        final User[] user = new User[1];
        final boolean[] res = {false};
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("name",auth.getCurrentUser().getDisplayName());
        notificationMap.put("from",currentUserId);

        db.collection("Users/"+currentUserId+"/Notifications")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot>task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc :task.getResult())
                            {
                                dataMap=doc.getData();

                                // adapter.updateUsersList(new User(String.valueOf(dataMap.get("name")), String.valueOf(dataMap.get("from"))));
                                friends.add(new User(String.valueOf(dataMap.get("name")), String.valueOf(dataMap.get("from")))) ;
                                from=String.valueOf(dataMap.get("from"));
                                Log.d("Tag","on complete"+doc.getData())  ;
                            }
                            for(int i=0; i <friends.size();i++)
                                adapter.updateUsersList(friends.get(i));
                            adapter.notifyDataSetChanged();
                            msgText.setText("");
                        }
                        else{
                            Log.d("Tag",task.getException().getMessage())  ;

                            msgText.setText("No friends yet");

                        }
                    }
                });

    }






}



