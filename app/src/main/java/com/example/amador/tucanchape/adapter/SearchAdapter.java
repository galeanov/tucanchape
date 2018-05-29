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
    ArrayList<String> userNameList;
    ArrayList<String> profilePicList;
    ArrayList<String> telefono1;




    class SearchViewHolder extends RecyclerView.ViewHolder {
        private  ImageView profileImage;
        private TextView full_name, tel1_emp, tel2_emp;
        private View.OnClickListener listener;
        private LinearLayout parentLayout;


        public SearchViewHolder(View itemView) {
            super(itemView);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
            full_name = (TextView) itemView.findViewById(R.id.full_name);

            parentLayout = (LinearLayout)itemView.findViewById(R.id.parent_layout);



        }
    }
    public SearchAdapter(Context context, ArrayList<String> fullNameList, ArrayList<String> userNameList, ArrayList<String> profilePicList) {
        this.context = context;
        this.fullNameList = fullNameList;
        this.userNameList = userNameList;
        this.profilePicList = profilePicList;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchAdapter.SearchViewHolder(view);

    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        holder.full_name.setText(fullNameList.get(position));
        Glide.with(context).load(profilePicList.get(position)).asBitmap().placeholder(R.mipmap.ic_launcher_round).into(holder.profileImage);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, fullNameList.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, PerfilActivity.class);
                intent.putExtra("nombre", fullNameList.get(position));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fullNameList.size();
    }


}
