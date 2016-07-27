package com.example.paul.itimedemologin;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        info = (TextView)findViewById(R.id.info);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        profile = Profile.getCurrentProfile();
                        info.setText(message(profile));
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

        if (AccessToken.getCurrentAccessToken()!=null){
            CharSequence message = "successfully login";
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }else{
            CharSequence message = "not login yet";
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onResume(){
        super.onResume();
        deleteAccessToken();
        deleteAccessProfile();
        profile = Profile.getCurrentProfile();

        if (profile != null){
            info.setText(message(profile));
        }
    }

    private String message(Profile profile){
        StringBuilder stringBuffer = new StringBuilder();
        if (profile != null){
            stringBuffer.append("Welcome ").append(profile.getName());
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        if (data.getExtras()!=null && resultCode == -1){
//            Toast.makeText(getApplicationContext(),(CharSequence)"Successfully login",Toast.LENGTH_SHORT).show();
//        }else if (data.getExtras()!=null && resultCode == 0){
//            Toast.makeText(getApplicationContext(),(CharSequence)"unsuccessfully login",Toast.LENGTH_SHORT).show();
//        }
    }

    // this is a token tracker, will be called when the accessToken changed
    private void deleteAccessToken(){
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    info.setText("User logout");
                }
            }
        };
    }

    private void deleteAccessProfile(){
        ProfileTracker mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if(currentProfile!=null) {
                    info.setText(message(currentProfile));
                }
            }
        };
    }
}
