package social.media.meeting.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import social.media.meeting.R;

public class livechat extends AppCompatActivity {
    EditText chat;
    String chatpartner;
    ListView chatlist;
    private FirebaseListAdapter<chatMessage> adapter;
    private String TAG = "LiveChat";
    ArrayList<chatMessage> array_chat = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livechat);
        chatlist = (ListView)findViewById(R.id.chat_list);
        chat = (EditText)findViewById(R.id.chat);
        Button send = (Button)findViewById(R.id.send);
        Intent intent = getIntent();
        chatpartner = intent.getStringExtra("Partner");
        init();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("LiveChat/"+ chatpartner +" vs "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.push().setValue(new chatMessage(chat.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(),String.valueOf(new Date().getTime()))
                        );
                chat.setText("");
            }
        });
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("LiveChat")
                .limitToLast(100);

        FirebaseRecyclerOptions<chatMessage> options =
                new FirebaseRecyclerOptions.Builder<chatMessage>()
                        .setQuery(query, chatMessage.class)
                        .build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<chatMessage, ChatHolder>(options) {
            @Override
            protected void onBindViewHolder(ChatHolder chatHolder, int i, chatMessage chatMessage) {

            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat, parent, false);

                return new ChatHolder(view);
            }

        };
    }

    private void init() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference post_Ref = database.getReference("LiveChat/"+ chatpartner +" vs "+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


        post_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    array_chat.clear();
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);

                        try {
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String message_author = userData.get("Title").toString();
                            String message_content = userData.get("Description").toString();
                            String message_created = userData.get("Created_Date").toString();
                            chatMessage message = new chatMessage(message_author, message_content, message_created);
                            array_chat.add(message);
                        } catch (Exception cce) {
                            Log.e(TAG, cce.toString());
                        }

                    }
                    refresh();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
    }

    private void refresh() {

        chatlist.setAdapter();
    }
}
