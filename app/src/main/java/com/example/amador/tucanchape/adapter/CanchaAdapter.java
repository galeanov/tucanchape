package com.example.amador.tucanchape.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Cancha;

import java.util.List;

public class CanchaAdapter extends RecyclerView.Adapter<CanchaAdapter.ViewHolder> {

    private List<Cancha> items;
    private Context ctx;
    private static DeleteCancha inteface;

    public interface DeleteCancha{
        void onCanchaPresed(int posi);
        void onDelteCancha(int posi);
    }

    public void setInteface(DeleteCancha inteface) {
        this.inteface = inteface;
    }

    public CanchaAdapter(Context ctx, List<Cancha> items) {
        this.items = items;
        this.ctx = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView canchaName;
        TextView precioD;
        TextView precioN;
        TextView canchaTipo;
        ImageView delete;

        public ViewHolder(final View itemView) {
            super(itemView);
            canchaName = itemView.findViewById(R.id.cancha_name);
            precioD = itemView.findViewById(R.id.canchaD);
            precioN = itemView.findViewById(R.id.canchaN);
            canchaTipo = itemView.findViewById(R.id.cancha_tipo);
            delete = itemView.findViewById(R.id.icon_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inteface.onDelteCancha(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inteface.onCanchaPresed(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_cancha, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cancha item = items.get(position);

        holder.canchaName.setText(item.getName());
        holder.precioD.setText("S/." + String.valueOf(item.getPrecioD()));
        holder.precioN.setText("S/." + String.valueOf(item.getPrecioN()));
        holder.canchaTipo.setText(item.getTipo());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
