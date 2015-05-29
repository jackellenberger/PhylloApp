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

import java.util.ArrayList;
import java.util.List;

public class MainLocationTab extends Fragment {

    private View myView;
    private DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_location_tab_content, container, false);
        final FragmentActivity mFragmentActivity = getActivity();
        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.location_content_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mFragmentActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final AdapterStoryRecycler mAdapter = new AdapterStoryRecycler(generateLocalData(3));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            private float x1, x2temp;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        if (x1 < v.getWidth() - 73) {
                            //leave room for drawer to be pulled
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2temp = event.getX();
                        if (x2temp > x1) {
                            //if you sliding right, allow for intercept to be handled by slidingTabLayout
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
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
                        Log.w("swipeRecyclerView", "Left");
                        AdapterStoryRecycler updatedAdapter = (AdapterStoryRecycler) recyclerView.getAdapter();
                        for (int position : reverseSortedPositions) {
                            Log.w("SwipeableRecyclerViewTouchListener " + String.valueOf(position), String.valueOf(position));
                            ClassStoryInfo swipedStory = updatedAdapter.getItem(position);
                            //updatedAdapter.notifyItemRemoved(position);
                        }
                        //updatedAdapter.notifyDataSetChanged();
                        return;
                    }
                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        Log.w("swipeRecyclerView", "Right");
                    }
                });
        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        return view;

    }

    //TODO: fill generateLocalData with functions that will query external for stories
    private List<ClassStoryInfo> generateLocalData(int size) {

        List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
        java.util.Date date= new java.util.Date();
        long currentTime = date.getTime();
        for (int i=1; i <= size; i++) {
            ClassStoryInfo csi = new ClassStoryInfo();
            if (i % 3 == 0) {
                csi.setType("tip");
                csi.setTitle("This is tip number " + (i/3));
                csi.setContent("You shouldn't be able to see this!!1!");
                csi.setTimestamp(currentTime);
                csi.setOriginalPoster("The Quokka In The Sky");
                csi.setTagList(new String[]{"tweet","tweet","imma bird"});
            }
            else if (i % 3 == 1) {
                csi.setType("longform");
                csi.setTitle("This is longform number " + (i / 3));
                csi.setContent(getString(R.string.filler_text));
                csi.setTimestamp(currentTime);
                csi.setOriginalPoster("The Quokka In The Sky");
                csi.setTagList(new String[]{"Latin filler","blag","quokka"});
            }
            else{
                csi.setType("link");
                csi.setTitle("This is link number " + (i / 3));
                csi.setContent("https://cs.uchicago.edu");
                csi.setTimestamp(currentTime);
                csi.setOriginalPoster("The Quokka In The Sky");
                csi.setTagList(new String[]{"uchicago","cs","edu","educate yo self"});
            }
            result.add(csi);
        }
        return result;
    }

}