package social.media.meeting.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import social.media.meeting.R;

public class chatAdapter extends ArrayAdapter<chatMessage> implements Filterable {


    ArrayList<chatMessage> array_chatMessages = new ArrayList<chatMessage>();
    chatMessage chatMessage;
    public chatAdapter(Context context, int textViewResourceId, ArrayList<chatMessage> objects) {
        super(context, textViewResourceId, objects);
        array_chatMessages = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_chat, null);
        LinearLayout chat_back = (LinearLayout)v.findViewById(R.id.chat_back);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);



        if (!array_chatMessages.get(position).getChatAuthor().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
            params.gravity = Gravity.LEFT;
            chat_back.setLayoutParams(params);
            chat_back.setBackgroundResource(R.drawable.button_previous_focus);
        }
        else {
            params.gravity = Gravity.RIGHT;
            chat_back.setLayoutParams(params);
        }
        TextView chat_content = (TextView)v.findViewById(R.id.chat_content);
        String content = array_chatMessages.get(position).getChatContent();
        chat_content.setText(content);
        return v;

    }
}