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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

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

    // To convert from JSON to story
    private class TempStory {
//        private int id;
        private String type;
        private String title;
        private String content;
        private String timestamp;
//        private int originalPoster;
//        private int location;
//        private int[] tags;
    }

    // "http://10.0.3.2:8000"
    private static final String API_URL = "https://floating-wildwood-9614.herokuapp.com";

    public interface LocationService {
        @GET("/stories")
        public void getHelloWorld(Callback<String> str);

        @GET("/stories/{pk}")
        // Asynchronously with a callback
        public void getStory(@Path("pk") int pk, Callback<TempStory> story);

        // not working ugh
        @POST("/stories/new")
        public void sendStory(@Body ClassStoryInfo story, Callback<String> str);

        @GET("/stories/{longitude}/{latitude}/{radius}")
        public void getLocationStories(@Path("longitude") long longitude,
                                       @Path("latitude") long latitude, @Path("radius") int radius,
                                       Callback<List<TempStory>> stories);

    }

    private void getLocationStories(long longitude, long latitude, int radius) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_URL)
                .build();
        LocationService service = restAdapter.create(LocationService.class);
        service.getLocationStories(longitude, latitude, radius, new Callback<List<TempStory>>() {
            @Override
            public void success(List<TempStory> tempStories, Response response) {
                Log.d("s", "success " + tempStories.size());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failed", error.getMessage());
            }
        });
    }

    public void postStory(ClassStoryInfo story) throws IOException {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_URL)
                .build();
        LocationService service = restAdapter.create(LocationService.class);
        service.sendStory(story, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d("can you feel it", s);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failed", error.getMessage());
            }
        });
    }

    //TODO: fill generateLocalData with functions that will query external for stories
    private List<ClassStoryInfo> generateLocalData(int size) {
        getLocationStories(40, 40, 200); // Get stories based on location info

        final List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                //.setConverter(new StringConverter())
                .build();

        LocationService service = restAdapter.create(LocationService.class);

        service.getStory(1, new Callback<TempStory>() {
            @Override
            public void success(TempStory s, Response response) {
                Log.d("success", s.title);
                ClassStoryInfo story = new ClassStoryInfo();
                story.setType(s.type);
                story.setTitle(s.title);
                story.setContent(s.content);
                result.add(story);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failed", error.getMessage());
            }
        });
        return result;
    }

}