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

import com.melnykov.fab.FloatingActionButton;

import java.sql.SQLException;
import java.util.List;

public class MainUserTab extends Fragment {

    private DrawerLayout mDrawerLayout;
    private UserStoryDb userDb;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View myView = inflater.inflate(R.layout.main_user_tab_content,container,false);
        //return myView;

        final View view = inflater.inflate(R.layout.main_user_tab_content, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.user_content_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.user_fab);
        fab.attachToRecyclerView(recyclerView);

        userDb = new UserStoryDb(this.getActivity());
        try {
            userDb.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final AdapterStoryRecycler adapter = new AdapterStoryRecycler(generateUserData());
                c.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
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
    private List<ClassStoryInfo> generateUserData() {

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