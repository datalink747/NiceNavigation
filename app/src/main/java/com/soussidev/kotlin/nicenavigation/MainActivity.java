package com.soussidev.kotlin.nicenavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soussidev.kotlin.navigationview.PaperFlower.PaperFlower;
import com.soussidev.kotlin.navigationview.PaperFlower.PaperFlowerListener;
import com.soussidev.kotlin.navigationview.navigation.NavigationView;
import com.soussidev.kotlin.navigationview.navigation.OnNavigationItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PaperFlower paperflower;
    private ImageView mImage;
    private Button mButton;
    private TextView mText;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private int[] image = {R.mipmap.icon_date, R.mipmap.icon_date, R.mipmap.icon_date};
    private int[] color = {R.color.menu_color_9,R.color.menu_color_1,R.color.menu_color_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paperflower = PaperFlower.attach2Window(this);

        navigationView =(NavigationView)findViewById(R.id.navigationview);

        if (navigationView != null) {
            navigationView.isWithText(true);
            navigationView.disableShadow();
            navigationView.isColoredBackground(false);
            navigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.menu_color_10));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);



        navigationView.setUpWithViewPager(viewPager, color, image, MainActivity.this);
        navigationView.setOnBottomNavigationItemClickListener(new OnNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {

             getflower(toolbar);
             //   navigationView.selectTab(index);
                toolbar.setTitle("Nice Navigation");
                toolbar.setSubtitle("By soussidev");
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                toolbar.setSubtitleTextColor(getResources().getColor(R.color.menu_color_1));
                toolbar.setLogo(R.mipmap.icon_fav);

            }
        });



    }
    // Setup View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new
                ViewPagerAdapter(getSupportFragmentManager());

        //change the fragmentName as per your need

        adapter.addFragment(new PageFragment1(), "Page 1");
        adapter.addFragment(new PageFragment2(), "Page 2");
        adapter.addFragment(new PageFragment3(), "Page 3");

        viewPager.setAdapter(adapter);
    }
    //View Pager Adaptateur

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }




    //Paper Flower

    public void getflower(final View view){
        paperflower.paperflower(view,450,new PaperFlowerListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }


    public void getlike(View view){
       paperflower.setColors(color);
        paperflower.paperflower(view);
        paperflower.setDotNumber(100);
        paperflower.setmListener(new PaperFlowerListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {

            }
        });
    }

    private void toast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }
}
