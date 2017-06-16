package in.techtatva.techtatva17.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import in.techtatva.techtatva17.R;
import in.techtatva.techtatva17.application.TechTatva;
import in.techtatva.techtatva17.fragments.AboutUsFragment;
import in.techtatva.techtatva17.fragments.CategoriesFragment;
import in.techtatva.techtatva17.fragments.DevelopersFragment;
import in.techtatva.techtatva17.fragments.EventsFragment;
import in.techtatva.techtatva17.fragments.FavouritesFragment;
import in.techtatva.techtatva17.fragments.HomeFragment;
import in.techtatva.techtatva17.fragments.OnlineEventsFragment;
import in.techtatva.techtatva17.fragments.ResultsFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity  {
    private FragmentManager fm;
    private NavigationView drawerView;
    private BottomNavigationView navigation;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.setScrimColor(Color.TRANSPARENT);  //If enabled, drops GPU overdraw area to some extent
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


         drawerView = (NavigationView) findViewById(R.id.nav_view);
        drawerView.setNavigationItemSelectedListener(mOnDrawerItemSelectedListener);
        drawerView.setCheckedItem(R.id.drawer_home);
        drawerView.setSelected(true);



         navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnBottomNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.bottom_nav_home);
        navigation.setSelected(true);

        fm = getSupportFragmentManager();
    }





    private BottomNavigationView.OnNavigationItemSelectedListener mOnBottomNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.bottom_nav_events:
                    selectedFragment = EventsFragment.newInstance();
                    break;
                case R.id.bottom_nav_categories:
                    selectedFragment = CategoriesFragment.newInstance();
                    break;

            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }

    };

    private NavigationView.OnNavigationItemSelectedListener mOnDrawerItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerView = (NavigationView) findViewById(R.id.nav_view);
            int id = item.getItemId();

            if (id == R.id.drawer_home) {

                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(VISIBLE);
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(VISIBLE);
                navigation.setSelectedItemId(R.id.bottom_nav_home);
                selectedFragment = HomeFragment.newInstance();

            } else if (id == R.id.drawer_favourites) {
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(VISIBLE);
                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(GONE);

                selectedFragment = FavouritesFragment.newInstance();

            } else if (id == R.id.drawer_online_events) {
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(GONE);
                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(VISIBLE);

                selectedFragment = OnlineEventsFragment.newInstance();

            } else if (id == R.id.drawer_results) {
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(VISIBLE);
                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(GONE);

                selectedFragment = ResultsFragment.newInstance();

            } else if (id == R.id.drawer_developers) {
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(GONE);
                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(GONE);

                selectedFragment = DevelopersFragment.newInstance();

            } else if (id == R.id.drawer_about_us) {
                 appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
                appBarLayout.setVisibility(GONE);
                 navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
                navigation.setVisibility(GONE);

                selectedFragment = AboutUsFragment.newInstance();

            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame_layout, selectedFragment);
            transaction.commit();


            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            if (drawerView.getMenu().getItem(0).isChecked() &&  navigation.getMenu().getItem(0).isChecked() ){
                finishAffinity();
            }
            else{
                if(TechTatva.searchOpen ==1 && drawerView.getMenu().getItem(0).isChecked()){

                    fm.beginTransaction().replace(R.id.main_frame_layout, new CategoriesFragment()).commit();

                    TechTatva.searchOpen =0;
                }

                if(TechTatva.searchOpen ==2 && drawerView.getMenu().getItem(0).isChecked()){

                    fm.beginTransaction().replace(R.id.main_frame_layout, new EventsFragment()).commit();

                    TechTatva.searchOpen =0;
                }

                else{
                    fm.beginTransaction().replace(R.id.main_frame_layout, new HomeFragment()).commit();
                    drawerView.setCheckedItem(R.id.drawer_home);
                    navigation.setSelectedItemId(R.id.bottom_nav_home);

                }


                if(navigation.getVisibility()==GONE)
                { navigation.setVisibility(VISIBLE);}

                if(appBarLayout!=null)
                {appBarLayout.setVisibility(VISIBLE);}
            }
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();


    }




/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hardware, menu);
        return true;
    }



    For menu option... Not implemented yet

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}


