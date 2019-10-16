package social.media.meeting.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import social.media.meeting.R;
import social.media.meeting.Util.Global;

public class public_chat_room extends AppCompatActivity {

    static ListView chat_view;
    EditText chat_message;
    Button btn_send;
    static public_chat_room self;

    static String CHAT_ID = "Chat_Id";
    static String CHAT_SENDER = "Chat_Sender";
    static String CHAT_CONTENT = "Chat_Content";
    static String CHAT_TYPE = "Chat_Type";
    static String CHAT_CREATED_DATE = "Chat_Created_Date";
    String TAG = "public chat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat_room);
        chat_view = (ListView)findViewById(R.id.chat_list);
        chat_message = (EditText)findViewById(R.id.chat_message);
        btn_send = (Button)findViewById(R.id.btn_send);
        self = this;

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chat_message.getText().toString().equals(""))
                    Toast.makeText(public_chat_room.this,"Invalid Input", Toast.LENGTH_SHORT).show();
                else{
                    FirebaseApp.initializeApp(public_chat_room.this);
                    String sub_id;
                    String chat_id;
                    chat_id = String.valueOf(Global.array_public_chats.size() + 1);
                    Integer limit = chat_id.length();
                    if (chat_id.length()<6)
                        for (int i = 0; i < 6 - limit; i ++){
                            chat_id = "0" + chat_id;
                        }


                    String id = "chats/Public Chat/"+ chat_id + "/";
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(id+ CHAT_ID);
                    myRef.setValue(chat_id);
                    myRef = database.getReference(id+CHAT_SENDER);
                    myRef.setValue(Global.current_user_name);
                    myRef = database.getReference(id+CHAT_CONTENT);
                    myRef.setValue(chat_message.getText().toString());
                    myRef = database.getReference(id+CHAT_TYPE);
                    myRef.setValue("Text");
                    myRef = database.getReference(id+CHAT_CREATED_DATE);
                    myRef.setValue(Global.getToday());

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is:" + value);
                            chat_message.setText("");
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG,"Failed to rad value ", databaseError.toException());
                            Toast.makeText(public_chat_room.this,"Can't Send Message. Please check Internet Connection.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        reset_chat();
    }

    public static void reset_chat(){
        public_chat_adapter adapter = new public_chat_adapter(public_chat_room.getInstance(),R.layout.item_mine_message, Global.array_public_chats);
        chat_view.setAdapter(adapter);
        chat_view.setSelection(adapter.getCount() - 1);
    }
    public static public_chat_room getInstance(){
        return self;
    }
}
