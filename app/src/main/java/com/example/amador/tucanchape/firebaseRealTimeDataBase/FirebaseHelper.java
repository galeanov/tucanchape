package com.example.amador.tucanchape.firebaseRealTimeDataBase;

import com.example.amador.tucanchape.model.Cancha;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FirebaseHelper {

    private DatabaseReference db;
    private Boolean saved;
    private ArrayList<Cancha> canchas=new ArrayList<>();

    /*
        PASS DATABASE REFRENCE
    */
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE IF NOT NULL
    public Boolean save(Cancha cancha)
    {
        if(cancha ==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("cancha").push().setValue(cancha);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        canchas.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Cancha cancha =ds.getValue(Cancha.class);
            this.canchas.add(cancha);
        }
    }

    //RETRIEVE
    public ArrayList<Cancha> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return canchas;
    }

}
