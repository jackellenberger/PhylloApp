package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    //TOOLBAR / APPBAR
    private Toolbar toolbar;

    //TABS
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"User", "Local"};
    int Numboftabs = 2;

    //DRAWER
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String USERNAME = "Default Username";
    String USEREMAIL = "quokka@uchicago.edu";
    int USERICON = R.drawable.default_user_icon;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mRecyclerLayoutManager;

    //DRAWER CONTENTS
    String LEFT_TITLES[] = {"Edit User","Statistics","Reputation","Logout"};
    int LEFT_ICONS[] = {R.drawable.default_icon_error,R.drawable.default_icon_error,R.drawable.default_icon_error,R.drawable.default_icon_error};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///// TOOLBAR / APPBAR /////
        setTitle("Phyllo"); //Text displayed in App Bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ///// TABS /////
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.tab_view_pager);
        pager.setAdapter(adapter);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tab_bar);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.accentColor);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                //When in User tab...
                if (position == 0) {
                    //mDrawerLayout.setScrimColor(0x99000000);
                    //mDrawerToggle.setDrawerIndicatorEnabled(true); counterpart to false call in else
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.left_drawer));

                }
                //When in Location Tab...
                else if (position == 1) {
                    //setScrimColor transparent = stops background from darkening
                    //mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
                    //mDrawerToggle.setDrawerIndicatorEnabled(false); //TODO: Animate left hamburger hiding, add hamburger menu to the right
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.left_drawer));

                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        ///// RECYCLER / DRAWER CONTENTS /////
        // Assigning the RecyclerView Object to the xml View
        mRecyclerView = (RecyclerView) findViewById(R.id.left_RecyclerView);
        // Letting the system know that the list objects are of fixed size (we won't change the number of tabs)
        mRecyclerView.setHasFixedSize(true);
        //create the adapter for programatically changing data displayed
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mAdapter = new LeftRecyclerAdapter(LEFT_TITLES,LEFT_ICONS,USERNAME,USEREMAIL,USERICON);
        // Setting the adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);
        // Creating a layout Manager
        mRecyclerLayoutManager = new LinearLayoutManager(this);
        // Setting the layout Manager
        mRecyclerView.setLayoutManager(mRecyclerLayoutManager);


        ///// DRAWER /////
        // Create Drawer Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
            }
            @Override
            public void onDrawerSlide(View drawerView,float slideOffset){
                super.onDrawerSlide(drawerView,slideOffset);
                //this might be useful for something? Allows you to emulate things in relation
                // to the drawer sliding in and out

            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));
        return true;
    }

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

/*

*/
}
