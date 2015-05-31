package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.sql.SQLException;

//TODO: make longforms clickable so that they expand into their full form. normally only display ~20 lines

public class MainUserTab extends Fragment {

    private DrawerLayout mDrawerLayout;
    private static UserStoryDb userDb;
    private static RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_user_tab_content, container, false);

        final FragmentActivity mFragmentActivity = getActivity();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.user_content_recycler);
        View mRelativeLayout = (View) view.findViewById(R.id.user_content_relative_layout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize local db connection
        userDb = new UserStoryDb(this.getActivity());
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final AdapterStoryRecycler mAdapter = new AdapterStoryRecycler(userDb.getAllStories());
        mRecyclerView.setAdapter(mAdapter);

        // allow child (recycler) to handle touch events, or give them up to slidingtablayout when applicable
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float x1, x2, y1, y2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        if (x1 > 73) {
                            //leave room for drawer to be pulled
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = event.getX();
                        y2 = event.getY();
                        if (x2 < x1) {
                            //if you sliding left, allow for intercept to be handled by slidingTabLayout
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                }
                return false;
            }
        });




        ///// Swipe To Post /////
        SwipeableRecyclerViewTouchListener swipeTouchListener =
            new SwipeableRecyclerViewTouchListener(mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    public boolean canSwipeLeft(int position) {
                        return false;
                    }
                    public boolean canSwipeRight(int position) {
                        return true;
                    }

                    //////////
                    //TODO: onDismissedSwipeRight and onDismissedSwipeLeft are backwards. i'll fix this when i have time
                    //////////
                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        Log.w("swipeRecyclerView","Left");
                        return;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        Log.w("swipeRecyclerView", "Right");
                        AdapterStoryRecycler updatedAdapter = (AdapterStoryRecycler) recyclerView.getAdapter();
                        for (int position : reverseSortedPositions) {
                            Log.w("SwipeableRecyclerViewTouchListener " + String.valueOf(position), String.valueOf(position));
                            ClassStoryInfo swipedStory = updatedAdapter.getItem(position);
                            ClassLocationInfo cli = MainLocationTab.getcurrentLocationInfo();
                            if (cli == null){
                                Toast.makeText(getActivity(),"Please find a location to post to", Toast.LENGTH_LONG).show();
                                return;
                            }
                            swipedStory.setLatitude(cli.getLatitude());
                            swipedStory.setLongitude(cli.getLongitude());

                            //TODO: POST STORY FUNCTION
                            MainLocationTab mlt = new MainLocationTab();
                            try {
                                mlt.postStory(swipedStory);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            userDb.deleteStory(swipedStory);
                            //TODO: Give option to not delete?
                            //updatedAdapter.notifyItemRemoved(position);
                        }
                        refreshUserRecycler();
                    }
                });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);


        // Initialize FAB
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.user_fab);
        fab.attachToRecyclerView(mRecyclerView);
        // FAB: open FragmentNewStory when pressed
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                ft.replace(R.id.drawer_layout, new FragmentNewStory()).addToBackStack(null).commit();
            }
        });
        return view;
    }

    public static UserStoryDb getUserDb(){
        return userDb;
    }

    public static void refreshUserRecycler() {
        if (mRecyclerView != null) {
            Toast.makeText(mRecyclerView.getContext(),"Refreshing Stories",Toast.LENGTH_SHORT).show();
            AdapterStoryRecycler updatedAdapter = new AdapterStoryRecycler(userDb.getAllStories());
            mRecyclerView.setAdapter(updatedAdapter);
            updatedAdapter.notifyDataSetChanged();
        }
    }


}