package com.example.amador.tucanchape.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amador.tucanchape.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;

    }

    public int[] slide_images = {
      R.drawable.field,
      R.drawable.calendar,
      R.drawable.map
    };

    public static  String[] slide_headings = {
      "CANCHAS SINTETICAS",
      "ORGANIZATE",
              "EN TODO LUGAR"
    };

    public String[] slide_descs = {
            "Información de tu cancha favorita",
            "Podrás visualizar la disponibilidad de tu cancha",
            "Cancha deportivas en todo lugar"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == (RelativeLayout)o;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
      View view = layoutInflater.inflate(R.layout.item_slider, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_images);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }
}
