package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContentUserTab extends Fragment {

    private DrawerLayout mDrawerLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.main_user_tab_content,container,false);
        //mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, getActivity().findViewById(R.id.right_drawer));

        RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.user_content_recycler);
        return myView;
    }
}