package com.example.amador.tucanchape.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.CanchaReservaAdapter;
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
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CanchaReservaFragment extends Fragment implements CanchaReservaAdapter.DeleteCancha, HorariosReservaFragment.HorarioReservaFragmentListener{

    private List<Cancha> items;
    private RecyclerView rvCanchas;
    private CanchaReservaAdapter adapter;
    private RelativeLayout lyEmpty;
    private DatabaseReference canchasNodo;
    private String usuarioId;
    private int fragment;

    public CanchaReservaFragment() {
        // Required empty public constructor
    }

    public static CanchaReservaFragment newInstance(String usuarioId, int fragmentNew) {

        CanchaReservaFragment fragment = new CanchaReservaFragment();

        Bundle args = new Bundle();
        args.putString("userId", usuarioId);
        args.putInt("fragment", fragmentNew);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cancha_reserva, container, false);

        if (getArguments() != null) {
            usuarioId = getArguments().getString("userId");
            fragment = getArguments().getInt("fragment");
        }

        items = new ArrayList<>();
        rvCanchas = view.findViewById(R.id.rv_canchas);
        lyEmpty = view.findViewById(R.id.ly_empty);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.child("cancha").getChildren()) { //cancha
                    if (postSnapshot.child("idusuario").getValue(String.class).equals(usuarioId)) { //si son mis canchas
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
                adapter = new CanchaReservaAdapter(getContext(),items);
                adapter.setInteface(CanchaReservaFragment.this);
                rvCanchas.setAdapter(adapter);
                rvCanchas.setLayoutManager(new LinearLayoutManager(getContext()));
                evaluateList();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Estamos presentando de conexión, le pedimos que lo intente de nuevo", Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }


    @Override
    public void onCanchaPresed(final int posi){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        android.support.v4.app.FragmentManager manager = getFragmentManager();
                        HorariosReservaFragment horariosReservaFragment = HorariosReservaFragment.newInstance(items.get(posi).getHorarios(), posi, dayOfMonth + "/" + (monthOfYear + 1) + "/" + year, fragment == R.id.escenario);
                        horariosReservaFragment.setListener(CanchaReservaFragment.this);
                        manager.beginTransaction().add(fragment, horariosReservaFragment).commit();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
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


