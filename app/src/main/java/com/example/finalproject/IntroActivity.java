package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    Button skipBtn;
    SharedPreferences prefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //get SP of the app
        prefs = getSharedPreferences("com.example.finalproject", MODE_PRIVATE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //check if this is the first time the user runs the app if yes show him the intro
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            ViewPager2 viewPager2 = findViewById(R.id.viewPager);
            Button nextBtn = findViewById(R.id.btn_next);
            skipBtn = findViewById(R.id.btn_get_started);

            final List<ViewPagerItem> mList = new ArrayList<>();
            mList.add(new ViewPagerItem("Create", "Create an organized, convenient and easy to use list for all of your shopping", R.drawable.create));
            mList.add(new ViewPagerItem("Share", "Sync your list with others and keep them updated at every step", R.drawable.connection));
            mList.add(new ViewPagerItem("Perform", "Make your shopping experience much easier to shop alone or with friends", R.drawable.preform));

            /* create an adapter for the view pagers item list */
            VPAdapter vpAdapter = new VPAdapter(mList);
            viewPager2.setAdapter(vpAdapter);


            TabLayout tabLayout = findViewById(R.id.tab_indicator);

            /* create a tablayout mediator to be able to connect tablayot with viewPager2  */
            new TabLayoutMediator(tabLayout, viewPager2,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            // tab.setText("Tab " + (position + 1));
                        }
                    }).attach();

            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainActivity);

                    //   savePrefsData();


                }
            });
            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index=viewPager2.getCurrentItem() + 1;
                    viewPager2.setCurrentItem( index, true);

                    /* if we reached last intro screen and next pressed open main layout  */
                    if(index == 3)
                    {
                        Intent loginActivity = new Intent(getApplicationContext(),userLoginActivity.class);
                        startActivity(loginActivity);
                        //  Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                        //   startActivity(mainActivity);

                        //   savePrefsData();
                        finish();

                    }
                    Log.i("myTag", String.valueOf(viewPager2.getCurrentItem() + 1));
                }
            });

            viewPager2.setClipToPadding(false);

            viewPager2.setClipChildren(false);

            viewPager2.setOffscreenPageLimit(2);

            viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
        else {
            Intent userLogin = new Intent(getApplicationContext(),userLoginActivity.class);
            startActivity(userLogin);

        }


    }


}
