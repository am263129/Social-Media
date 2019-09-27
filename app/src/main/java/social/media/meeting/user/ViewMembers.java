package social.media.meeting.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import social.media.meeting.R;
import social.media.meeting.Util.Global;


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
    }
}
