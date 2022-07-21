package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
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
    List<User> userList;
    usersViewModel model;
    FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> dataMap = new HashMap<>();
    FirebaseDatabase dataBase ;
    DatabaseReference ref;
    TextView msgText;
    String from ;
    ArrayList<User>  friends = new ArrayList<>();
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


//        final DocumentReference docRef = db.collection("Users").document(auth.getCurrentUser().getEmail());
//        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                if (error != null) {
//                    Log.w("TAG", "Listen failed.", error);
//                    return;
//                }
//
//                if (value != null && value.exists()) {
//                    Log.d("TAG", "Current data: " + value.getData());
//                    startService("You have a friend request from ");
//                } else {
//                    Log.d("TAG", "Current data: null");
//                }
//            }
//        });


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


    public void startService(String value) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", value);
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }



}



