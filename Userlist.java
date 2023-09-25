package com.example.imobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Userlist extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<User> list;
    MyAdapter adapter;
    FirebaseFirestore db;
    Button sair , trocar;
    TextView text_email;
    TextView mList;
    String userid = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Userlist.this, Login.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        IniciarComponentes();

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        trocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Userlist.this, Trocar.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });



        recyclerView = findViewById(R.id.recycleview);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<User>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(Userlist.this, list);
        recyclerView.setAdapter(adapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        Bundle extras = getIntent().getExtras();

        String useridx = null;
        if (extras != null) {
            userid = extras.getString("userid");
        }
        TextView texto = (TextView)findViewById(R.id.text_email);
        texto.setText(userid);



        useridx =  "x" + userid;
        db.collection(useridx)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestone err", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                               list.add(dc.getDocument().toObject(User.class));

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


    }

private void IniciarComponentes(){
        sair = findViewById(R.id.sair);
        trocar = findViewById(R.id.trocar);
    }

}