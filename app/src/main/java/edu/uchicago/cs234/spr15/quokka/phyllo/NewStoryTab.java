package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by jellenberger on 5/16/15.
 */
public class NewStoryTab extends Fragment {

    private View myView;
    private DrawerLayout mDrawerLayout;
    private UserStoryDb userDB;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_new_story, container, false);
        Bundle args = getArguments();
        final int position = args.getInt("position", 0);
        final TextView contentView = (TextView) view.findViewById(R.id.new_card_content);
        final TextView titleView = (TextView) view.findViewById(R.id.new_card_title);
        final TextView tagView = (TextView) view.findViewById(R.id.new_card_tag);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_story_fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String type;
                if (position == 0) {type = "tip";}
                else if (position == 1) {type = "link";}
                else {type = "longform";}

                Date date= new Date();
                long timestamp = date.getTime();

                //TODO: save information about original poster
                String poster = "Current User";

                //TODO: allow user to create tags
                String[] tags = new String[] {String.valueOf(tagView.getText())};

                userDB = new UserStoryDb(getActivity());
                try{
                    userDB.open();
                } catch (SQLException e){
                    e.printStackTrace();
                }

                userDB.createStory(type, String.valueOf(titleView.getText()), String.valueOf(contentView.getText()), timestamp, poster, 0, tags);

                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.user_content_recycler);
                recyclerView.setAdapter(new AdapterStoryRecycler(userDB.getAllStories()));
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        final float scale = view.getResources().getDisplayMetrics().density;
        if (position == 0){
            contentView.setHeight(0);
            contentView.setVisibility(View.INVISIBLE);
            titleView.setHint("Enter Tip (250 char)");
        }
        else if (position == 1){
            contentView.setHint("Enter URL");
        }
        else/*(position == 2)*/{
            contentView.setMinimumHeight((int) (250 * scale + 0.5f));
            contentView.setHint("Enter Longform (10,000 chars)");

        }
        final FragmentActivity c = getActivity();

        return view;

    }
}
