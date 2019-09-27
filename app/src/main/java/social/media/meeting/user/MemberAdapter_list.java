package social.media.meeting.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filterable;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import social.media.meeting.R;


public class MemberAdapter_list extends ArrayAdapter <Member> implements Filterable {


    ArrayList<Member> array_all_members = new ArrayList<>();
    Member member;
    CheckBox hire;
    public MemberAdapter_list(Context context, int textViewResourceId, ArrayList<Member> objects) {
        super(context, textViewResourceId, objects);
        array_all_members = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_user_list, null);
        TextView user_name = (TextView) v.findViewById(R.id.user_name);
        TextView user_email = (TextView) v.findViewById(R.id.user_email);
        TextView user_gender = (TextView) v.findViewById(R.id.user_gender);
        TextView user_designation = (TextView)v.findViewById(R.id.user_designation);
        RoundedImageView user_photo = (RoundedImageView) v.findViewById(R.id.roundedImageViewLovesItemLovesImage);
        String userName = array_all_members.get(position).getName();
        String userEmail = array_all_members.get(position).getEmail();
        String userGender = array_all_members.get(position).getGender();
        String userDesignation = array_all_members.get(position).getArea();

        user_name.setText(userName);
        user_email.setText(userEmail);
        user_gender.setText(userGender);
        user_designation.setText(userDesignation);

        String base64photo = array_all_members.get(position).getPhoto();
        if (base64photo.equals("")) {
            if (array_all_members.get(position).getGender().equals("Male"))
                user_photo.setImageResource(R.drawable.man_dummy);
            else
                user_photo.setImageResource(R.drawable.woman_dummy);

        } else {
            String imageDataBytes = base64photo.substring(base64photo.indexOf(",") + 1);
            InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            user_photo.setImageBitmap(bitmap);
        }
        return v;
    }

}
