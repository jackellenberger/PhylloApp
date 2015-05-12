package edu.uchicago.cs234.spr15.quokka.phyllo;

/**
 * Created by Jack on 5/12/2015.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class userStatisticsFragment extends Fragment {

    //TOOLBAR / APPBAR
    private Toolbar toolbar;
    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.user_statistics, container, false);

        //TODO: Figure out how to manage the app bar

        return inflatedView;
    }
}