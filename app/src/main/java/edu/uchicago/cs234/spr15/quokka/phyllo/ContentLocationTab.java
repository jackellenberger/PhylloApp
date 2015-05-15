package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContentLocationTab extends Fragment {

    private View myView;
    private DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_location_tab_content, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.location_content_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final AdapterStoryRecycler adapter = new AdapterStoryRecycler(generateLocalData(10));
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        return view;

    }

    //TODO: fill generateLocalData with functions that will query external for stories
    private List<ClassStoryInfo> generateLocalData(int size) {

        List<ClassStoryInfo> result = new ArrayList<ClassStoryInfo>();
        for (int i=1; i <= size; i++) {
            ClassStoryInfo csi = new ClassStoryInfo();
            if ((i+1)%3==0) {
                csi.type="tip";
                csi.title="This is tip number " + (i/3);
                csi.content="You shouldn't be able to see this!!1!";
                csi.timestamp="Now";
                csi.originalPoster="The Quokka In The Sky";
                csi.locationOfOrigin="current location";
                csi.tags = new String[]{"tweet"};
            }
            else if (i%3 == 0) {
                csi.type="longform";
                csi.title="This is longform number " + (i/3);
                csi.content= getString(R.string.filler_text);
                csi.timestamp="Now";
                csi.originalPoster="The Quokka In The Sky";
                csi.locationOfOrigin="current location";
                csi.tags = new String[]{"Latin filler"};
            }
            else{
                csi.type="link";
                csi.title="This is link number " + (i/3);
                csi.content="https://cs.uchicago.edu";
                csi.timestamp="Now";
                csi.originalPoster="The Quokka In The Sky";
                csi.locationOfOrigin="current location";
                csi.tags = new String[]{"uchicago"};
            }
            result.add(csi);
        }
        return result;
    }

}