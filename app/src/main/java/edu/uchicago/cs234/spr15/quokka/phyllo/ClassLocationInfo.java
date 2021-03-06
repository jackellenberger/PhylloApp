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
    protected double latitude;
    protected double longitude;
    protected double radius;
    protected long locationID;
    protected String[] tags;
    protected Location locationObject;

    public String getLocationName(){
        return locationName;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getRadius(){
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
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public void setRadius(double radius){
        this.radius = radius;
    }
    public void setLocationId(long locationID){
        this.locationID = locationID;
    }
    public void setLocationObject(Location newLocationObject){
        this.locationObject = newLocationObject;
    }

    public static void init(ClassLocationInfo loc){
        loc.setLocationId(0);
        loc.setLocationObject(null);
        loc.setLatitude(-1);
        loc.setLongitude(-1);
        loc.setLocationName("Waiting for Location");
        loc.setRadius(5.0);
    }


}
