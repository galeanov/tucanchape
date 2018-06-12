package com.example.amador.tucanchape.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.model.Empresa;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private Context context;
    private List<Empresa> empresas;
    private OnSearchClick listener;

    public interface OnSearchClick{
        void onSearch(int posi);
    }

    public void setListener(OnSearchClick listener) {
        this.listener = listener;
    }

    public SearchAdapter(Context context, List<Empresa> empresas) {
        this.context = context;
        this.empresas = empresas;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Empresa item = empresas.get(position);

        holder.full_name.setText(item.getNombre());
        holder.tel_1.setText(item.getTelefono1());
        holder.tel_2.setText(item.getTelefono2());
    }

    @Override
    public int getItemCount() {
        return empresas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView full_name;
        TextView tel_1;
        TextView tel_2;

        public ViewHolder(View itemView) {
            super(itemView);
            full_name = itemView.findViewById(R.id.full_name);
            tel_1 = itemView.findViewById(R.id.tel_1);
            tel_2 = itemView.findViewById(R.id.tel_2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSearch(getAdapterPosition());
                }
            });
        }
    }


}
