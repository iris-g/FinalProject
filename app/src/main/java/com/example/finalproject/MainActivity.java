package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        final List<ViewPagerItem> mList = new ArrayList<>();
        mList.add(new ViewPagerItem("Create","Create an organized, convenient and easy-to-use list for all of your shopping",R.drawable.create));
        mList.add(new ViewPagerItem("Share","Sync your list with others and keep them updated at every step",R.drawable.connection));
        mList.add(new ViewPagerItem("Perform","Shop easily: alone or with friends",R.drawable.preform));



        VPAdapter vpAdapter = new VPAdapter(mList);

        viewPager2.setAdapter(vpAdapter);

        viewPager2.setClipToPadding(false);

        viewPager2.setClipChildren(false);

        viewPager2.setOffscreenPageLimit(2);

        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

//    public void showToast()
//    {
//        LayoutInflater layoutInflater = getLayoutInflater();
//        View layout = layoutInflater.inflate(R.layout.toast_layout,
//                (ViewGroup) findViewById(R.id.toast_root));
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }
}