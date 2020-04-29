package com.example.sdlproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPageAdapter introViewPageAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        if (restorePrefData()) {
                Intent mainActivity = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(mainActivity);
                finish();

        } else {

            tabIndicator = findViewById(R.id.tab_indicator);
            btnNext = findViewById(R.id.btn_next);
            btnGetStarted = findViewById(R.id.btn_get_started);
            btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

            final List<ScreenItem> mList = new ArrayList<>();
            mList.add(new ScreenItem("Agro Marketing", "Easy marketing between farmer and buyer. Farmer can easily place their crops, which is available to buyer for purchase.", R.drawable.img1));
            mList.add(new ScreenItem("Simple To Understand", "Secured Account, Secured account, so that you can only access it, with simple layout, which is easy to understand for everyone.", R.drawable.img2));
            mList.add(new ScreenItem("Easy Payment", "Easy and secure payment on app only, through UPI, Debit Card, Credit Card and Paytm. You can go all cashless.", R.drawable.img3));


            screenPager = findViewById(R.id.screen_viewpager);
            introViewPageAdapter = new IntroViewPageAdapter(this, mList);
            screenPager.setAdapter(introViewPageAdapter);

            //set up tablayout with tab viewPager

            tabIndicator.setupWithViewPager(screenPager);

            //next button click Listener

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    position = screenPager.getCurrentItem();
                    if (position < mList.size()) {

                        position++;
                        screenPager.setCurrentItem(position);

                    }

                    if (position == mList.size() - 1) {

                        //TODO: show the GETSTARTED button and hide the indicator and the next button

                        loadLastScreen();

                    }

                }
            });

            tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == mList.size() - 1) {
                        loadLastScreen();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            btnGetStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent mainActivity = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(mainActivity);

                    savePrefsData();
                    finish();

                }
            });

        }
    }

    private boolean restorePrefData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;

    }

    private void savePrefsData() {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();

    }

    private void loadLastScreen() {

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }
}
