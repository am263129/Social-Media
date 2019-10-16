package social.media.meeting.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import social.media.meeting.R;
import social.media.meeting.Util.Global;

public class chat_adapter extends ArrayAdapter<chat> {

    ArrayList<chat> array_chat = new ArrayList<>();
    char member;
    CheckBox hire;
    public chat_adapter(Context context, int textViewResourceId, ArrayList<chat> objects) {
        super(context, textViewResourceId, objects);
        array_chat = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String chat_sender = array_chat.get(position).getChat_sender();
        if (chat_sender.equals(Global.current_user_name)) {
            v = inflater.inflate(R.layout.item_mine_message, null);

            TextView message_content = (TextView) v.findViewById(R.id.message);
            message_content.setText(array_chat.get(position).getChat_content());

        } else {
            v = inflater.inflate(R.layout.item_other_message, null);

            TextView message_content = (TextView) v.findViewById(R.id.message);
            message_content.setText(array_chat.get(position).getChat_content());
        }

        return v;
    }
}
