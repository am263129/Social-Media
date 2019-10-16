package social.media.meeting.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import social.media.meeting.R;
import social.media.meeting.Util.Global;
import social.media.meeting.chat.chat_room;


public class ViewMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        ListView all_members = (ListView)findViewById(R.id.list_all_members);
        ArrayList<Member> array_all_members = new ArrayList<Member>();
        array_all_members = Global.All_members;
        MemberAdapter_list adapter = new MemberAdapter_list(this,R.layout.item_user_list,array_all_members);
        all_members.setAdapter(adapter);

        all_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent chat  = new Intent( ViewMembers.this, chat_room.class);
                chat.putExtra("call",i);
                startActivity(chat);
            }
        });
    }
}
