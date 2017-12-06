package com.ttl.project.thetalklist;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class Registration extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        getSupportFragmentManager().beginTransaction().replace(R.id.registration_viewpager,new Tablayout_with_viewpager()).commit();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentStack fragments=FragmentStack.getInstance();

        if (fragments.size()>0) {
            Fragment fragment = fragments.pop();
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                getSupportFragmentManager().executePendingTransactions();
                getSupportFragmentManager().beginTransaction().replace(R.id.registration_viewpager, fragment, fragment.getClass().toString()).commit();
            }
        }else {
            if (fragments.size() > 0) {

                if (fragments.size() == 1) {
                    Toast.makeText(getApplicationContext(), "Please press once to exit..", Toast.LENGTH_SHORT).show();
                }

            } else {
                fragments.setSize(0);
                TabBackStack.getInstance().setTabPosition(0);
                if (getSharedPreferences("loginStatus", MODE_PRIVATE).getString("LoginWay", "").equalsIgnoreCase("FacebookLogin")) {
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    LoginManager.getInstance().logOut();

                    AccessToken.setCurrentAccessToken(null);
                }
                finish();

                new Login().onBackPressed();
                new SplashScreen().onBackPressed();
            }
        }
    }
}
