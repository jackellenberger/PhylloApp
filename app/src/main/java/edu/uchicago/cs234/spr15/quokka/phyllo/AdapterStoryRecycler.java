package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jellenberger on 5/14/15.
 */

public class AdapterStoryRecycler extends RecyclerView.Adapter<AdapterStoryRecycler.StoryViewHolder>{

    private List<ClassStoryInfo> storyInfoList;
    static private View viewContext;

    public AdapterStoryRecycler(List<ClassStoryInfo> storyInfoList) {
        this.storyInfoList = storyInfoList;
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView vTitle;
        protected TextView vSubTitle;
        protected FlowLayout vTags; //RecyclerView in the future?? ListView??
        protected TextView vContent;

        public StoryViewHolder(View v) {
            super(v);
            viewContext = v;
            vTitle = (TextView) v.findViewById(R.id.card_title);
            vSubTitle = (TextView) v.findViewById(R.id.card_subtitle);
            vTags = (FlowLayout) v.findViewById(R.id.tag_flow_layout);
            vContent = (TextView) v.findViewById(R.id.card_content);
        }
    }

    @Override
    public int getItemCount() {
        return storyInfoList.size();
    }

    public ClassStoryInfo getItem(int position){
        return storyInfoList.get(position);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder storyViewHolder, int i) {
        ClassStoryInfo csi = storyInfoList.get(i);
        storyViewHolder.vTitle.setText(csi.getTitle());
        Timestamp ts = new Timestamp(csi.getTimestamp());
        storyViewHolder.vSubTitle.setText("From "+csi.getOriginalPoster()+" on "+ ts.toString().split("\\.")[0]);
        //storyViewHolder.vTags.setText(csi.getTagList()[0]);
        String[] tags = csi.getTagList();
        int tagNum = 0;
        /*android:id="@+id/card_tag"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="4dp"
        android:layout_marginEnd="8dp"
        android:textAllCaps="true"
        android:text="Add a Tag!"
        android:background="@color/background_material_light"
        style="?android:attr/textAppearanceButton"*/
        Resources r = viewContext.getContext().getResources();
        int px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        int px4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
        int px8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        for (String tag : tags){
            TextView newView = new TextView(viewContext.getContext());
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, px2, px8, px2);
            newView.setLayoutParams(params);
            newView.setId(tagNum);
            newView.setPadding(px4,px2,px4,px2);
            newView.setAllCaps(true);
            newView.setTextColor(Color.BLACK);
            newView.setText(tag);
            newView.setBackgroundColor(r.getColor(R.color.background_material_light));

            tagNum += 1;
            storyViewHolder.vTags.addView(newView);
        }
        if (!csi.getType().equals("tip")) {
            storyViewHolder.vContent.setText(csi.getContent());
        }
        else{ storyViewHolder.vContent.setVisibility(View.GONE); }
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_cardview_content, parent, false);

        return new StoryViewHolder(itemView);
    }

}
