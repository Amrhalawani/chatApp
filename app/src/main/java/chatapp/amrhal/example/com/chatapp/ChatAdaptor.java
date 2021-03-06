package chatapp.amrhal.example.com.chatapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by hp on 12/11/2017.
 */


    public class ChatAdaptor extends ArrayAdapter<Data> {
        public ChatAdaptor(Context context, int resource, List<Data> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
            }

            ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
            ImageView profileImageView = convertView.findViewById(R.id.photoImageView);
            TextView messageTextView = convertView.findViewById(R.id.messageTextView);
            TextView authorTextView = convertView.findViewById(R.id.nameTextView);

            Data message = getItem(position);
            boolean isPhoto = message.getPhotoUrl() != null;
            if (isPhoto) {
                messageTextView.setVisibility(View.GONE);
                photoImageView.setVisibility(View.VISIBLE);
                Glide.with(photoImageView.getContext())
                        .load(message.getPhotoUrl())
                        .into(photoImageView);
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                photoImageView.setVisibility(View.GONE);
                messageTextView.setText(message.getText());
            }
            authorTextView.setText(message.getName());

            return convertView;
        }
    }

