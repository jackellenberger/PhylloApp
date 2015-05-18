package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AdapterSlidingTab extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when AdapterSlidingTab is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the AdapterSlidingTab is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public AdapterSlidingTab(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if (Titles[0].equals("User")){
            if(position == 0) // if the position is 0 we are returning the First tab
            {
                return new MainUserTab();
            }
            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                MainLocationTab MainLocationTab = new MainLocationTab();
                return MainLocationTab;
            }
        }
        else /*if (Titles[0].equals("Tip"))*/ {
            NewStoryTab newStory = new NewStoryTab();
            Bundle args = new Bundle();
            args.putInt("position",position);
            newStory.setArguments(args);
            return newStory;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}