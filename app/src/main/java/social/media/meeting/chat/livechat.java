package social.media.meeting.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.ui.ImeHelper;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import social.media.meeting.R;

public class livechat extends AppCompatActivity {
    EditText chat;
    String chatpartner;
    ListView chatlist;
    private FirebaseListAdapter<Chat> adapter;
    private String TAG = "LiveChat";
    ArrayList<Chat> array_chat = new ArrayList<>();
    @NonNull
    protected static final Query sChatQuery =
            FirebaseDatabase.getInstance().getReference().child("chats").limitToLast(50);

    RecyclerView mRecyclerView;
    Button mSendButton;
    EditText mMessageEdit;
    TextView mEmptyListMessage;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_livechat);
            mRecyclerView = (RecyclerView)findViewById (R.id.messagesList);
            mSendButton = (Button)findViewById(R.id.sendButton);
            mMessageEdit = (EditText)findViewById(R.id.messageEdit);
            mEmptyListMessage = (TextView)findViewById(R.id.emptyTextView);
            mSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSendClick();
                }
            });
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        @Override
        public void onStart() {
            super.onStart();
            if (isSignedIn()) { attachRecyclerViewAdapter(); }
        }

        @Override
        protected void onStop() {
            super.onStop();
        }

        private boolean isSignedIn() {
            return FirebaseAuth.getInstance().getCurrentUser() != null;
        }

        private void attachRecyclerViewAdapter() {
            final RecyclerView.Adapter adapter = newAdapter();

            // Scroll to bottom on new messages
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    mRecyclerView.smoothScrollToPosition(adapter.getItemCount());
                }
            });

            mRecyclerView.setAdapter(adapter);
        }


        public void onSendClick() {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String name = "User " + uid.substring(0, 6);

            onAddMessage(new Chat(name, mMessageEdit.getText().toString(), uid));

            mMessageEdit.setText("");
        }

        @NonNull
        protected RecyclerView.Adapter newAdapter() {
            FirebaseRecyclerOptions<Chat> options =
                    new FirebaseRecyclerOptions.Builder<Chat>()
                            .setQuery(sChatQuery, Chat.class)
                            .setLifecycleOwner(this)
                            .build();

            return new FirebaseRecyclerAdapter<Chat, ChatHolder>(options) {
                @Override
                public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new ChatHolder(LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.message, parent, false));
                }

                @Override
                protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {
                    holder.bind(model);
                }

                @Override
                public void onDataChanged() {
                    // If there are no chat messages, show a view that invites the user to add a message.
                    mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                }
            };
        }

        protected void onAddMessage(@NonNull Chat chat) {
            sChatQuery.getRef().push().setValue(chat, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError error, DatabaseReference reference) {
                    if (error != null) {
                        Log.e(TAG, "Failed to write message", error.toException());
                    }
                }
            });
        }
    }