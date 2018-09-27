package com.hlal.m7moud.mysolutiontask.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hlal.m7moud.mysolutiontask.R;
import com.hlal.m7moud.mysolutiontask.Data.DataItem;
import com.hlal.m7moud.mysolutiontask.MainActivity;
import com.hlal.m7moud.mysolutiontask.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    View view;

    EditText   Edt_Desc ;
    AutoCompleteTextView Edt_Type ;
    private TextInputLayout inputLayoutType , inputLayoutDesc ;
    CircleImageView Btn_Photo ;
    Button Btn_Send  ;
    private ProgressBar mProgress ;

    Uri ImgUri ;

    private static final int PICK_IMAGE_REQUEST = 1;

    FirebaseDatabase mFirebaseDatabase ;
    DatabaseReference mDataRef ;
    FirebaseStorage mFirebaseStorage ;
    StorageReference mStorageReference  ;
    StorageTask mStorageTask  ;

    String[] languages = { "استشاره 1","استشاره 2","استشاره 3","استشاره 4","استشاره 5","استشاره 6","استشاره 7","استشاره 8","استشاره 9" };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home , container , false) ;
        initComponent();
        MakeOnClick();
        return view ;
    }


    private void initComponent() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDataRef = mFirebaseDatabase.getReference().child("order");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference().child("order");

        Edt_Type = view.findViewById(R.id.Type);
        Edt_Desc = view.findViewById(R.id.Desc);
        inputLayoutType = view.findViewById(R.id.input_layout_type);
        inputLayoutDesc = view.findViewById(R.id.input_layout_Desc);
        Btn_Send = view.findViewById(R.id.send);
        Btn_Photo = view.findViewById(R.id.photo);

        mProgress = view.findViewById(R.id.progress);
        mProgress.setVisibility(View.INVISIBLE);


        //Create Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.select_dialog_singlechoice , languages);
        //Find TextView control
         Edt_Type = view.findViewById(R.id.Type);
        //Set the number of characters the user must type before the drop down list is shown
        Edt_Type.setThreshold(1);
        //Set the adapter
        Edt_Type.setAdapter(adapter);
    }

    // on buttons click
    private void MakeOnClick() {
        Btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStorageTask != null && mStorageTask.isInProgress()){
                    Toast.makeText(getActivity(), "جارى رفع البيانات....", Toast.LENGTH_SHORT).show();

                }else {
                    validate();
                }
            }
        });
        Btn_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }



        @Override
        public void onActivityResult (int requestCode, int resultCode, Intent data){

            super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                        data != null && data.getData() != null ){

                    ImgUri = data.getData();

                    Picasso.with(getActivity()).load(ImgUri).into(Btn_Photo);
                    Btn_Photo.setImageURI(ImgUri);
                }

        }



   //open gallary
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*") ;
        intent.setAction(Intent.ACTION_GET_CONTENT) ;
        startActivityForResult(intent , PICK_IMAGE_REQUEST);
    }

    //get file exctention
    private String getFileExctention(Uri uri){
        ContentResolver cr =  getActivity().getContentResolver() ;
        MimeTypeMap mime = MimeTypeMap.getSingleton() ;
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //validate and upload data
    private void validate(){

        final  String Type = Edt_Type.getText().toString().trim() ;
        final  String Desc = Edt_Desc.getText().toString().trim() ;


        if (TextUtils.isEmpty(Type)){
            inputLayoutType.setError(getResources().getString(R.string.required));
            Edt_Type.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Desc)){
            inputLayoutDesc.setError(getResources().getString(R.string.required));
            Edt_Desc.requestFocus();
            return;
        }


        if (ImgUri != null){
            mProgress.setVisibility(View.VISIBLE);
            StorageReference fileRef = mStorageReference.child(System.currentTimeMillis() + "."
                    + getFileExctention(ImgUri)) ;
            mStorageTask = fileRef.putFile(ImgUri).addOnSuccessListener( getActivity() , new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {




                    Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_LONG).show();
                    DataItem mDataItem = new DataItem(Type , Desc ,"hhh","good"
                                                      ,taskSnapshot.getStorage().getDownloadUrl().toString());
                    List<DataItem> list = new ArrayList<>();
                    list.add(mDataItem);
                    String uploadId = mDataRef.push().getKey() ;
                    mDataRef.child(uploadId).setValue(mDataItem) ;
                    Edt_Type.setText("");
                    Edt_Desc.setText("");
                    mProgress.setVisibility(View.GONE);
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    mProgress.setVisibility(View.GONE);

                }
            });

        }else {
            Toast.makeText(getActivity(), "no file selection", Toast.LENGTH_SHORT).show();
            mProgress.setVisibility(View.GONE);

        }



    }







}
