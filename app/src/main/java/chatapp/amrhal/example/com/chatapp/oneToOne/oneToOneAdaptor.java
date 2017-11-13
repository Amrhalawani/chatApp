package chatapp.amrhal.example.com.chatapp.oneToOne;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by hp on 13/11/2017.
 */

public class oneToOneAdaptor extends ArrayAdapter<oneToOneData> {

    public oneToOneAdaptor(@NonNull Context context, int resource, @NonNull List<oneToOneData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        return super.getView(position, convertView, parent);
    }
}

