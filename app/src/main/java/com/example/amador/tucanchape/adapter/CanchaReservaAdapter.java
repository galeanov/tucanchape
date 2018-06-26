package com.example.amador.tucanchape.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Cancha;

import java.util.List;

public class CanchaReservaAdapter extends RecyclerView.Adapter<CanchaReservaAdapter.ViewHolder> {

    private List<Cancha> items;
    private Context ctx;
    private static DeleteCancha inteface;

    public interface DeleteCancha{
        void onCanchaPresed(int posi);
    }

    public void setInteface(DeleteCancha inteface) {
        this.inteface = inteface;
    }

    public CanchaReservaAdapter(Context ctx, List<Cancha> items) {
        this.items = items;
        this.ctx = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView canchaName;
        TextView precioD, precioN ;
        TextView canchaTipo;

        public ViewHolder(final View itemView) {
            super(itemView);
            canchaName = itemView.findViewById(R.id.cancha_name);
            precioD = itemView.findViewById(R.id.cancha_precioD);
            precioN = itemView.findViewById(R.id.cancha_precioN);
            canchaTipo = itemView.findViewById(R.id.cancha_tipo);
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
        View itemView = inflater.inflate(R.layout.item_cancha_reserva, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cancha item = items.get(position);

        holder.canchaName.setText(item.getName());
        holder.precioD.setText("S/." + String.valueOf(item.getPrecioD()));
        holder.precioN.setText("S/." + String.valueOf(item.getPrecioD()));
        holder.canchaTipo.setText(item.getTipo());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
