package edu.uchicago.cs234.spr15.quokka.phyllo;

/**
 * Created by jellenberger on 5/12/15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentNewStory extends Fragment {

    //TOOLBAR / APPBAR
    private Toolbar toolbar;
    private View view;
    private DrawerLayout mDrawerLayout;
    AdapterSlidingTab adapter;
    CharSequence Titles[] = {"Tip", "Link", "Longform"};
    ViewPager pager;
    SlidingTabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fraglayout_new_story, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        toolbar.setTitle("Create New Story");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ///// TABS /////
        adapter =  new AdapterSlidingTab(getActivity().getSupportFragmentManager(),Titles,Titles.length);
        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.story_type_pager);
        pager.setAdapter(adapter);
        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.story_type_bar);
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
            public void onPageSelected(int position) { currentPage = position; }
            public final int getCurrentPage() {
                return currentPage;
            }
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()))
        {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (url != null) {
                pager.setCurrentItem(1);
            }
        }


        return view;
    }
}
