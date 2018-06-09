package com.example.amador.tucanchape.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.CanchaReservaAdapter;
import com.example.amador.tucanchape.fragment.CanchaReservaFragment;
import com.example.amador.tucanchape.model.Cancha;
import com.example.amador.tucanchape.model.Horario;
import com.example.amador.tucanchape.model.Reserva;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CanchaReserva extends AppCompatActivity implements CanchaReservaAdapter.DeleteCancha{

    private List<Cancha> items;
    private RecyclerView rvCanchas;
    private CanchaReservaAdapter adapter;
    private RelativeLayout lyEmpty;
    private DatabaseReference canchasNodo;
    private FirebaseUser user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancha_reserva);

        items = new ArrayList<>();
        rvCanchas = findViewById(R.id.rv_canchas);
        lyEmpty = findViewById(R.id.ly_empty);

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

                                    List<Reserva> reservas = new ArrayList<>();
                                    for (DataSnapshot postReserva : postHorarios.child("reservas").getChildren()) { //horarios de chancgas
                                        Reserva reserva = new Reserva();
                                        reserva.setFecha(postReserva.child("fecha").getValue(String.class));
                                        reserva.setReservado(postReserva.child("reservado").getValue(String.class));
                                        reservas.add(reserva);
                                    }
                                    horario.setReservas(reservas);
                                }
                                cancha.setHorarios(horarios);
                                items.add(cancha);
                            }
                        }
                    }
                    adapter = new CanchaReservaAdapter(context,items);
                    adapter.setInteface(CanchaReserva.this);
                    rvCanchas.setAdapter(adapter);
                    rvCanchas.setLayoutManager(new LinearLayoutManager(context));
                    evaluateList();
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }

    private void evaluateList() {
        if(items.isEmpty())
            lyEmpty.setVisibility(View.VISIBLE);
        else
            lyEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onCanchaPresed(int posi) {

    }
}
