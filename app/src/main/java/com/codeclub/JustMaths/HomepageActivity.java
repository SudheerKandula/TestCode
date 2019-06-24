package com.codeclub.JustMaths;



import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.net.SocketOption;
import java.util.*;

public class HomepageActivity extends AppCompatActivity {

    //    public static String[] myUsersArray = new String[]
    private static final String TAG = "HomepageActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final int RESULT_PICK_CONTACT = 1;
    private TextView follower;
    private Button select;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
    private EventListener eventListener;
    public static ArrayList<String> myUsers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        follower = findViewById(R.id.follower);
        select = findViewById(R.id.select);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(in, RESULT_PICK_CONTACT);

            }
        });

        if (isServicesOK()) {
            init();
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("Users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> myContacts = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String userPhone = ds.child("phone").getValue(String.class);

                    Log.d("TAG", userPhone);
//                    myUsers.add(userPhone);
                    myContacts.add(userPhone);

                }
                myUsers.clear();
                myUsers.addAll(myContacts);

//                ArrayAdapter<String> adapter = new ArrayAdapter(HomepageActivity.this, android.R.layout.simple_list_item_1, myUsers);
//
//                mListview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
//        usersdRef.addListenerForSingleValueEvent(eventListener);
        usersdRef.addValueEventListener(eventListener);
    }

    private void init() {
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomepageActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine & the user can perform a map request
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occurred which can be resolved
            Log.d(TAG, "isServicesOK: an error occurred but can be resolved");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomepageActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    RegisteredUsers allContacts = new RegisteredUsers();
                    ArrayList<String> contactsList = allContacts.getContacts();
//                    Toast.makeText(this, "Contacts from Database size: " + myUsers.size(), Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Toast.makeText(this, "Failed to Pick Contact", Toast.LENGTH_SHORT).show();
        }
    }

//    public class UAT {
//    private void getUsers(String[] args) {
//         final ArrayList<String> myUsers=new ArrayList<String>();
//        //to fetch all the users of firebase Auth app
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//
//        DatabaseReference usersdRef = rootRef.child("Users");
//
//
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    String name = ds.child("phone").getValue(String.class);
//
//                    Log.d("TAG", name);
//
//                    myUsers.add(name);
//
//                }
//                boolean ans = myUsers.contains(follower);
//                 if(ans)
//                     System.out.println("Valid contact");
//                 else
//                     System.out.println("Failed, Invalid contact");
//                Toast.makeText(HomepageActivity.this, "Invalid contact", Toast.LENGTH_SHORT).show();
//
//
////                ArrayAdapter<String> adapter = new ArrayAdapter(HomepageActivity.this, android.R.layout.simple_list_item_1, myUsers);
////
////                mListview.setAdapter(adapter);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        usersdRef.addListenerForSingleValueEvent(eventListener);
//    }
//
//};

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = "";
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            System.out.println(phoneNo);

            if (myUsers.contains(phoneNo)) {
                follower.setText(phoneNo);
                Toast.makeText(this, "Valid App User", Toast.LENGTH_SHORT).show();
            }
            else {
                follower.setText("Invalid contact");
                Toast.makeText(this, "Not a Registered App User", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


