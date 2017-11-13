package chatapp.amrhal.example.com.chatapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ActivityLauncher.openMainActivity(SplashScreen.this);
            }
        };

        new Handler().postDelayed(runnable,3000);
    }
}
