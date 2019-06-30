package com.codeclub.JustMaths;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

//import android.app.Dialog;
//import android.os.Handler;
//import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private Button buttonLogin;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private FirebaseAuth firebaseAuth;
    private FirebaseApp firebaseApp;
//    private ProgressBar mprogressBar;
//    private TextView mloadingText;
//    private Integer mprogressStatus = 0;
//    private Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

//        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mloadingText = (TextView) findViewById(R.id.loadingText);
//        progressBar = new ProgressBar(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);

        buttonRegister.setOnClickListener(MainActivity.this);
        buttonLogin.setOnClickListener(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null){
            //Handle the already login user
        }
    }
    //        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (mprogressStatus < 100){
//                    mprogressStatus++;
//                    android.os.SystemClock.sleep(100);
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mprogressBar.setProgress(mprogressStatus);
//                        }
//                    });
//                }
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mloadingText.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        }) .start();
//        buttonLogin.setOnClickListener(MainActivity.this);
//            buttonRegister.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            registerUser();
//        }
//    });


//        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, HomepageActivity.class);
//                startActivity(i);
//            }
//        });

        private void registerUser(){
            final String name = editTextName.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();


            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this, "Please enter the Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(phone)){
                Toast.makeText(this, "Please enter Phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.getTrimmedLength(phone) != 10){
                Toast.makeText(this, "Please valid Phone number", Toast.LENGTH_SHORT).show();
                return;
            }


            firebaseAuth.createUserWithEmailAndPassword(name,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                System.out.println(phone);

                                //insert some default data
                                User user = new User();
                                user.setName(name.substring(0, name.indexOf("@")));
                                user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                user.setPhone(phone);


//                                User user = new User(
//                                        phone
//                                );

//                                DatabaseReference xy = FirebaseDatabase.getInstance().getReference("newfolder").push();
//                                if(xy == null){
//                                    System.out.println("firebase ref is null");
//                                }
                                String op = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                System.out.println(op);


//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference myRef = database.getReference("message");
//                                System.out.println(myRef.getParent());
//                                myRef.setValue("Hello, World!");
//                                System.out.println("stop");
                                FirebaseDatabase.getInstance().getReference("Users")
                                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this, "Phone number added", Toast.LENGTH_LONG).show();
                                        } else{
                                            Toast.makeText(MainActivity.this, "Failed Phone number addition", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

//
//                                Intent i = new Intent(MainActivity.this, HomepageActivity.class);
//                                startActivity(i);
//                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MainActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(View = buttonLogin){
//                    //do something else to login;
//                }
//            }
//        });

//    private void loginUser(){
//        String name = editTextName.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();
//
//        if(TextUtils.isEmpty(name)){
//            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this, "Please enter the Password", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(name = firebaseAuth.getCurrentUser())

    private void loginUser() {
        String name = editTextName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter the Password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(name, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent i = new Intent(MainActivity.this, HomepageActivity.class);
                            startActivity(i);
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
        public void onClick(View v)
    {
           switch (v.getId()) {
               case R.id.buttonLogin:
                   loginUser();
//                   Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();
                   break;
               case R.id.buttonRegister:
        registerUser();
        break;
           }
        }
//  @Override
//    public void onClick(View v) {
//      loginUser();
//  }
//  }
}


