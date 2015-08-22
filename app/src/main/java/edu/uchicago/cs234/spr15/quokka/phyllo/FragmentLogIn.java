package edu.uchicago.cs234.spr15.quokka.phyllo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

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
                logIn(loginAttemptUser);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassUserInfo creationAttemptUser = fieldsToUserInfo();
                createNewUser(creationAttemptUser);
            }
        });

        return inflatedView;
    }

    private void onLoginSuccess(ClassUserInfo user){
        currentUser = user;
        AdapterLeftDrawerRecycler.setUserHeaderText(currentUser);
        DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getActivity().getSupportFragmentManager().popBackStack();
    }
    private ClassUserInfo fieldsToUserInfo(){
        ClassUserInfo enteredInfo = new ClassUserInfo();
        enteredInfo.setUserName(usernameField.getText().toString());
        enteredInfo.setPassword(passwordField.getText().toString());
        return enteredInfo;
    }

    public static ClassUserInfo getCurrentUser(){
        if (currentUser != null) {
            return currentUser;
        }
        else{
            return new ClassUserInfo();
        }
    }

    private static final String API_URL = "https://floating-wildwood-9614.herokuapp.com";

    public interface LogInService {
        @POST("/users/new")
        public void createUser(@Body ClassUserInfo user, Callback<String> str);

        @POST("/users/login")
        public void userLogin(@Body ClassUserInfo user, Callback<String> str);
    }

    private void logIn(final ClassUserInfo userInfo){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_URL)
                .build();
        LogInService service = restAdapter.create(LogInService.class);
        service.userLogin(userInfo, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d("User logged in!", s);
                onLoginSuccess(userInfo);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Login failed", error.getMessage());
                TextView loginFailure = (TextView) inflatedView.findViewById(R.id.login_failure_text);
                loginFailure.setVisibility(View.VISIBLE);
                passwordField.setText("");
            }
        });
        return;
    }


    private void createNewUser(final ClassUserInfo userInfo) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(API_URL)
                .build();
        LogInService service = restAdapter.create(LogInService.class);
        service.createUser(userInfo, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d("User created!", s);
                onLoginSuccess(userInfo);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Failed", error.getMessage());
                TextView newUserFailure = (TextView) inflatedView.findViewById(R.id.new_user_failure_text);
                newUserFailure.setVisibility(View.VISIBLE);
                usernameField.setText("");
            }
        });
        return;
    }

    public static void populateUserTags(ClassUserInfo user){
        String username = user.getUserName();
        String[] returnedTags = queryDBforTags(username);
        user.setTags(returnedTags);
    }

    private static String[] queryDBforTags(String username){
        UserStoryDb userDB = MainUserTab.getUserDb();
        Set<String> result = new HashSet<String>();
        List<ClassStoryInfo> stories = userDB.getAllStories();
        for (int i = 0; i < stories.size(); i++) {
            ClassStoryInfo story = stories.get(i);
            String[] tags = story.getTagList();
            for (int j = 0; j < tags.length; i++) {
                result.add(tags[j]);
            }
        }
        String[] res = new String[result.size()];
        res = result.toArray(res);
        return res;
    }

    public static void logOutUser(){
        currentUser = new ClassUserInfo();
        AdapterLeftDrawerRecycler.setUserHeaderText(currentUser);
    }
}
