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
import com.example.amador.tucanchape.activity.CanchaReservaActivity;
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
                final EditText inputPrecioD = new EditText(getContext());
                final EditText inputPrecioN = new EditText(getContext());
                final EditText inputTipo = new EditText(getContext());

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                input.setLayoutParams(lp);
                input.setHint("Nombre: Cancha 1");
                inputPrecioD.setLayoutParams(lp);
                inputPrecioD.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputPrecioD.setHint("Precio en el Día");
                inputPrecioN.setLayoutParams(lp);
                inputPrecioN.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputPrecioN.setHint("Precio en la Noche");
                inputTipo.setLayoutParams(lp);
                inputTipo.setHint("Tipo de cancha");

                layout.addView(input);
                layout.addView(inputPrecioD);
                layout.addView(inputPrecioN);
                layout.addView(inputTipo);

                new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setTitle("Nueva Cancha")
                        .setMessage("Ingrese la información de la cancha")
                        .setNegativeButton("Cancelar", null)// sin listener
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!input.getText().toString().equals("") && !inputPrecioD.getText().toString().equals("") && !inputPrecioN.getText().toString().equals("") && !inputTipo.getText().toString().equals("")){
                                    Cancha cancha = new Cancha();
                                    cancha.setName(input.getText().toString());
                                    cancha.setPrecioD(Double.valueOf(inputPrecioD.getText().toString()));
                                    cancha.setPrecioN(Double.valueOf(inputPrecioN.getText().toString()));
                                    cancha.setTipo(inputTipo.getText().toString());
                                    List<Horario> horarios = new ArrayList<>();
                                    cargarHorariosDefault(horarios);
                                    cancha.setHorarios(horarios);
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
                                cancha.setPrecioD(postCanchas.child("precioD").getValue(Double.class));
                                cancha.setPrecioN(postCanchas.child("precioN").getValue(Double.class));

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
                    Toast.makeText(getContext(), "Estamos presentando de conexión, le pedimos que lo intente de nuevo", Toast.LENGTH_LONG).show();

                }
            });
        }

        return view;
    }

    private void cargarHorariosDefault(final List<Horario> horarios) {
        Horario horario1 = new Horario();
        Horario horario2 = new Horario();
        Horario horario3 = new Horario();
        Horario horario4 = new Horario();
        Horario horario5 = new Horario();
        Horario horario6 = new Horario();
        Horario horario7 = new Horario();
        Horario horario8 = new Horario();
        Horario horario9 = new Horario();
        Horario horario10 = new Horario();
        Horario horario11 = new Horario();
        Horario horario12 = new Horario();
        Horario horario13 = new Horario();
        Horario horario14 = new Horario();
        Horario horario15 = new Horario();
        Horario horario16 = new Horario();
        Horario horario17 = new Horario();
        Horario horario18 = new Horario();
        Horario horario19 = new Horario();
        Horario horario20 = new Horario();
        Horario horario21 = new Horario();




        horario1.setDesde("5:00 a.m");
        horario1.setHasta("6:00 a.m");

        horario2.setDesde("6:00 a.m");
        horario2.setHasta("7:00 a.m");

        horario3.setDesde("7:00 a.m");
        horario3.setHasta("8:00 a.m");

        horario4.setDesde("8:00 a.m");
        horario4.setHasta("9:00 a.m");

        horario5.setDesde("9:00 a.m");
        horario5.setHasta("10:00 a.m");

        horario6.setDesde("10:00 a.m");
        horario6.setHasta("11:00 a.m");

        horario7.setDesde("11:00 a.m");
        horario7.setHasta("12:00 p.m");

        horario8.setDesde("12:00 p.m");
        horario8.setHasta("1:00 p.m");

        horario9.setDesde("1:00 p.m");
        horario9.setHasta("2:00 p.m.");

        horario10.setDesde("2:00 p.m");
        horario10.setHasta("3:00 p.m");

        horario11.setDesde("3:00 p.m");
        horario11.setHasta("4:00 p.m");

        horario12.setDesde("4:00 p.m");
        horario12.setHasta("5:00 p.m");

        horario13.setDesde("5:00 p.m");
        horario13.setHasta("6:00 p.m");

        horario14.setDesde("6:00 p.m");
        horario14.setHasta("7:00 p.m");

        horario15.setDesde("7:00 p.m");
        horario15.setHasta("8:00 p.m");

        horario16.setDesde("8:00 p.m");
        horario16.setHasta("9:00 p.m");

        horario17.setDesde("9:00 p.m");
        horario17.setHasta("10:00 a.m");

        horario18.setDesde("10:00 p.m");
        horario18.setHasta("11:00 p.m");

        horario19.setDesde("11:00 p.m");
        horario19.setHasta("12:00 a.m");

        horario19.setDesde("12:00 a.m");
        horario19.setHasta("1:00 a.m");

        horario20.setDesde("1:00 a.m");
        horario20.setHasta("01:00 a.m");

        horario21.setDesde("02:00 a.m");
        horario21.setHasta("03:00 a.m");


        horarios.add(horario1);
        horarios.add(horario2);
        horarios.add(horario3);
        horarios.add(horario4);
        horarios.add(horario5);
        horarios.add(horario6);
        horarios.add(horario7);
        horarios.add(horario8);
        horarios.add(horario9);
        horarios.add(horario10);
        horarios.add(horario11);
        horarios.add(horario12);
        horarios.add(horario13);
        horarios.add(horario14);
        horarios.add(horario15);
        horarios.add(horario16);
        horarios.add(horario17);
        horarios.add(horario18);
        horarios.add(horario19);
        horarios.add(horario20);
        horarios.add(horario21);
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


