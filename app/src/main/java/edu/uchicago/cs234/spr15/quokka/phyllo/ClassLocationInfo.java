package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by jellenberger on 5/14/15.
 */
public class ClassLocationInfo {
    protected String locationName;
    protected String latitude;
    protected String longitude;
    protected long radius;
    protected long locationID;
    protected String[] tags;
    protected Location locationObject;

    public String getLocationName(){
        return locationName;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public long getRadius(){
        return radius;
    }
    public long getLocationId(){
        return locationID;
    }
    public String[] getTags(){
        return new String[0];
        //TODO: get Tag distributions
    }
    public Location getLocationObject(){
        return locationObject;
    }

    public void setLocationName(String locationName){
        this.locationName = locationName;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public void setRadius(long radius){
        this.radius = radius;
    }
    public void setLocationId(long locationID){
        this.locationID = locationID;
    }
    public void setLocationObject(Location newLocationObject){
        this.locationObject = newLocationObject;
    }


}
