package social.media.meeting.Util;

import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import social.media.meeting.chat.chat;
import social.media.meeting.user.Member;

public class Global {
    public static String current_user_email;
    public static String current_user_name;
    public static String current_user_photo;
    public static ArrayList<Object> array_all_chats = new ArrayList<Object>();
    public static ArrayList<Object> array_my_chats = new ArrayList<Object>();
    public static ArrayList<String> chat_key = new ArrayList<String>();
    public static ArrayList<Member> All_members = new ArrayList<Member>();
    public static ArrayList<chat> array_public_chats =  new ArrayList<chat>();

    public static String getToday() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        return currentTime.toString();
    }
    public static class FishNameComparator implements Comparator<chat>
    {
        public int compare(chat left, chat right) {
            return left.getChat_id().compareTo(right.getChat_id());
        }
    }
}
