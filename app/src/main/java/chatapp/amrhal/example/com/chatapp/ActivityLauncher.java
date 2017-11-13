package chatapp.amrhal.example.com.chatapp;

import android.content.Context;
import android.content.Intent;

import chatapp.amrhal.example.com.chatapp.oneToOne.oneToOneActivity;


public final class ActivityLauncher {

    public static void openMainActivity(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
    public static void openOnetoOneActivity(Context context){
        Intent i = new Intent(context, oneToOneActivity.class);
        context.startActivity(i);
    }

}
