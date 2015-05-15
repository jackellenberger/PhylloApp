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

        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vTitle;

        public StoryViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vTitle = (TextView) v.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemCount() {
        return storyInfoList.size();
    }

    @Override
    public void onBindViewHolder(StoryViewHolder storyViewHolder, int i) {
        ClassStoryInfo ci = storyInfoList.get(i);
        storyViewHolder.vName.setText(ci.name);
        storyViewHolder.vSurname.setText(ci.surname);
        storyViewHolder.vEmail.setText(ci.email);
        storyViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);

        return new StoryViewHolder(itemView);
    }

}
