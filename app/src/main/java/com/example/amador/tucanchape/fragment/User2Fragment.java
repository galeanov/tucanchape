package com.example.amador.tucanchape.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.SearchAdapter;
import com.example.amador.tucanchape.model.Empresa;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User2Fragment extends Fragment {

    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> fullNameList;
    ArrayList<String> tel1;
    ArrayList<String> tel2;
    SearchAdapter searchAdapter;


    public User2Fragment() {
    }

    public static User2Fragment newInstance(/*String param1*/) {
        User2Fragment fragment = new User2Fragment();

        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);*/
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_2, container, false);

        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);*/
        }

        search_edit_text = (EditText) view.findViewById(R.id.search_edit_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        /*
         * Create a array list for each node you want to use
         * */
        fullNameList = new ArrayList<>();
        tel1 = new ArrayList<>();
        tel2 = new ArrayList<>();


        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    fullNameList.clear();
                    tel1.clear();
                    tel2.clear();
                    recyclerView.removeAllViews();
                }
            }
        });



        return view;
    }

    private void setAdapter(final String searchedString) {
        databaseReference.child("empresa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*
                 * Clear the list for every new search
                 * */
                fullNameList.clear();
                tel1.clear();
                tel2.clear();
                recyclerView.removeAllViews();

                int counter = 0;

                /*
                 * Search all users for matching searched string
                 * */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String tf1 = snapshot.child("telefono1").getValue(String.class);
                    String tf2 = snapshot.child("telefono2").getValue(String.class);

                    if (nombre.toLowerCase().contains(searchedString.toLowerCase())) {
                        fullNameList.add(nombre);
                        tel1.add(tf1);
                        tel2.add(tf2);
                        counter++;
                    }

                    /*
                     * Get maximum of 15 searched results only
                     * */
                    if (counter == 15)
                        break;
                }

                searchAdapter = new SearchAdapter(getContext(), fullNameList, tel1, tel2);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
