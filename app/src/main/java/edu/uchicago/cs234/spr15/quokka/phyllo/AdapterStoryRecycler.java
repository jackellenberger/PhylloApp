package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        protected ImageButton vOverflow;

        public StoryViewHolder(View v) {
            super(v);
            viewContext = v;
            vTitle = (TextView) v.findViewById(R.id.card_title);
            vSubTitle = (TextView) v.findViewById(R.id.card_subtitle);
            vTags = (FlowLayout) v.findViewById(R.id.tag_flow_layout);
            vContent = (TextView) v.findViewById(R.id.card_content);
            vOverflow = (ImageButton) v.findViewById(R.id.card_overflow);
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
    public void onBindViewHolder(final StoryViewHolder storyViewHolder, int i) {
        ClassStoryInfo csi = storyInfoList.get(i);
        storyViewHolder.vTitle.setText(csi.getTitle());
        Timestamp ts = new Timestamp(csi.getTimestamp());
        storyViewHolder.vSubTitle.setText("From " + csi.getOriginalPoster() + " on " + ts.toString().split("\\.")[0]);

        String[] tags = csi.getTagList();

        //dp to px conversion
        Resources r = viewContext.getContext().getResources();
        int px2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
        int px4 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
        int px8 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

        if (tags != null && storyViewHolder.vTags.getChildCount() == 0) {
            int tagNum = 0;
            for (String tag : tags) {
                Log.w("Writing Tags",storyViewHolder.vTitle.getText() + " " + tag);
                TextView newView = new TextView(storyViewHolder.vTags.getContext());
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, px2, px4, px2);
                newView.setLayoutParams(params);
                newView.setId(tagNum);
                newView.setPadding(px4, px2, px4, px2);
                newView.setAllCaps(true);
                newView.setTextColor(Color.BLACK);
                newView.setText(tag);
                newView.setBackgroundColor(r.getColor(R.color.background_material_light));

                tagNum += 1;
                storyViewHolder.vTags.addView(newView);
            }
        }
        //remove content view from tips
        if (csi.getType().equals("tip")) { storyViewHolder.vContent.setVisibility(View.GONE);}
        else { storyViewHolder.vContent.setText(csi.getContent()); }

        //hyperlink links
        if(csi.getType().equals("url")){
            storyViewHolder.vContent.setAutoLinkMask(1);
            storyViewHolder.vContent.setMovementMethod(LinkMovementMethod.getInstance());
        }

        //Overflow button menu
        final PopupMenu mPopupMenu = new PopupMenu(viewContext.getContext(), storyViewHolder.vOverflow);
        MenuInflater menuInflater = mPopupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menulayout_card_overflow, mPopupMenu.getMenu());
        storyViewHolder.vOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupMenu.show();
            }
        });
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.card_overflow_delete:
                        int pos = storyViewHolder.getPosition();
                        MainUserTab.getUserDb().deleteStory(storyInfoList.get(pos));
                        MainUserTab.getmRecyclerView().getAdapter().notifyItemRemoved(pos);
                        MainUserTab.getmRecyclerView().getAdapter().notifyDataSetChanged();
                        break;
                    case R.id.card_overflow_share:
                        Toast.makeText(viewContext.getContext(),"Traitor",Toast.LENGTH_SHORT).show();
                        //TODO:implement sharing
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_cardview_content, parent, false);

        return new StoryViewHolder(itemView);
    }

}
