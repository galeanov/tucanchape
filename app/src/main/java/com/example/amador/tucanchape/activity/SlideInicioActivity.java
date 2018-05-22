package com.example.amador.tucanchape.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.SliderAdapter;

public class SlideInicioActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private SliderAdapter sliderAdapter;
    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_inicio);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        mNextBtn = (Button)findViewById(R.id.nextBtn);
        mBackBtn = (Button)findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int currentPage  = mSlideViewPager.getCurrentItem()+1;
                    if (currentPage < mDots.length){
                        mSlideViewPager.setCurrentItem(currentPage);
                    }else{
                        Intent intent = new Intent(SlideInicioActivity.this, InicioSesionActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorwhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorwhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
        addDotsIndicator(i);
        mCurrentPage = i;

        if (i == 0){
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(false);
            mBackBtn.setVisibility(View.INVISIBLE);
            mNextBtn.setText("Next");
            mBackBtn.setText("");
        }else if (i == mDots.length - 1){
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);
            mNextBtn.setText("Finish");
            mBackBtn.setText("Back");
        }else{
            mNextBtn.setEnabled(true);
            mBackBtn.setEnabled(true);
            mBackBtn.setVisibility(View.VISIBLE);
            mNextBtn.setText("Next");
            mBackBtn.setText("Back");
        }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
