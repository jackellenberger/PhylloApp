package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    //TODO: Implement hiding tab bar
    //TODO: when longform cards are clicked open in new fragment

    //TOOLBAR / APPBAR
    private Toolbar toolbar;

    //TABS
    ViewPager pager;
    AdapterSlidingTab adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"User", "Local"};
    int Numboftabs = 2;
    int navIconWidth = -1;

    //DRAWER - LEFT
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String USERNAME = "Default Username";
    String USEREMAIL = "quokka@uchicago.edu";
    int USERICON = R.drawable.default_user_icon;
    RecyclerView mLeftDrawerRecyclerView;
    RecyclerView.Adapter mLeftDrawerAdapter;
    RecyclerView.LayoutManager mLeftRecyclerLayoutManager;

    //DRAWER - RIGHT
    String LOCATIONNAME = "Default Location";
    RecyclerView mRightDrawerRecyclerView;
    RecyclerView.Adapter mRightDrawerAdapter;
    RecyclerView.LayoutManager mRightRecyclerLayoutManager;

    //DRAWER CONTENTS
    //TODO: Find a way to have settings and feedback stuck to the bottom of the drawer. This will probably take another recyclerView ugh
    //TODO: Figure out how to do drawables the correct way, where they're the correct res for any display
    //TODO: related: get higher res images
    String LEFT_TITLES[] = {"Statistics","Reputation","Edit User","Logout","Settings","Feedback"};
    int LEFT_ICONS[] = {R.mipmap.ic_assessment_grey600_48dp,R.mipmap.ic_assignment_ind_grey600_48dp,R.mipmap.ic_face_grey600_48dp,R.mipmap.ic_highlight_remove_grey600_48dp, R.mipmap.ic_settings_grey600_48dp,R.mipmap.ic_email_grey600_48dp};
    String RIGHT_TITLES[] = {"Statistics","Best Of","Most Viral","Edit Location","Nearby Locations"};
    int RIGHT_ICONS[] = {R.mipmap.ic_assessment_grey600_48dp,R.mipmap.ic_grade_grey600_48dp,R.mipmap.ic_bug_report_grey600_48dp,R.mipmap.ic_rate_review_grey600_48dp,R.mipmap.ic_pin_drop_grey600_48dp};

    // LOCAL DATABASE
    private UserStoryDb userDb;

    //SWIPABLE CARDS
    private RecyclerView mUserRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // LOCAL DATABASE
        //this.deleteDatabase("localUserQueue.db"); // To recreate the db each time for now
        userDb = new UserStoryDb(this);
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // THIS PUTS THREE DUMMY RECORDS IN THE DATABASE
        // To create a story: createStory(String type, String title, String content, long timestamp,
        //      String poster, String tagList, long locationId)
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        //userDb.createStory("tip", "This is tip number 1", "", ts.getTime(), "The Quokka in the Sky", -1.0,-1.0, new String[]{"one"});
        //userDb.createStory("longform", "This is longform number 1", getString(R.string.filler_text), ts.getTime(), "The Quokka in the Sky", -1.0,-1.0, new String[]{"two"});
        //userDb.createStory("url", "This is link number 1", "https://cs.uchicago.edu", ts.getTime(), "The Quokka in the Sky", -1.0,-1.0, new String[]{"three"});
        List<ClassStoryInfo> stories = userDb.getAllStories();
        for (ClassStoryInfo s : stories) {
            Log.d("Story information:", s.toString());
        }

        ///// TOOLBAR / APPBAR /////
        setTitle("Phyllo"); //Text displayed in App Bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ///// TABS /////
        // Creating The AdapterSlidingTab and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new AdapterSlidingTab(getSupportFragmentManager(),Titles,Numboftabs);
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
            private int currentPage;
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                //When in User tab...
                if (position == 0) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.left_drawer));
                }
                //When in Location Tab...
                else if (position == 1) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.left_drawer));
                }
            }
            public final int getCurrentPage() {
                return currentPage;
            }
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (toolbar.getNavigationIcon() != null) {
                    navIconWidth = toolbar.getNavigationIcon().getMinimumWidth();
                }
                if (positionOffset != 0.0 && navIconWidth != -1) {
                    toolbar.scrollTo((int) (positionOffset * toolbar.getWidth() * (navIconWidth*2.0) / toolbar.getWidth()), 0);
                }

            }
            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        ///// RECYCLER FOR DRAWER CONTENTS /////
        // Assigning the RecyclerView Object to the xml View
        mLeftDrawerRecyclerView = (RecyclerView) findViewById(R.id.left_RecyclerView);
        // Letting the system know that the list objects are of fixed size (we won't change the number of tabs)
        mLeftDrawerRecyclerView.setHasFixedSize(true);
        //create the adapter for programatically changing data displayed
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mLeftDrawerAdapter = new AdapterLeftDrawerRecycler(LEFT_TITLES,LEFT_ICONS,USERNAME,USEREMAIL,USERICON);
        // Setting the adapter to RecyclerView
        mLeftDrawerRecyclerView.setAdapter(mLeftDrawerAdapter);
        // Creating a layout Manager
        mLeftRecyclerLayoutManager = new LinearLayoutManager(this);
        // Setting the layout Manager
        mLeftDrawerRecyclerView.setLayoutManager(mLeftRecyclerLayoutManager);

        //And again for the right drawer
        mRightDrawerRecyclerView = (RecyclerView) findViewById(R.id.right_RecyclerView);
        mRightDrawerRecyclerView.setHasFixedSize(true);
        mRightDrawerAdapter = new AdapterRightDrawerRecycler(RIGHT_TITLES,RIGHT_ICONS,LOCATIONNAME);
        mRightDrawerRecyclerView.setAdapter(mRightDrawerAdapter);
        mRightRecyclerLayoutManager = new LinearLayoutManager(this);
        mRightDrawerRecyclerView.setLayoutManager(mRightRecyclerLayoutManager);


        ///// ONTOUCH EVENT HANDLER FOR NAV DRAWER /////
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


        mLeftDrawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawerLayout.closeDrawers();
                    onTouchLeftDrawer(recyclerView.getChildPosition(child));
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) { }
        });
        mRightDrawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawerLayout.closeDrawers();
                    onTouchRightDrawer(recyclerView.getChildPosition(child));
                    //Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) { }
        });



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

        //make right Hamburger button clickable to open the right drawer
        View mRightDrawerToggle = findViewById(R.id.rightHamburger);
        mRightDrawerToggle.isClickable();
        mRightDrawerToggle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(edu.uchicago.cs234.spr15.quokka.phyllo.R.id.right_drawer));

        ///// REFRESH BUTTON /////
        View refreshButton = findViewById(R.id.refreshButton);
        refreshButton.isClickable();
        refreshButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MainLocationTab.getCurrentLocation();
                MainUserTab.refreshUserRecycler();
                MainLocationTab.refreshLocationRecycler();
            }
        });

        ///// SHARING FROM BROWSER TO APP /////
        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()))
        {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (url != null) {
                //create FragmentNewStory when a link is shared to the app
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                FragmentNewStory mFragmentNewStory = new FragmentNewStory();
                ft.replace(R.id.drawer_layout, mFragmentNewStory).addToBackStack(null).commit();
            }
        }
    }

    ///// HELPER FUNCTIONS FOR OPENING FRAGMENTS /////
    private void openFragment(final Fragment fragment) {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
        ft.replace(R.id.drawer_layout, fragment).addToBackStack(null).commit();
        //TODO: make proper fragment handling mechanic. pushing and popping off the FragMan is poor practice and doesn't support nesting
    }


    public void onTouchLeftDrawer(final int position) {
        //{"Statistics","Reputation","Edit User","Logout","Settings","Feedback"}
        switch (position) {
            case 0:
                if (FragmentLogIn.getCurrentUser().getUserName() == null) {
                    openFragment(new FragmentLogIn());
                }
                break;
            case 1:
                openFragment(new FragmentUserStatistics());
                break;
            case 2:
                openFragment(new FragmentUserReputation());
                break;
            case 3:
                openFragment(new FragmentUserEdit());
                break;
            case 4:
                if (FragmentLogIn.getCurrentUser().getUserName() != null) {
                    FragmentLogIn.logOutUser();
                    Toast.makeText(MainActivity.this, "You Have Been Logged Out.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Please Log In Before You Log Out, Dummy", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                openFragment(new FragmentAppSettings());
                break;
            case 6:
                //TODO: Test feedback on an actual device - it doesn't seem to work on the emulator
                String[] emails = {"quokka@uchicago.edu"};
                String subject = "Sup Phyllo Devs";
                String message = "We got a problem...";
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, emails);
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                // need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            default:
                return;
        }
    }
    public void onTouchRightDrawer(final int position) {
        //{"Statistics","Best Of","Most Viral","Edit Location","Nearby Locations"}
        switch (position) {
            case 1:
                openFragment(new FragmentLocationStatistics());
                break;
            case 2:
                openFragment(new FragmentLocationBestOf());
                break;
            case 3:
                openFragment(new FragmentLocationMostViral());
                break;
            case 4:
                openFragment(new FragmentLocationEdit());
                break;
            case 5:
                openFragment(new FragmentLocationNearby());
                break;
            default:
                return;
        }
    }

    /* REMOVE OPTIONS MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        rightDrawerToggle = menu;
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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

    }*/

/*

*/
}
