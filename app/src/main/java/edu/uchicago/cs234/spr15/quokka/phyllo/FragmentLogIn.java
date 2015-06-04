package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jellenberger on 6/3/15.
 */
public class FragmentLogIn extends Fragment {

    private static ClassUserInfo currentUser;
    private View inflatedView;
    private EditText passwordField;
    private EditText usernameField;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fraglayout_user_login, container, false);
        Button signInButton = (Button) inflatedView.findViewById(R.id.sign_in_button);
        Button newUserButton = (Button) inflatedView.findViewById(R.id.new_user_button);
        usernameField = (EditText) inflatedView.findViewById(R.id.username_field);
        passwordField = (EditText) inflatedView.findViewById(R.id.password_field);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassUserInfo loginAttemptUser = fieldsToUserInfo();
                boolean didSignIn = logIn(loginAttemptUser);
                if (didSignIn){
                    currentUser = loginAttemptUser;
                    DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    TextView loginFailure = (TextView) inflatedView.findViewById(R.id.login_failure_text);
                    loginFailure.setVisibility(View.VISIBLE);
                    passwordField.setText("");
                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassUserInfo creationAttemptUser = fieldsToUserInfo();
                boolean userWasCreated = createNewUser(creationAttemptUser);
                if (userWasCreated){
                    currentUser = creationAttemptUser;
                    DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else{
                    TextView newUserFailure = (TextView) inflatedView.findViewById(R.id.new_user_failure_text);
                    newUserFailure.setVisibility(View.VISIBLE);
                    usernameField.setText("");
                }
            }
        });

        return inflatedView;
    }

    private ClassUserInfo fieldsToUserInfo(){
        ClassUserInfo enteredInfo = new ClassUserInfo();
        enteredInfo.setUserName(usernameField.getText().toString());
        enteredInfo.setPassword(passwordField.getText().toString());
        return enteredInfo;
    }

    public static ClassUserInfo getCurrentUser(){
        return currentUser;
    }

    private boolean logIn(ClassUserInfo userInfo){
        return true;
        //TODO: check credentials
    }
    private boolean createNewUser(ClassUserInfo userInfo){
        return true;
        //TODO: create new user
    }

    public static void populateUserTags(ClassUserInfo user){
        String username = user.getUserName();
        String[] returnedTags = queryDBforTags(username);
        user.setTags(returnedTags);
    }

    private static String[] queryDBforTags(String username){
        UserStoryDb userDB = MainUserTab.getUserDb();
        return new String[] {};
        //TODO: find all tags associated with user
    }
}
