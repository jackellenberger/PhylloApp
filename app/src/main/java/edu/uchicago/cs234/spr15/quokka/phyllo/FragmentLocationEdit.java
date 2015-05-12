package edu.uchicago.cs234.spr15.quokka.phyllo;

/**
 * Created by jellenberger on 5/12/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLocationEdit extends Fragment {

    //TOOLBAR / APPBAR
    private Toolbar toolbar;
    private View inflatedView;
    private DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fraglayout_loc_edit, container, false);

        toolbar = (Toolbar) inflatedView.findViewById(R.id.app_bar);
        toolbar.setTitle("Edit Location");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getActivity().getSupportFragmentManager().popBackStack(); }
        });
        return inflatedView;
    }
}

