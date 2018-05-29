package com.example.amador.tucanchape.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.activity.PerfilActivity;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    Context context;
    ArrayList<String> fullNameList;
    ArrayList<String> tel1;
    ArrayList<String> tel2;





    class SearchViewHolder extends RecyclerView.ViewHolder {
        private  ImageView profileImage;
        private TextView full_name, tel1_emp, tel2_emp;
        private View.OnClickListener listener;
        private LinearLayout parentLayout;


        public SearchViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            full_name = (TextView) itemView.findViewById(R.id.full_name);
            //77falta telefnos pero no hace falta mostrar en listado

            parentLayout = (LinearLayout)itemView.findViewById(R.id.parent_layout);



        }
    }
    public SearchAdapter(Context context, ArrayList<String> fullNameList, ArrayList<String> tf1, ArrayList<String> tf2) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.tel1 = tf1;
        this.tel2 = tf2;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        holder.full_name.setText(fullNameList.get(position));


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, fullNameList.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, PerfilActivity.class);
                intent.putExtra("nombre", fullNameList.get(position));
                intent.putExtra("telefono1", tel1.get(position));
                intent.putExtra("telefono2", tel2.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }


}
