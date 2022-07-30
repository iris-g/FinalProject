package com.example.finalproject;

import android.app.AlertDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SharingListActivity extends AppCompatActivity {
    Button emailBtn ;
    Button sendBtn;
    FirebaseFirestore rootRef;
    private String userEmail;
    FirebaseAuth auth;
    FirebaseUser fUser;
    ShoppingListModel shoppingList;
    String shoppingListId;
    private CollectionReference itemsRef;
    Map<String, Object> dataMap = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String friendEmail;
    private CollectionReference mRequestReference;
    public static final String FOLLOW_REQUEST_SENT_NOTIFICATION = "FOLLOW_REQUEST_SENT_NOTIFICATION";

    final NetworkBroadcastReceiver br= new NetworkBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharing_layout);
        emailBtn = findViewById(R.id.email_btn);
        sendBtn=findViewById(R.id.send_button);
        rootRef =FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        fUser=auth.getCurrentUser();
        ViewModel model  = new ViewModelProvider(this).get(ViewModel.class);
        userEmail= fUser.getEmail();
        mRequestReference = rootRef.collection("Requests");
        /** get reference to products collection  **/
        itemsRef = rootRef.collection("products");


        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get list name and update UI
               ArrayList<Item> items ;
                Bundle bundle = getIntent().getExtras();
                if(bundle != null)
                {
                    String lName=(String) bundle.get("list");
                    shoppingListId=getShoppingListId(lName);
                 //   sendNotification("Hello world");

                }



            }

        });
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {
//                    Intent i = new Intent(Intent.ACTION_SEND);
//                    i.setType("text/plain");
//                    i.putExtra(Intent.EXTRA_SUBJECT, "My app name");
//                    String strShareMessage = "\nLet me recommend you this application\n\n";
//                    strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
//                    Uri screenshotUri = Uri.parse("android.resource://packagename/drawable/image_name");
//                    i.setType("image/png");
//                    i.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                    i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
//                    startActivity(Intent.createChooser(i, "Share via"));
                    ShareCompat.IntentBuilder.from(SharingListActivity.this)
                            .setType("text/plain")
                            .setChooserTitle("Chooser title")
                            .setText("http://play.google.com/store/apps/details?id=" + SharingListActivity.this.getPackageName())
                            .startChooser();
                } catch(Exception e) {
                    //e.toString();
                }

            }


        });

    }

    private void shareShoppingList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SharingListActivity.this);
        builder.setTitle("Share Shopping List");
        builder.setMessage("Please insert your friend's email");

        EditText editText = new EditText(SharingListActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editText.setHint("Type an email address");
        editText.setHintTextColor(Color.GRAY);
        builder.setView(editText);

        builder.setPositiveButton("Add", (dialogInterface, i) -> {
            friendEmail = editText.getText().toString().trim();
            rootRef.collection("shoppingLists").document(friendEmail)
                    .collection("userShoppingLists").document(shoppingListId)
                    .set(shoppingList).addOnSuccessListener(aVoid -> {
                        Map<String, Object> users = new HashMap<>();
                        Map<String, Object> map = new HashMap<>();
                        map.put(userEmail, true);
                        map.put(friendEmail, true);
                        users.put("users", map);
                        rootRef.collection("shoppingLists").document(userEmail)
                                .collection("userShoppingLists").document(shoppingListId)
                                .update(users);
                        rootRef.collection("shoppingLists").document(friendEmail)
                                .collection("userShoppingLists").document(shoppingListId)
                                .update(users);
                    });
            notificationForFollowRequest(userEmail,friendEmail);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /** get all items in a list by lists name   **/
    private String getShoppingListId(String listName){

        FirebaseUser fUser = auth.getCurrentUser();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final String[] listId = new String[1];
        itemsRef = rootRef.collection("shoppingLists");
        db.collection("shoppingLists").document(fUser.getEmail()).collection("userShoppingLists").whereEqualTo("shoppingListName",listName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc :task.getResult())
                    {
                        dataMap=doc.getData();
                        listId[0] =(String.valueOf(dataMap.get("shoppingListId")));

                    }
                    shoppingListId=listId[0];
                    shoppingList = new ShoppingListModel(listId[0],listName, fUser.getDisplayName());
                    shareShoppingList();
                }
                else{
                    Log.d("Tag",task.getException().getMessage())  ;
                }
            }
        });

    return listId[0];
    }

    private void notificationForFollowRequest(final String currentUserId, final String otherUserId){

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("name",auth.getCurrentUser().getDisplayName());
        notificationMap.put("from",currentUserId);

        db.collection("Users/"+otherUserId+"/Notifications")
                .add(notificationMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            showToast();
                        } else {
                            Toast.makeText(SharingListActivity.this,"Failed to send notification",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**DYNAMIC REGISTRATION (run-time)*/
        /**to Network availability*/
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    public void showToast(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.toast_layout,
        (ViewGroup) findViewById(R.id.toast_root));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}

