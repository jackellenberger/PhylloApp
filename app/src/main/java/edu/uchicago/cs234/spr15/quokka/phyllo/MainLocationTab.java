package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    private long LOCATION_QUEUE_RADIUS = 5; //TODO: what is the location queue radisu supposed to be?
    private ClassLocationInfo currentLocationInfo;
    private LocationListener mLocationListener;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

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
                    public boolean canSwipeLeft(int position) {return true;}
                    public boolean canSwipeRight(int position) {
                        return false;
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
                        getCurrentLocation(); //TODO: remove this when i have a better solution
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
        getCurrentLocation();
        List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
        result = getLocationStories(40, 40, 200);
//        java.util.Date date= new java.util.Date();
//        long currentTime = date.getTime();
//        for (int i=1; i <= size; i++) {
//            ClassStoryInfo csi = new ClassStoryInfo();
//            if (i % 3 == 0) {
//                csi.setType("tip");
//                csi.setTitle("This is tip number " + (i/3));
//                csi.setContent("You shouldn't be able to see this!!1!");
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"tweet","tweet","imma bird"});
//            }
//            else if (i % 3 == 1) {
//                csi.setType("longform");
//                csi.setTitle("This is longform number " + (i / 3));
//                csi.setContent(getString(R.string.filler_text));
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"Latin filler","blag","quokka"});
//            }
//            else{
//                csi.setType("link");
//                csi.setTitle("This is link number " + (i / 3));
//                csi.setContent("https://cs.uchicago.edu");
//                csi.setTimestamp(currentTime);
//                csi.setOriginalPoster("The Quokka In The Sky");
//                csi.setTagList(new String[]{"uchicago","cs","edu","educate yo self"});
//            }
//            result.add(csi);
//        }
        return result;
    }

    //////// LOCATION BUSINESS //////////

    protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return newLocation;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return newLocation;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }
    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /** Getting stories from server */
    private static final String API_URL = "https://floating-wildwood-9614.herokuapp.com";

    // Temporary story for converting JSON to story
    private class TempStory {
        private String type;
        private String title;
        private String content;
        private String timestamp;
    }

    public interface LocationService {
        @GET("/stories")
        public void getHelloWorld(Callback<String> str);

        @GET("/stories/{pk}")
        // Asynchronously with a callback
        public void getStory(@Path("pk") int pk, Callback<TempStory> story);

        @POST("/stories/new")
        public void sendStory(@Body ClassStoryInfo story, Callback<String> str);

        @GET("/stories/{longitude}/{latitude}/{radius}")
        public void getLocationStories(@Path("longitude") long longitude,
                                       @Path("latitude") long latitude, @Path("radius") int radius,
                                       Callback<List<TempStory>> stories);

    }

    private List<ClassStoryInfo> getLocationStories(long longitude, long latitude, int radius) {
        final List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_URL)
                .build();
        LocationService service = restAdapter.create(LocationService.class);
        service.getLocationStories(longitude, latitude, radius, new Callback<List<TempStory>>() {
            @Override
            public void success(List<TempStory> tempStories, Response response) {
                Log.d("s", "success " + tempStories.size());
                for (int i = 0; i < tempStories.size(); i++) {
                    ClassStoryInfo s = new ClassStoryInfo();
                    TempStory ts = tempStories.get(i);
                    s.setTitle(ts.title);
                    s.setType(ts.type);
                    s.setContent(ts.content);
                    result.add(s);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failed", error.getMessage());
            }
        });

        return result;
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
                Log.d("story posted!", s);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("failed", error.getMessage());
            }
        });
    }

    public void getCurrentLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (currentLocationInfo == null) {
            Log.w("currentLocationInfo","creating mLocationListener");
            currentLocationInfo = new ClassLocationInfo();
            currentLocationInfo.setLocationId(0);
            currentLocationInfo.setLocationObject(null);
            currentLocationInfo.setLatitude("Waiting for Location");
            currentLocationInfo.setLongitude("Waiting for Location");
            currentLocationInfo.setLocationName("Waiting for Location");
            currentLocationInfo.setRadius(LOCATION_QUEUE_RADIUS);
            updateLocationHeader();
            mLocationListener = new LocationListener() {
                public void onLocationChanged(Location loc){
                    String latString = String.valueOf(loc.getLatitude());
                    String lonString = String.valueOf(loc.getLongitude());
                    currentLocationInfo.setLocationObject(loc);

                    //TODO: private ClassLocationInfo getLocationQueueInfo(latString,lonString,LOCATION_QUEUE_RADIUS)
                    // NOTE: cast radius to INT for now
                    //List<ClassStoryInfo> stories = getLocationStories(Long.valueOf(lonString), Long.valueOf(latString), (int)LOCATION_QUEUE_RADIUS)
                    currentLocationInfo.setLatitude(latString);
                    currentLocationInfo.setLongitude(lonString);
                    currentLocationInfo.setLocationId(0);
                    currentLocationInfo.setLocationName("Some Location");
                    currentLocationInfo.setRadius((long) LOCATION_QUEUE_RADIUS);

                }
                public void onProviderDisabled(String info){}
                public void onProviderEnabled(String info){}
                public void onStatusChanged(String arg1, int arg2, Bundle arg3){}
            };
            if(locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            }
            if(locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            }
        }
        else{
            Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            currentLocationInfo.setLocationObject(getBetterLocation(lastLocation, currentLocationInfo.getLocationObject()));

            String latString = String.valueOf(currentLocationInfo.getLatitude());
            String lonString = String.valueOf(currentLocationInfo.getLongitude());

            //TODO: private ClassLocationInfo getLocationQueueInfo(latString,lonString,LOCATION_QUEUE_RADIUS)

            currentLocationInfo.setLatitude(latString);
            currentLocationInfo.setLongitude(lonString);
            currentLocationInfo.setLocationId(0);
            currentLocationInfo.setLocationName("Some Location");
            currentLocationInfo.setRadius((long) LOCATION_QUEUE_RADIUS);
            updateLocationHeader();

        }

    }


    private void updateLocationHeader(){
        RecyclerView mRightDrawer = (RecyclerView) getActivity().findViewById(R.id.right_RecyclerView);
        AdapterRightDrawerRecycler mRightDrawerAdapter = (AdapterRightDrawerRecycler) mRightDrawer.getAdapter();

        mRightDrawerAdapter.setHeaderText(currentLocationInfo.getLatitude(), currentLocationInfo.getLongitude(), currentLocationInfo.getLocationName());
    }
}