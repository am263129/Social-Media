package social.media.meeting.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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

public class public_chat_adapter extends ArrayAdapter<chat> {

    ArrayList<chat> array_chat = new ArrayList<>();
    char member;
    CheckBox hire;
    public public_chat_adapter(Context context, int textViewResourceId, ArrayList<chat> objects) {
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
            v = inflater.inflate(R.layout.item_public_mine_message, null);
            TextView message_content = (TextView) v.findViewById(R.id.message);
            message_content.setText(array_chat.get(position).getChat_content());
            RoundedImageView avatar = (RoundedImageView)v.findViewById(R.id.public_chat_avatar);
            setAvatar(avatar,position);

        } else {
            v = inflater.inflate(R.layout.item_public_other_message, null);
            TextView message_content = (TextView) v.findViewById(R.id.message);
            message_content.setText(array_chat.get(position).getChat_content());
            RoundedImageView avatar = (RoundedImageView)v.findViewById(R.id.public_chat_avatar);
            setAvatar(avatar,position);

        }

        return v;
    }

    public void setAvatar(RoundedImageView avatar, int position){
        String base64photo = null;
        for (int i = 0; i < Global.All_members.size(); i ++){
            if (Global.All_members.get(i).getName().equals(array_chat.get(position).getChat_sender())) {
                base64photo = Global.All_members.get(i).getPhoto();
                break;
            }
        }
        try {
            if (base64photo == null)
                avatar.setImageResource(R.drawable.dummy_user);
            else {
                String imageDataBytes = base64photo.substring(base64photo.indexOf(",") + 1);
                InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                avatar.setImageBitmap(bitmap);
            }
        }
        catch (Exception E){
            Log.e("No Avatar",E.toString());
        }
    }
}
