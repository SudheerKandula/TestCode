package com.codeclub.JustMaths;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public  class RegisteredUsers {
    public static ArrayList<String> myUsers=new ArrayList<String>();
    public ArrayList<String> getContacts() {
        //to fetch all the users of firebase Auth app
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
            System.out.println(myUsers.size());

        return myUsers;
    }

}

