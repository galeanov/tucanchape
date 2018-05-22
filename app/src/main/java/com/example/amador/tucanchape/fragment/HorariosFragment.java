package com.example.amador.tucanchape.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.CanchaAdapter;
import com.example.amador.tucanchape.adapter.HorarioAdapter;
import com.example.amador.tucanchape.model.Cancha;
import com.example.amador.tucanchape.model.Horario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HorariosFragment extends Fragment implements HorarioAdapter.HorarioListener{

    private List<Horario> items;
    private RecyclerView rvHorarios;
    private HorarioAdapter adapter;
    private RelativeLayout lyEmpty;
    private int posi;
    private HorarioFragmentListener listener;

    public interface HorarioFragmentListener{
        void onChangeHorario(final List<Horario> horarios, final int posi);
    }

    public void setListener(HorarioFragmentListener listener) {
        this.listener = listener;
    }

    public HorariosFragment() {
        // Required empty public constructor
    }

    public static HorariosFragment newInstance(final List<Horario> items, final int posi) {

        HorariosFragment fragment = new HorariosFragment();

        Bundle args = new Bundle();
        args.putSerializable("horarios", (Serializable) items);
        args.putInt("posi", posi);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_horario, container, false);

        if (getArguments() != null) {
            items = (List<Horario>) getArguments().getSerializable("horarios");
            posi = getArguments().getInt("posi");
        }
        adapter = new HorarioAdapter(getContext(),items);
        adapter.setInteface(this);
        rvHorarios = view.findViewById(R.id.rv_horarios);
        rvHorarios.setAdapter(adapter);
        rvHorarios.setLayoutManager(new LinearLayoutManager(getContext()));
        lyEmpty = view.findViewById(R.id.ly_empty);
        evaluateList();


        FloatingActionButton fab = view.findViewById(R.id.add_hor);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputDeste = new EditText(getContext());
                final EditText inputHasta = new EditText(getContext());
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                inputDeste.setLayoutParams(lp);
                inputDeste.setHint("Desde");
                inputHasta.setLayoutParams(lp);
                inputHasta.setHint("Hasta");
                layout.addView(inputDeste);
                layout.addView(inputHasta);

                new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setTitle("Nuevo Horario")
                        .setMessage("Ingrese los parametros del horario")
                        .setNegativeButton("Cancelar", null)// sin listener
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!inputDeste.getText().toString().equals("") && !inputHasta.getText().toString().equals("")){
                                    Horario horario = new Horario();
                                    horario.setDesde(inputDeste.getText().toString());
                                    horario.setHasta(inputHasta.getText().toString());
                                    items.add(horario);
                                    adapter.notifyDataSetChanged();
                                    evaluateList();
                                    listener.onChangeHorario(items, HorariosFragment.this.posi);
                                }else {
                                    Toast.makeText(getContext(),"No se permiten parametros vacios", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).show();
            }
        });

        return view;
    }

    public void evaluateList(){
        if(items.isEmpty())
            lyEmpty.setVisibility(View.VISIBLE);
        else
            lyEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onDelteHorario(final int posi) {
        new AlertDialog.Builder(getContext())
                .setTitle("Advertencia")
                .setMessage("¿Estás seguro de eliminar el horario?")
                .setNegativeButton("Cancelar", null)// sin listener
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(posi);
                        adapter.notifyDataSetChanged();
                        evaluateList();
                        listener.onChangeHorario(items, HorariosFragment.this.posi);
                    }
                }).show();
    }
}


