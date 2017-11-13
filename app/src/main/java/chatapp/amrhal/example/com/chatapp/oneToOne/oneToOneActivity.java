package chatapp.amrhal.example.com.chatapp.oneToOne;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chatapp.amrhal.example.com.chatapp.ChatAdaptor;
import chatapp.amrhal.example.com.chatapp.Data;
import chatapp.amrhal.example.com.chatapp.MainActivity;
import chatapp.amrhal.example.com.chatapp.R;

import static chatapp.amrhal.example.com.chatapp.MainActivity.ANONYMOUS;
import static chatapp.amrhal.example.com.chatapp.MainActivity.RC_SIGN_IN;

public class oneToOneActivity extends AppCompatActivity {

    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 40;
    public static final int RC_SIGN_IN = 1;
    private static final String TAG = "MainActivity";
    private static final int RC_PHOTO_PICKER = 2;

    private ListView mMessageListView;
    private ChatAdaptor mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private String mUsername;

    private FirebaseDatabase mFireBaseDatabase;
    private DatabaseReference mMassegesDatabaseRefrances;
    private ChildEventListener childEventListener;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one);
        mUsername = ANONYMOUS;

        // Initialize references to views
        initViews();
        // Initialize message ListView and its adapter
        List<Data> friendlyMessagesList = new ArrayList<>();
        mMessageAdapter = new ChatAdaptor(this, R.layout.item_message, friendlyMessagesList); //(context, resource, objects)
        mMessageListView.setAdapter(mMessageAdapter); // listview

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mFireBaseDatabase = FirebaseDatabase.getInstance(); // for get instance
        mFirebaseStorage = FirebaseStorage.getInstance();
        mfirebaseAuth = FirebaseAuth.getInstance();

        mMassegesDatabaseRefrances = mFireBaseDatabase.getReference().child("messages"); // for get part of instance of this child
        mChatPhotosStorageReference = mFirebaseStorage.getReference().child("chat_photos");

        Log.d("tag ", mMassegesDatabaseRefrances.toString());

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Complete action using"), RC_PHOTO_PICKER);
            }
        });


        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)}); // 7arof maximum

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                Data friendlyUploadMessage = new Data(mMessageEditText.getText().toString(), mUsername, null);
                mMassegesDatabaseRefrances.push().setValue(friendlyUploadMessage); // han7ot el object da fe database
                // Clear input box
                mMessageEditText.setText("");
            }
        });

        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
        );


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mfirebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(oneToOneActivity.this, "you are signed in successfully", Toast.LENGTH_LONG).show();
                    onSignedInInitialize(user.getDisplayName()); // harga3 name of user we fel method ha = be mName ele hia ra7a lel adaptor

                } else {

                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(providers) //deprecated in oreo
                                    .build(),
                            RC_SIGN_IN);
                }

            }

        };
    }

    private void onSignedInInitialize(String name) { //ha3ady el name el gay men firebase lel variable mUsername
        mUsername = name;
        attachDatabaseReadListener(); // de bta3et retrieve el data
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //  Toast.makeText(MainActivity.this, "onChild Added triggred", Toast.LENGTH_LONG).show();
                    // bas lazem tecon nafs el data benfes el names for ex hena name we text we fe data base nafs el klam
                    // how beakhod el data men firebase weyrga3aha fe object FriendlyMessage.class
                    Data friendlyDownLoadMessage = dataSnapshot.getValue(Data.class);
                    mMessageAdapter.add(friendlyDownLoadMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //  Toast.makeText(MainActivity.this, "onChild Changed triggred", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //  Toast.makeText(MainActivity.this, "onChild Removed triggred", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    //   Toast.makeText(MainActivity.this, "onChild Moved triggred", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //   Toast.makeText(MainActivity.this, "on Cancelled triggred", Toast.LENGTH_LONG).show();
                }
            };
            mMassegesDatabaseRefrances.addChildEventListener(childEventListener);
        }

    }

    private void onSignedOutCleanup() { //lamaA3mel signout lazem a clear 3ashan may7salesh conflict
        mUsername = ANONYMOUS; //
        mMessageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        if (childEventListener != null) {
            mMassegesDatabaseRefrances.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(oneToOneActivity.this, "RESULT_OK", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(oneToOneActivity.this, "RESULT_CANCELED", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            // Set the download URL to the message box, so that the user can send it to the database
                            Data friendlyMessage = new Data("test", mUsername, downloadUrl.toString());
                            mMassegesDatabaseRefrances.push().setValue(friendlyMessage);
                        }

                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mAuthStateListener); //owl ma yfta7 el app yt check
    }

    @Override
    protected void onPause() {
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mAuthStateListener);
        mMessageAdapter.clear();
        detachDatabaseReadListener();
    }

    private void initViews() {
        mProgressBar = findViewById(R.id.progressBar);
        mMessageListView = findViewById(R.id.messageListView);
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        mMessageEditText = findViewById(R.id.messageEditText);
        mSendButton = findViewById(R.id.sendButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                // mfirebaseAuth.signOut(); //bet3mel sign out bardo
                AuthUI.getInstance().signOut(this);//signOut
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
