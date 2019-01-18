package com.om.developers.scan;


import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.ArrayAdapter;
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
        textView = findViewById(R.id.textView);
        listView = findViewById(R.id.lists);


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
                        mydata = (String) document.getData().get("number");
                        String valid=(String)document.getData().get("status");
                        Log.d("", mydata);
                        arrayList.add(mydata);
                        Log.d("", document.getId() + "==>" + document.getData().get("name"));

                        if(dec.contains(mydata)){
                            if(valid.equals("Present")){
                                Toast.makeText(getApplicationContext(),"Already Present",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Valid College ID", Toast.LENGTH_SHORT).show();
                                db.collection("students").document(document.getId()).update("status","Present");
                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"QR Not Valid",Toast.LENGTH_SHORT).show();
                        }
//
//                        if ( arrayList.contains(dec) )
//                            {
//                                if(valid.equals("Present")){
//                                    Toast.makeText(getApplicationContext(),"Already Present",Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    Toast.makeText(getApplicationContext(), "Valid College ID", Toast.LENGTH_SHORT).show();
//                                    db.collection("students").document(document.getId()).update("status","Present");
//                                }
//                            }

                    ArrayAdapter <String> arrayAdapter = new ArrayAdapter <String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData().get("name"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



//
//        db.collection("students")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
//
//                        if ( task.isSuccessful() )
//                            {
//                                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
//                                    {
//                                        mydata = (String) documentSnapshot.getData().get("number");
//                                        String valid=(String)documentSnapshot.getData().get("status");
//                                        arrayList.add(mydata);
//                                        Log.d("", documentSnapshot.getId() + "==>" + documentSnapshot.getData().get("name"));
//                                         if ( arrayList.contains(dec) )
//                                            {
//                                                if(valid.equals("Present")){
//                                                    Toast.makeText(getApplicationContext(),"Already Present",Toast.LENGTH_SHORT).show();
//                                                }
//                                                else {
//                                                    Toast.makeText(getApplicationContext(), "Valid College ID", Toast.LENGTH_SHORT).show();
//                                                    db.collection("students").document(documentSnapshot.getId()).update("status","Present");
//                                                }
//                                            }
//                                    }
//
//                                ArrayAdapter <String> arrayAdapter = new ArrayAdapter <String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
//                                arrayAdapter.notifyDataSetChanged();
//                                listView.setAdapter(arrayAdapter);
//                            } else
//                            {
//                                Log.d("", "ERROR", task.getException());
//                            }
//                    }
//                });
    }

    public void onBackPressed(){
        startActivity(new Intent(ICEM.this,MainActivity.class));
    }

    }

