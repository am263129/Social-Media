package social.media.meeting.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.core.app.RemoteInput;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import social.media.meeting.R;
import social.media.meeting.Util.Global;
import social.media.meeting.chat.chat_room;
import social.media.meeting.chat.unread_chat;
import social.media.meeting.user.ViewMembers;

public class call_service extends Service {
    String CHANNEL_ID = "0001";
    String TAG = "Receiver Service";

    static String CHAT_ID = "Chat_Id";
    static String CHAT_SENDER = "Chat_Sender";
    static String CHAT_CONTENT = "Chat_Content";
    static String CHAT_TYPE = "Chat_Type";
    static String CHAT_CREATED_DATE = "Chat_Created_Date";
    static String CHAT_STATUS = "Chat_Status";
    static String CHAT_NOTIFICATION = "Chat_Notification";

    private static final String KEY_TEXT_REPLY = "key_text_reply";
    public call_service() {
                receiver();
    }

    /**
     * check new chat and make notification.
     *
     */
    private void receiver() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Global.array_all_chats.clear();
                Global.array_my_chats.clear();
                Global.chat_key.clear();
                Global.array_unread_chat_sender.clear();
                if (dataSnapshot.exists()){
                    try {
                        HashMap<String,Object> datamap = (HashMap<String ,Object>) dataSnapshot.getValue();

                        for(String key: datamap.keySet()) {
                            String test = key;
                            Log.e(TAG, key);
                            Object data = datamap.get(key);
                            HashMap<String, Object> chat_data = (HashMap<String, Object>)data;
                            Global.array_all_chats.add(data);
                            try {
                                if (key.split("&")[0].equals(Global.current_user_name) || key.split("&")[1].equals(Global.current_user_name)) {
                                    Global.array_my_chats.add(data);
                                    Global.chat_key.add(key);
                                    try
                                    {
                                        ArrayList<String> unread_subkey = new ArrayList<String>();
                                        String unread_chat_sender = null;
                                        String unread_chat_key = key;
                                        try {
                                            for (String sub_key : chat_data.keySet()) {
                                                Object chat_item = chat_data.get(sub_key);
                                                HashMap<String, Object> chat_detail = (HashMap<String, Object>) chat_item;
                                                if (chat_detail.get(CHAT_NOTIFICATION).toString().equals("Unmade")
                                                        && !chat_detail.get(CHAT_SENDER).toString().equals(Global.current_user_name)
                                                        && !Global.chat_room_running) {

                                                    String sender = chat_detail.get(CHAT_SENDER).toString();
                                                    String message = chat_detail.get(CHAT_CONTENT).toString();
                                                    make_notification(sender, message);

                                                    String id = "chats/" + key + "/" + sub_key + "/";
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myRef = database.getReference(id + CHAT_NOTIFICATION);
                                                    myRef.setValue("made");
                                                }

                                                if (!chat_detail.get(CHAT_SENDER).toString().equals(Global.current_user_name)
                                                        && chat_detail.get(CHAT_STATUS).toString().equals("Unread")) {
                                                    unread_subkey.add(sub_key);
                                                    unread_chat_sender = chat_detail.get(CHAT_SENDER).toString();

                                                }
                                            }
                                        }catch (Exception E){}
                                        if (unread_chat_sender!=null && unread_subkey.size()>0) {
                                            unread_chat unread_chat = new unread_chat(unread_chat_sender, unread_chat_key, unread_subkey);
                                            Global.array_unread_chat_sender.add(unread_chat);
                                            try
                                            {
                                                ViewMembers.refresh();
                                            }catch (Exception E){}
                                        }
                                    }catch (Exception E){
                                        Log.e("No status",E.toString());
                                    }
                                }
                            }
                            catch (Exception E){}
                        }
                    }
                    catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                }
                try {
                    chat_room.reset_list();
                }
                catch (Exception E){
                    Log.e(TAG,E.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void make_notification(String sender, String content) {


        String replyLabel = getResources().getString(R.string.reply_label);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            RemoteInput remoteInput = new RemoteInput.Builder(content)
                    .setLabel(replyLabel)
                    .build();

            Intent resultIntent = new Intent(this, chat_room.class);
            Integer index = 0;
            for(int i = 0; i < Global.All_members.size(); i ++){
                if (Global.All_members.get(i).getName().equals(sender))
                {
                    index = i;
                    break;
                }
            }
            resultIntent.putExtra("call",index);
            resultIntent.putExtra("Receiver",sender);
            resultIntent.putExtra("From","call_service");
            resultIntent.putExtra("Content",remoteInput.getResultKey());
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.btn_send,
                            getString(R.string.reply_label), resultPendingIntent)
                            .build();

            String creator = sender;
            String notification_message;
            String status = "";
            notification_message = content;
            status = "Late";
            notificationDialog(creator, notification_message, status,action);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void notificationDialog(String creator, String message, String status,NotificationCompat.Action action) {
        try {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String NOTIFICATION_CHANNEL_ID = "01";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, creator, NotificationManager.IMPORTANCE_MAX);
                // Configure the notification channel.
                notificationChannel.setDescription(message);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            notificationBuilder.setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    //.setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle(creator)
                    .setContentText(message)
                    .setContentInfo("Min Pe")
                    .addAction(action)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setSound(alarmSound);

            if (status.equals("late")) {
                notificationBuilder.setSmallIcon(R.drawable.icon);
            } else
                notificationBuilder.setSmallIcon(R.drawable.icon);
            notificationManager.notify(1, notificationBuilder.build());
        }
        catch (Exception E){}
    }
}
