package com.om.developers.scan;


import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

import java.util.List;


public class ICEM extends AppCompatActivity {

    private static final String TAG = "";
    TextView tvs,textView;
    String mydata;
    ImageView backs;
    ListView listView;
    private List<String> arrayList=new ArrayList <>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String dec = "";
    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icem);
        db.setFirestoreSettings(settings);

        tvs = findViewById(R.id.data);
        textView = findViewById(R.id.name);
        listView = findViewById(R.id.lists);
        backs=findViewById(R.id.back);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final String clg_id = getIntent().getStringExtra("datas");
        int num=0;

        for(int i=0;i<clg_id.length();i++){
            num = (int)clg_id.charAt(i) - 97 - i;

            dec += Integer.toString(num);
        }


        tvs.setText(dec);

        DocumentReference docRef = db.collection("students").document(dec);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       mydata = (String) document.getData().get("name");
                        textView.setText("Welcome "+mydata);
                        String valid=(String)document.getData().get("status");
                        arrayList.add(mydata);
                            if(valid.equals("Present")){
                                Toast.makeText(getApplicationContext(),"Already Present",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Valid College ID", Toast.LENGTH_SHORT).show();
                                db.collection("students").document(document.getId()).update("status","Present");
                            }



                    ArrayAdapter <String> arrayAdapter = new ArrayAdapter <String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);

                    } else {
                       Toast.makeText(getApplicationContext(),"INVALID QR CODE",Toast.LENGTH_SHORT).show();
                    }
                } else {
                  Toast.makeText(getApplicationContext(),"error in data",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    public void onBackPressed(){
        startActivity(new Intent(ICEM.this,MainActivity.class));
    }

    }

