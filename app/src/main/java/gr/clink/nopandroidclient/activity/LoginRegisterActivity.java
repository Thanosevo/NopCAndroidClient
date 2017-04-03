package gr.clink.nopandroidclient.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.Window;

import gr.clink.nopandroidclient.R;
import gr.clink.nopandroidclient.fragment.LoginFragment;
import gr.clink.nopandroidclient.fragment.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity {

    private Handler mHandler;
    private int currentFragment = R.id.navigation_login;
    private void loadFragment(final Fragment fr){
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.fragmentContainer, fr, null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_login:
                    if(currentFragment != R.id.navigation_login) {
                        currentFragment = R.id.navigation_login;
                        loadFragment(new LoginFragment());
                    }
                    return true;
                case R.id.navigation_register:
                    if(currentFragment != R.id.navigation_register) {
                        currentFragment = R.id.navigation_register;
                        loadFragment(new RegisterFragment());
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

           //set the transition
            Transition ts = new Explode();
            ts.setDuration(5000);
            getWindow().setEnterTransition(ts);
            getWindow().setExitTransition(ts);
        }
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

        setContentView(R.layout.activity_login_register);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new LoginFragment());
    }




}
