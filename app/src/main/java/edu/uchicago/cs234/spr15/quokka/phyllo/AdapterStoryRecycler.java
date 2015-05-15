package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jellenberger on 5/14/15.
 */

public class AdapterStoryRecycler extends RecyclerView.Adapter<AdapterStoryRecycler.StoryViewHolder>{

    private List<ClassStoryInfo> storyInfoList;

    public AdapterStoryRecycler(List<ClassStoryInfo> storyInfoList) {
        this.storyInfoList = storyInfoList;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vSubTitle;
        protected TextView vTags; //RecyclerView in the future?? ListView??
        protected TextView vContent;

        public StoryViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.card_title);
            vSubTitle = (TextView) v.findViewById(R.id.card_subtitle);
            vTags = (TextView) v.findViewById(R.id.card_tag);
            vContent = (TextView) v.findViewById(R.id.card_content);
        }
    }

    @Override
    public int getItemCount() {
        return storyInfoList.size();
    }


    @Override
    public void onBindViewHolder(StoryViewHolder storyViewHolder, int i) {
        ClassStoryInfo csi = storyInfoList.get(i);
        storyViewHolder.vTitle.setText(csi.title);
        storyViewHolder.vSubTitle.setText("From "+csi.originalPoster+" in "+csi.locationOfOrigin+" on "+ csi.timestamp);
        storyViewHolder.vTags.setText(csi.tags[0]);
        if (!csi.type.equals("tip")) {
            storyViewHolder.vContent.setText(csi.content);
        }
        else{ storyViewHolder.vContent.setVisibility(View.GONE); }
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_cardview_content, parent, false);

        return new StoryViewHolder(itemView);
    }

}
