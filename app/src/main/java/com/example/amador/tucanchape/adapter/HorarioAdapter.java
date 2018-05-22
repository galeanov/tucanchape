package com.example.amador.tucanchape.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Horario;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {

    private List<Horario> items;
    private Context ctx;
    private static  HorarioListener inteface;

    public interface HorarioListener{
        void onDelteHorario(int posi);
    }

    public void setInteface(HorarioListener inteface) {
        this.inteface = inteface;
    }

    public HorarioAdapter(Context ctx, List<Horario> items) {
        this.items = items;
        this.ctx = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView horario_name;
        ImageView delete;

        public ViewHolder(final View itemView) {
            super(itemView);
            horario_name = itemView.findViewById(R.id.horario_name);
            delete = itemView.findViewById(R.id.icon_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inteface.onDelteHorario(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_horario, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Horario item = items.get(position);

        holder.horario_name.setText(item.getDesde().toString() + " - " + item.getHasta().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
