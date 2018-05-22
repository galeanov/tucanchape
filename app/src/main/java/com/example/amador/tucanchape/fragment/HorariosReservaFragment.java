package com.example.amador.tucanchape.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
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
import com.example.amador.tucanchape.adapter.HorarioReservaAdapter;
import com.example.amador.tucanchape.model.Horario;
import com.example.amador.tucanchape.model.Reserva;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HorariosReservaFragment extends Fragment implements HorarioReservaAdapter.HorarioReservadoAdapterListener{

    private static List<Horario> items;
    private RecyclerView rvHorarios;
    private HorarioReservaAdapter adapter;
    private RelativeLayout lyEmpty;
    private int posi;
    private String fecha;
    private HorarioReservaFragmentListener listener;


    public interface HorarioReservaFragmentListener{
        void onChangeHorario(final List<Horario> horarios, final int posi);
    }

    public void setListener(HorarioReservaFragmentListener listener) {
        this.listener = listener;
    }

    public HorariosReservaFragment() {
        // Required empty public constructor
    }

    public static HorariosReservaFragment newInstance(final List<Horario> items, final int posi, String fecha) {

        HorariosReservaFragment fragment = new HorariosReservaFragment();

        Bundle args = new Bundle();
        args.putSerializable("horarios", (Serializable) items);
        args.putInt("posi", posi);
        args.putString("fecha", fecha);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_horario_reserva, container, false);

        if (getArguments() != null) {
            items = (List<Horario>) getArguments().getSerializable("horarios");
            posi = getArguments().getInt("posi");
            fecha = getArguments().getString("fecha");
        }
        adapter = new HorarioReservaAdapter(getContext(),items, fecha);
        adapter.setListener(this);
        rvHorarios = view.findViewById(R.id.rv_horarios);
        rvHorarios.setAdapter(adapter);
        rvHorarios.setLayoutManager(new LinearLayoutManager(getContext()));
        lyEmpty = view.findViewById(R.id.ly_empty);
        evaluateList();

        return view;
    }

    public void evaluateList(){
        if(items.isEmpty())
            lyEmpty.setVisibility(View.VISIBLE);
        else
            lyEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onChangeReserva(final int posi, boolean val) {

        if(val) {
            final EditText input = new EditText(getContext());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            input.setLayoutParams(lp);

            new AlertDialog.Builder(getContext())
                    .setView(input)
                    .setTitle("Nueva Reserva")
                    .setMessage("Ingrese el nombre de quien reserva")
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                        }
                    })// sin listener
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!input.getText().toString().equals("")) {
                                if (items.get(posi).getReservas() == null)
                                    items.get(posi).setReservas(new ArrayList<Reserva>());

                                Reserva reserva = new Reserva();
                                reserva.setFecha(fecha);
                                reserva.setReservado(input.getText().toString());
                                items.get(posi).getReservas().add(reserva);

                                adapter.notifyDataSetChanged();
                                listener.onChangeHorario(items,HorariosReservaFragment.this.posi);
                            } else {
                                Toast.makeText(getContext(), "No se permiten nombres vacios", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).show();
        }else {
            for(int i = 0; i < items.get(posi).getReservas().size(); i++){
                if(items.get(posi).getReservas().get(i).getFecha().equals(fecha))
                    items.get(posi).getReservas().remove(i);
                adapter.notifyDataSetChanged();
                listener.onChangeHorario(items,HorariosReservaFragment.this.posi);
            }
        }
    }
}


