package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by jellenberger on 5/16/15.
 */
public class NewStoryTab extends Fragment {

    private View myView;
    private DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_new_story, container, false);
        Bundle args = getArguments();
        final int position = args.getInt("position", 0);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_story_fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TODO: Implement saving post from tab "+String.valueOf(position),Toast.LENGTH_LONG).show();
            }
        });

        final float scale = view.getResources().getDisplayMetrics().density;
        TextView contentView = (TextView) view.findViewById(R.id.new_card_content);
        TextView titleView = (TextView) view.findViewById(R.id.new_card_title);
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
