package edu.uchicago.cs234.spr15.quokka.phyllo;

/**
 * Created by jellenberger on 5/10/15.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterRightDrawerRecycler extends RecyclerView.Adapter<AdapterRightDrawerRecycler.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    private String location;        //String Resource for header View Name
    private static View headerView;


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        TextView locationName;



        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


                locationName = (TextView) itemView.findViewById(R.id.location_header_text);         // Creating Text View object from header.xml for locationName
                Holderid = 0;                                                   // Setting holder id = 0 as the object being populated are of type header view
            }
        }


    }



    AdapterRightDrawerRecycler(String Titles[], int Icons[], String locationName){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, locationName, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        location = locationName;



    }



    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public AdapterRightDrawerRecycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawerutil_recycler_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawerutil_location_drawer_header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(headerView,viewType); //Creating ViewHolder and passing the object of type view



            return vhHeader; //returning the object created


        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(AdapterRightDrawerRecycler.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
        }
        else{
            holder.locationName.setText(location);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static void setHeaderText(double lat, double lon, String name){
        if (headerView != null) { //TODO: find a better solution than this
            TextView headerLat = (TextView) headerView.findViewById(R.id.location_header_lat);
            TextView headerLon = (TextView) headerView.findViewById(R.id.location_header_lon);
            TextView headerLocationName = (TextView) headerView.findViewById(R.id.location_header_text);
            if (lat == -1 || lon == -1){
                headerLat.setText("Waiting for Location");
                headerLon.setText("Waiting for Location");
            }
            else{
                headerLat.setText(String.valueOf(lat));
                headerLon.setText(String.valueOf(lon));
            }
            headerLocationName.setText(name);
        }
        else {Log.w("headerView","does not exist");}
    }
}