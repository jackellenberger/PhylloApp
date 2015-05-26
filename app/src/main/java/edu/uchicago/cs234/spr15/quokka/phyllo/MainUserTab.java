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

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.melnykov.fab.FloatingActionButton;

import java.sql.SQLException;
import java.util.List;

//TODO: make longforms clickable so that they expand into their full form. normally only display ~20 lines
//TODO: make links hyperlinks
//TODO: capture share from external

public class MainUserTab extends Fragment {

    private DrawerLayout mDrawerLayout;
    private UserStoryDb userDb;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View myView = inflater.inflate(R.layout.main_user_tab_content,container,false);
        //return myView;

        final View view = inflater.inflate(R.layout.main_user_tab_content, container, false);
        final FragmentActivity mFragmentActivity = getActivity();
        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.user_content_recycler);
        View mRelativeLayout = (View) view.findViewById(R.id.user_content_relative_layout);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize FAB
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.user_fab);
        fab.attachToRecyclerView(mRecyclerView);

        // Initialize local db connection
        userDb = new UserStoryDb(this.getActivity());
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final AdapterStoryRecycler mAdapter = new AdapterStoryRecycler(generateUserData());
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float x1, x2, x2temp;
            static final int MIN_DISTANCE = 150;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        if (x1 > 73) {
                            //leave room for drawer to be pulled
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } break;
                    case MotionEvent.ACTION_MOVE:
                        x2temp = event.getX();
                        if (x2temp < x1) {
                            //if you sliding left, allow for intercept to be handled by slidingTabLayout
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        } break;
                }
                return false;
            }
        });

        SwipeableRecyclerViewTouchListener swipeTouchListener =
            new SwipeableRecyclerViewTouchListener(mRecyclerView,
                new SwipeableRecyclerViewTouchListener.SwipeListener() {
                    @Override
                    public boolean canSwipe(int position) {
                        return true;
                    }

                    @Override
                    public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        Log.w("swipeRecyclerView","Left");
                        return;
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        Log.w("swipeRecyclerView", "Right");
                        AdapterStoryRecycler updatedAdapter = (AdapterStoryRecycler) recyclerView.getAdapter();
                        for (int position : reverseSortedPositions) {
                            Log.w("SwipeableRecyclerViewTouchListener " + String.valueOf(position), String.valueOf(position));
                            ClassStoryInfo swipedStory = updatedAdapter.getItem(position);
                            //TODO: POST STORY FUNCTION
                            userDb.deleteStory(swipedStory); //TODO: WHY ISN'T IT DELETING
                            //TODO: Give option to not delete
                            updatedAdapter.notifyItemRemoved(position);
                        }
                        updatedAdapter.notifyDataSetChanged();
                    }
                });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);



        // FAB: open FragmentNewStory when pressed
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top, R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
                ft.replace(R.id.drawer_layout, new FragmentNewStory()).addToBackStack(null).commit();
            }
        });
        return view;
    }
    //TODO: fill generateUserData with functions that will query local db for stories
    public List<ClassStoryInfo> generateUserData() {

//        List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
//        java.util.Date date= new java.util.Date();
//        long currentTime = date.getTime();
//        for (int i=1; i <= size; i++) {
//            ClassStoryInfo csi = new ClassStoryInfo();
//            if (i%3==0) {
//                csi.setType("tip");
//                csi.setTitle("This is tip number " + (i/3));
//                csi.setContent("You shouldn't be able to see this!!1!");
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"tweet"});
//            }
//            else if ((i+1)%3==0) {
//                csi.setType("link");
//                csi.setTitle("This is link number " + (i / 3));
//                csi.setContent("https://cs.uchicago.edu");
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"uchicago"});
//            }
//            else {
//                csi.setType("longform");
//                csi.setTitle("This is longform number " + (i / 3));
//                csi.setContent(getString(R.string.filler_text));
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"Latin filler"});
//            }
//            result.add(csi);
//        }
        return userDb.getAllStories();
    }

}