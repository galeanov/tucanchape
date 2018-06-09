package com.example.amador.tucanchape.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.firebaseRealTimeDataBase.FirebaseReferences;
import com.example.amador.tucanchape.model.Empresa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mehdi.sakout.fancybuttons.FancyButton;

public class PerfilActivity extends AppCompatActivity {

    private TextView nameprof, tel1prof, tel2prof, getID;
    private Button btnhor;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase mfirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        nameprof = (TextView)findViewById(R.id.name_prof);
        tel1prof  = (TextView)findViewById(R.id.tel_1_prof);
        tel2prof  = (TextView)findViewById(R.id.tel_2_prof);
        btnhor = (Button)findViewById(R.id.btn_hor);
        getID = (TextView)findViewById(R.id.get_id);

        firebaseAuth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference myRef = mfirebaseDatabase.getReference(FirebaseReferences.EMPRESA_REFERENCE);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle !=null){
            String claveone = bundle.getString("CLAVEONE");

            myRef.child(claveone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Empresa empresa = dataSnapshot.getValue(Empresa.class);

                    try {
                        
                        nameprof.setText(empresa.getNombre());
                        tel1prof.setText(empresa.getTelefono1());
                        tel2prof.setText(empresa.getTelefono2());

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }




    }







}
