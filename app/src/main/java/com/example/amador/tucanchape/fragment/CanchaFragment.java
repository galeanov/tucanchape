package com.example.amador.tucanchape.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.CanchaAdapter;
import com.example.amador.tucanchape.model.Cancha;
import com.example.amador.tucanchape.model.Horario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CanchaFragment extends Fragment implements CanchaAdapter.DeleteCancha, HorariosFragment.HorarioFragmentListener{

    private List<Cancha> items;
    private RecyclerView rvCanchas;
    private CanchaAdapter adapter;
    private RelativeLayout lyEmpty;
    private DatabaseReference canchasNodo;
    private FirebaseUser user;
    private ProgressBar pb;

    public CanchaFragment() {
        // Required empty public constructor
    }

    public static CanchaFragment newInstance(/*String param1*/) {

        CanchaFragment fragment = new CanchaFragment();

        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);*/
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cancha, container, false);

        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);*/
        }

        items = new ArrayList<>();
        rvCanchas = view.findViewById(R.id.rv_canchas);
        lyEmpty = view.findViewById(R.id.ly_empty);
        pb = view.findViewById(R.id.pb);


        final FloatingActionButton fab = view.findViewById(R.id.add_can);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(getContext());
                final EditText inputPrecio = new EditText(getContext());
                final EditText inputTipo = new EditText(getContext());

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                input.setLayoutParams(lp);
                input.setHint("Nombre");
                inputPrecio.setLayoutParams(lp);
                inputPrecio.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputPrecio.setHint("Precio");
                inputTipo.setLayoutParams(lp);
                inputTipo.setHint("Tipo");

                layout.addView(input);
                layout.addView(inputPrecio);
                layout.addView(inputTipo);

                new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setTitle("Nueva Cancha")
                        .setMessage("Ingrese la información de la cancha")
                        .setNegativeButton("Cancelar", null)// sin listener
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!input.getText().toString().equals("") && !inputPrecio.getText().toString().equals("") && !inputTipo.getText().toString().equals("")){
                                    Cancha cancha = new Cancha();
                                    cancha.setName(input.getText().toString());
                                    cancha.setPrecio(Double.valueOf(inputPrecio.getText().toString()));
                                    cancha.setTipo(inputTipo.getText().toString());
                                    items.add(cancha);
                                    if(canchasNodo!=null)
                                        canchasNodo.child("canchas").setValue(items);
                                    else {
                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                                        Map<String, Object> newUserCancha = new HashMap<>();
                                        newUserCancha.put("idusuario", user.getUid());
                                        newUserCancha.put("canchas", items);
                                        myRef.child("cancha").push().setValue(newUserCancha);
                                    }
                                }else {
                                    Toast.makeText(getContext(),"No se permiten nombres vacios", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).show();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    items.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.child("cancha").getChildren()) { //cancha
                        if (postSnapshot.child("idusuario").getValue(String.class).equals(user.getUid())) { //si son mis canchas
                            canchasNodo = postSnapshot.getRef();
                            for (DataSnapshot postCanchas : postSnapshot.child("canchas").getChildren()) { //canchas
                                Cancha cancha = new Cancha();
                                cancha.setName(postCanchas.child("name").getValue(String.class));
                                cancha.setTipo(postCanchas.child("tipo").getValue(String.class));
                                cancha.setPrecio(postCanchas.child("precio").getValue(Double.class));

                                List<Horario> horarios = new ArrayList<>();
                                for (DataSnapshot postHorarios : postCanchas.child("horarios").getChildren()) { //horarios de chancgas
                                    Horario horario = new Horario();
                                    horario.setDesde(postHorarios.child("desde").getValue(String.class));
                                    horario.setHasta(postHorarios.child("hasta").getValue(String.class));
                                    horarios.add(horario);
                                }
                                cancha.setHorarios(horarios);
                                items.add(cancha);
                            }
                        }
                    }
                    adapter = new CanchaAdapter(getContext(),items);
                    adapter.setInteface(CanchaFragment.this);
                    rvCanchas.setAdapter(adapter);
                    rvCanchas.setLayoutManager(new LinearLayoutManager(getContext()));
                    evaluateList();
                    pb.setVisibility(View.GONE);
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        return view;
    }


    @Override
    public void onCanchaPresed(final int posi) {
        android.support.v4.app.FragmentManager manager = getFragmentManager();
        HorariosFragment horariosFragment = HorariosFragment.newInstance(items.get(posi).getHorarios(), posi);
        horariosFragment.setListener(this);
        manager.beginTransaction().add(R.id.escenario, horariosFragment).commit();
    }

    @Override
    public void onDelteCancha(final int posi) {
        new AlertDialog.Builder(getContext())
                .setTitle("Advertencia")
                .setMessage("¿Estás seguro de eliminar la cancha?")
                .setNegativeButton("Cancelar", null)// sin listener
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(posi);
                        canchasNodo.child("canchas").setValue(items);
                    }
                }).show();
    }

    public void evaluateList(){
        if(items.isEmpty())
            lyEmpty.setVisibility(View.VISIBLE);
        else
            lyEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onChangeHorario(List<Horario> horarios, int posi) {
        items.get(posi).setHorarios(horarios);
        canchasNodo.child("canchas").setValue(items);
    }
}


