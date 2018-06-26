package com.example.amador.tucanchape.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Horario;
import com.example.amador.tucanchape.model.Reserva;

import java.util.List;

public class HorarioReservaAdapter extends RecyclerView.Adapter<HorarioReservaAdapter.ViewHolder> {

    private List<Horario> items;
    private Context ctx;
    private String fecha;
    private static HorarioReservadoAdapterListener listener;
    private boolean visible;

    public interface HorarioReservadoAdapterListener{
        void onChangeReserva(int posi, boolean val);
    }

    public void setListener(HorarioReservadoAdapterListener listener) {
        this.listener = listener;
    }

    public HorarioReservaAdapter(Context ctx, final List<Horario> items, String fecha, boolean visible) {
        this.items = items;
        this.ctx = ctx;
        this.fecha =fecha;
        this.visible =visible;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView horario_name;
        TextView usuario_name;
        TextView siglas_name;
        ImageView icon_status;
        CheckBox sw_status;

        public ViewHolder(final View itemView) {
            super(itemView);
            horario_name = itemView.findViewById(R.id.horario_name);
            usuario_name = itemView.findViewById(R.id.usuario_name);
            siglas_name = itemView.findViewById(R.id.siglas_name);
            icon_status = itemView.findViewById(R.id.icon_status);
            sw_status = itemView.findViewById(R.id.sw_status);

            sw_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChangeReserva(getAdapterPosition(),sw_status.isChecked());
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_horario_reserva, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Horario item = items.get(position);

        holder.horario_name.setText(item.getDesde().toString() + " - " + item.getHasta().toString());
        holder.icon_status.setBackground(ctx.getDrawable(R.drawable.shape_enabled));
        holder.sw_status.setChecked(false);
        holder.sw_status.setVisibility(visible?View.VISIBLE:View.GONE);
        holder.usuario_name.setVisibility(View.GONE);
        holder.siglas_name.setVisibility(View.GONE);

        if(item.getReservas()!=null) {
            for (Reserva reserva : item.getReservas()) {
                if (reserva.getFecha().equals(fecha)) {
                    holder.icon_status.setBackground(ctx.getDrawable(R.drawable.shape_disabled));
                    holder.sw_status.setChecked(true);
                    holder.usuario_name.setText(reserva.getReservado());
                    holder.siglas_name.setText(reserva.getSiglas());
                    holder.siglas_name.setVisibility(View.GONE);
                    holder.usuario_name.setVisibility(visible? View.VISIBLE:View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
