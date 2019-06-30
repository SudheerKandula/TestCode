package com.codeclub.JustMaths;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;

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

//    //firebase
//    private FirebaseAuth.AuthStateListener mAuthListener;

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

        //as part of trail
//        setupFirebaseAuth();

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

//    private void setSupportActionBar(Toolbar toolbar) {
//    }

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

//
//    private void initFCM(){
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "initFCM: token: " + token);
//        sendRegistrationToServer(token);
//
//    }
//    public void openMessageDialog(String userId){
//        System.out.println("Entered loop for userid is:"+ userId);
//        Log.d(TAG, "openMessageDialog: opening a dialog to send a new message");
//        MessageDialog2 dialog = new MessageDialog2();
//
//        Bundle bundle = new Bundle();
//        bundle.putString(getString(R.string.intent_user_id), userId);
//        dialog.setArguments(bundle);
//        dialog.show(getSupportFragmentManager(), getString(R.string.dialog_message));
//    }

//    /**
//     * Persist token to third-party servers.
//     *
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private void sendRegistrationToServer(String token) {
//        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        reference.child(getString(R.string.dbnode_users))
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child(getString(R.string.field_messaging_token))
//                .setValue(token);
//    }

    /*
       ----------------------------- Firebase setup ---------------------------------
*/
//    private void setupFirebaseAuth(){
//        Log.d(TAG, "setupFirebaseAuth: started.");
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//
//                } else {
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    Intent intent = new Intent(HomepageActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                }
//                // ...
//            }
//        };
//    }

//    @Override
//    public void onBackPressed() {
//        if (dl.isDrawerOpen(GravityCompat.START)) {
//            dl.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return  abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
//    }


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
//                initFCM();
//                String cuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                System.out.println("the current userid is:" + cuserid);
//                openMessageDialog(cuserid);
            }
            else {
                follower.setText("Invalid contact");
                Toast.makeText(this, "Not a Registered App User", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
//        }
//    }

    //as part of new intent opening for userlist

    /*
    ----------------------------- Firebase setup ---------------------------------
 */
//    private void setupFirebaseAuth(){
//        Log.d(TAG, "setupFirebaseAuth: started.");
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    Toast.makeText(HomepageActivity.this, "Authenticated with: " + user.getEmail(), Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(HomepageActivity.this, UserListActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                    //check for extras from FCM
//                    if (getIntent().getExtras() != null) {
//                        Log.d(TAG, "initFCM: found intent extras: " + getIntent().getExtras().toString());
//                        for (String key : getIntent().getExtras().keySet()) {
//                            Object value = getIntent().getExtras().get(key);
//                            Log.d(TAG, "initFCM: Key: " + key + " Value: " + value);
//                        }
//                        String data = getIntent().getStringExtra("data");
//                        Log.d(TAG, "initFCM: data: " + data);
//                    }
//                    startActivity(intent);
//                    finish();
//
//
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // ...
//            }
//        };
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
//        }
//    }

}


