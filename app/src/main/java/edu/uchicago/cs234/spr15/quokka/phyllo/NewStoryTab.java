package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.wefika.flowlayout.FlowLayout;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jellenberger on 5/16/15.
 */
public class NewStoryTab extends Fragment {

    private View myView;
    private DrawerLayout mDrawerLayout;
    private UserStoryDb userDB;
    private FlowLayout tagsView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_new_story, container, false);
        Bundle args = getArguments();
        final int position = args.getInt("position", 0);
        final TextView contentView = (TextView) view.findViewById(R.id.new_card_content);
        final ScrollView contentScrollView = (ScrollView) view.findViewById(R.id.new_card_content_scrollview);
        final TextView titleView = (TextView) view.findViewById(R.id.new_card_title);
        tagsView = (FlowLayout) view.findViewById(R.id.new_tag_flow_layout);
        final FragmentActivity mFragmentActivity = getActivity();

        createNewTagView(mFragmentActivity);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_story_fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = String.valueOf(titleView.getText());
                if (title.length() < 2){
                    Toast.makeText(getActivity(),"We're going to need a longer title than that, bud",Toast.LENGTH_LONG).show();
                    return;
                }

                String[] tags = getTagsFromFlowLayout(tagsView);
                if (tags.length == 0){
                    Toast.makeText(getActivity(),"Please add at least one tag",Toast.LENGTH_LONG).show();
                    return;
                }

                String content = String.valueOf(contentView.getText());

                String type;
                if (position == 0) {type = "tip";}
                else if (position == 1) {type = "link";}
                else {type = "longform";}

                Date date= new Date();
                long timestamp = date.getTime();

                //TODO: save information about original poster
                String poster = "Current User";

                userDB = new UserStoryDb(getActivity());
                try{
                    userDB.open();
                } catch (SQLException e){
                    e.printStackTrace();
                }

                userDB.createStory(type, title, content, timestamp, poster, -1.0, -1.0, tags);

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
            //contentView.setMinimumHeight((int) (250 * scale + 0.5f));
            contentView.setHint("Enter Longform (10,000 chars)");
            contentView.setMaxHeight((int) (275 * scale + 0.5f));
        }

        Intent intent = getActivity().getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()))
        {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (url != null) {
                contentView.setText(url);
            }
        }

        return view;
    }

    private void createNewTagView(final FragmentActivity context){
        Resources r = context.getResources();
        int oldTagCount = tagsView.getChildCount();
        int px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        int px4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
        int px8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        EditText newTagView = new EditText(context);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newTagView.setHint(R.string.newTagText);
        params.setMargins(0, px2, px4, px2);
        newTagView.setLayoutParams(params);
        newTagView.setPadding(px4, px2, px4, px2);
        newTagView.setTag((oldTagCount+1));
        newTagView.setTextColor(Color.BLACK);
        newTagView.setBackgroundColor(r.getColor(R.color.background_material_light));

        //if you (double) click a tag, it will give you space to add another tag
        newTagView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == tagsView.getChildCount()){
                    createNewTagView(context);
                }
            }
        });
        tagsView.addView(newTagView);
    }

    private String[] getTagsFromFlowLayout(FlowLayout flowLayout){
        List<String> tagList = new ArrayList<String>();
        int childCount = flowLayout.getChildCount();
        for (int i = 0; i < childCount; i++){
            EditText tagView = (EditText) flowLayout.getChildAt(i);
            String tag = tagView.getText().toString();
            if (!tag.equals(getString(R.string.newTagText))){
                tagList.add(tag);
            }
        }
        return tagList.toArray(new String[tagList.size()]);
    }
}
