package com.hlal.m7moud.mysolutiontask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hlal.m7moud.mysolutiontask.Data.User;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    Button btn_signin , btn_register ;
    ConstraintLayout main_layout ;

    FirebaseDatabase nDatabase ;
    FirebaseAuth nFirebaseAuth ;
    DatabaseReference dbref ;

    ProgressDialog progressDialog ;


    private ValueEventListener nDBlistener ;
    List<User> mUploads  ;
    String sUserName = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        nDatabase = FirebaseDatabase.getInstance() ;
        nFirebaseAuth =  FirebaseAuth.getInstance() ;
        dbref = nDatabase.getReference("users") ;
        intiview();
        mUploads = new ArrayList<>() ;

        nDBlistener =  dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //mUploads.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    User upload = dataSnapshot1.getValue(User.class) ;
                    //upload.setUsername(dataSnapshot1.);
                    mUploads.add(upload);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(StartActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void intiview() {

        btn_signin = (Button)findViewById(R.id.btn_signin) ;
        btn_register = (Button)findViewById(R.id.btn_register) ;
        main_layout = (ConstraintLayout)findViewById(R.id.main_layout)  ;

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnRegisterClick();
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                OnSigninClick();


            }
        });
    }

    private  void showprogress(String s){
        progressDialog = ProgressDialog.show(this , "" , s ,true)  ;
        progressDialog.setCancelable(false);
    }

    private void OnRegisterClick() {
        AlertDialog.Builder dialog =new  AlertDialog.Builder(this);
        dialog.setTitle("Register");
        dialog.setMessage("Enter your correct data");
        LayoutInflater inflater = LayoutInflater.from(this) ;
        View Layout_Register = inflater.inflate(R.layout.view_register , null) ;
        final EditText edt_email  = Layout_Register.findViewById(R.id.edt_email);
        final EditText edt_password  = Layout_Register.findViewById(R.id.edt_password);
        final EditText edt_phone  = Layout_Register.findViewById(R.id.edt_phone);
        final EditText edt_username  = Layout_Register.findViewById(R.id.edt_username);

        dialog.setView(Layout_Register) ;
        dialog.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

                if (TextUtils.isEmpty(edt_email.getText().toString())){
                    Snackbar.make(main_layout , "Enter Your Email" , Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_password.getText().toString())){
                    Snackbar.make(main_layout , "Enter Your Password" , Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_username.getText().toString())){
                    Snackbar.make(main_layout , "Enter Your UserName" , Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_phone.getText().toString())){
                    Snackbar.make(main_layout , "Enter Your Phone" , Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (edt_password.getText().length() < 6){
                    Snackbar.make(main_layout , "Your Password Is Too Short" , Snackbar.LENGTH_SHORT).show();
                    return;
                }


                showprogress("Please Waitting...");
                nFirebaseAuth.createUserWithEmailAndPassword(edt_email.getText().toString() , edt_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        final User driver = new User() ;
                        driver.setEmail(edt_email.getText().toString());
                        driver.setUsername(edt_username.getText().toString());
                        driver.setPhone(edt_phone.getText().toString());
                        driver.setPassword(edt_password.getText().toString());
                        dbref.child(edt_username.getText().toString()).setValue(driver).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                progressDialog.dismiss();


                                //txt_UserName.setText(driver.getUsername().toString());
                                Intent intent = new Intent(StartActivity.this , MainActivity.class) ;
                                intent.putExtra("username" , driver.getUsername().toString());
                                startActivity(intent);
                                finish();
                                Snackbar.make(main_layout , "Success Registeration" , Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Snackbar.make(main_layout , e.getMessage() , Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Snackbar.make(main_layout , e.getMessage() , Snackbar.LENGTH_SHORT).show();
                    }
                });

            }

        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show() ;
    }

    private void OnSigninClick() {


        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Enter your correct data");
        LayoutInflater inflater = LayoutInflater.from(this);
        View Layout_Signin = inflater.inflate(R.layout.view_login, null);
        final EditText edt_email =  Layout_Signin.findViewById(R.id.edt_email_signin);
        final EditText edt_password =  Layout_Signin.findViewById(R.id.edt_password_signin);

        dialog.setView(Layout_Signin);



        dialog.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                dialogInterface.dismiss();
                //get user name
                for(int i = 0 ; i< mUploads.size() ; i++){
                    if (edt_email.getText().toString().equals(mUploads.get(i).getEmail())){
                        sUserName = mUploads.get(i).getUsername() ;
                    }
                }

                if (TextUtils.isEmpty(edt_email.getText().toString())) {
                    Snackbar.make(main_layout, "Enter Your Email", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(edt_password.getText().toString())) {
                    Snackbar.make(main_layout, "Enter Your Password", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (edt_password.getText().length() < 6) {
                    Snackbar.make(main_layout, "Your Password Is Too Short", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                showprogress("Please Waitting...");

                nFirebaseAuth.signInWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {



                        progressDialog.dismiss();

                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("username1" , sUserName );
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Snackbar.make(main_layout, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }
}
