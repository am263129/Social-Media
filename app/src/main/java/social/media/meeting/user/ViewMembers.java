package social.media.meeting.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import social.media.meeting.R;
import social.media.meeting.Util.Global;
import social.media.meeting.chat.chat_room;


public class ViewMembers extends AppCompatActivity {
    static ArrayList<Member> array_all_members = new ArrayList<Member>();
    static String CHAT_STATUS = "Chat_Status";
    public static ViewMembers self;
    static ListView all_members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        all_members = (ListView)findViewById(R.id.list_all_members);
        self = this;
        array_all_members = Global.All_members;
        refresh();
        all_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(array_all_members.get(i).getName().equals(Global.current_user_name))
                    Toast.makeText(ViewMembers.this,"It's me!", Toast.LENGTH_SHORT).show();
                else {
                    reset_read_status(array_all_members.get(i).getName());
                    Intent chat = new Intent(ViewMembers.this, chat_room.class);
                    chat.putExtra("call", i);
                    startActivity(chat);
                }

            }
        });
    }


    /**
     * reset the data of Firebase after read chat message
     * @param name: name of chat message sender.
     */
    private void reset_read_status(String name) {
        String key="";
        ArrayList<String> array_sub_key = new ArrayList<String>();
        if (Global.array_unread_chat_sender !=null) {
            for (int i = 0; i < Global.array_unread_chat_sender.size(); i++) {
                if (Global.array_unread_chat_sender.get(i).getSender().equals(name)) {
                    key = Global.array_unread_chat_sender.get(i).getKey();
                    array_sub_key = Global.array_unread_chat_sender.get(i).getSubkey();
                    Global.array_unread_chat_sender.remove(i);
                }
            }
            refresh();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            for (int j = 0; j < array_sub_key.size(); j++) {
                String id = "chats/" + key + "/" + array_sub_key.get(j) + "/";
                DatabaseReference myRef = database.getReference(id + CHAT_STATUS);
                myRef.setValue("read");

            }
        }

    }

    /**
     * Refresh the list after change array.
     * from anywhere.
     */
    public static void refresh(){
        MemberAdapter_list adapter = new MemberAdapter_list(ViewMembers.getInstance(),R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter);
    }

    public static ViewMembers getInstance(){
        return self;
    }
}
