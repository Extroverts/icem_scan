package com.om.developers.scan;

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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ICEM extends AppCompatActivity {

    TextView tvs,textView;
    String mydata;
    ListView listView;
    private List<String> arrayList=new ArrayList <>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        tvs.setText(clg_id);

        db.collection("abc")
                .get()
                .addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {

                        if ( task.isSuccessful() )
                            {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                                    {
                                        mydata = (String) documentSnapshot.getData().get("name");
                                        String valid=(String)documentSnapshot.getData().get("clg_id");
                                        arrayList.add(mydata);
                                        Log.d("", documentSnapshot.getId() + "==>" + documentSnapshot.getData().get("clg_id"));
                                         if ( arrayList.contains(clg_id) )
                                            {
                                                if(valid.equals("Present")){
                                                    Toast.makeText(getApplicationContext(),"Already Present",Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(), "Valid College ID", Toast.LENGTH_SHORT).show();
                                                    db.collection("abc").document(documentSnapshot.getId()).update("clg_id","Present");
                                                }
                                            }
                                    }

                                ArrayAdapter <String> arrayAdapter = new ArrayAdapter <String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
                                arrayAdapter.notifyDataSetChanged();
                                listView.setAdapter(arrayAdapter);
                            } else
                            {
                                Log.d("", "ERROR", task.getException());
                            }
                    }
                });
    }


    }

