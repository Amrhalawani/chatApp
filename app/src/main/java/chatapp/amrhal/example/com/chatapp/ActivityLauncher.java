package chatapp.amrhal.example.com.chatapp;

import android.content.Context;
import android.content.Intent;


public final class ActivityLauncher {

    public static void openActivity(Context context){
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

}
