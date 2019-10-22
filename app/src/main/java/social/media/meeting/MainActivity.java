package social.media.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import social.media.meeting.Util.Global;
import social.media.meeting.chat.chat;
import social.media.meeting.chat.chat_room;
import social.media.meeting.chat.public_chat_room;
import social.media.meeting.service.call_service;
import social.media.meeting.user.Member;
import social.media.meeting.user.ViewMembers;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    Dialog dialog;


    static String CHAT_ID = "Chat_Id";
    static String CHAT_SENDER = "Chat_Sender";
    static String CHAT_CONTENT = "Chat_Content";
    static String CHAT_TYPE = "Chat_Type";
    static String CHAT_CREATED_DATE = "Chat_Created_Date";
    static String CHAT_STATUS = "Chat_Status";
    static MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        startService(new Intent(this, call_service.class));
        setContentView(R.layout.activity_main);
        LinearLayout viewMember = (LinearLayout)findViewById(R.id.btn_private_chat);
        final LinearLayout public_chat = (LinearLayout)findViewById(R.id.btn_public_chat);
        viewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewMembers.class);
                startActivity(intent);
            }
        });
        public_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, public_chat_room.class);
                startActivity(intent);
            }
        });
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
        initData();

    }

    private void initData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Member");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Global.All_members.clear();
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    ArrayList<Member> array_all_members = new ArrayList<Member>();
                    for (String key : dataMap.keySet()){

                        Object data = dataMap.get(key);

                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            String userName = userData.get("Name").toString();
                            String userEmail = userData.get("Email").toString();
                            String userAddress = userData.get("Address").toString();
                            String userGender = userData.get("Gender").toString();
                            String password = userData.get("Password").toString();
                            String user_Area = userData.get("Area").toString();
                            String user_Birthday = userData.get("Birthday").toString();
                            String user_Phone = userData.get("Phone").toString();
                            String user_MemberShip = userData.get("Membership").toString();
                            String user_security = userData.get("Private").toString();
                            boolean security = false;
                            if (user_security.equals("True"))
                                security = true;
                            String base64photo = "";
                            try {
                                base64photo = userData.get("Photo").toString();
                            }
                            catch (Exception e){
                                Log.e(TAG,e.toString());
                            }

                            if(userEmail.equals(Global.current_user_email)) {
                                Global.current_user_name = userName;
                                Global.current_user_photo = base64photo;
//                                setPhoto(base64photo);
                            }
                            array_all_members.add(new Member(userName, userEmail, userAddress, user_Birthday, user_Phone, user_Area, password, userGender, user_MemberShip, base64photo, security));
                        }catch (Exception cce){
                            Log.e(TAG, cce.toString());
                        }

                    }
                    Global.All_members = array_all_members;
                    dialog.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });

        myRef = database.getReference("chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Global.array_all_chats.clear();
                Global.array_my_chats.clear();
                Global.chat_key.clear();
                if (dataSnapshot.exists()){
                    try {
                        HashMap<String,Object> datamap = (HashMap<String ,Object>) dataSnapshot.getValue();

                        for(String key: datamap.keySet()) {
                            String test = key;
                            Log.e(TAG, key);
                            Object data = datamap.get(key);
                            Global.array_all_chats.add(data);
                            try {
                                if (key.split("&")[0].equals(Global.current_user_name) || key.split("&")[1].equals(Global.current_user_name)) {
                                    Global.array_my_chats.add(data);
                                    Global.chat_key.add(key);
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


        myRef = database.getReference("chats/Public Chat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Global.array_public_chats.clear();
                if (dataSnapshot.exists()){
                    try {
                        HashMap<String,Object> datamap = (HashMap<String ,Object>) dataSnapshot.getValue();

                        for(String key: datamap.keySet()) {
                            String test = key;
                            Log.e(TAG, key);
                            Object data = datamap.get(key);

                            HashMap <String, Object> public_chat = (HashMap<String, Object>) data;
                            try {
                                String chat_id = public_chat.get(CHAT_ID).toString();
                                String chat_sender = public_chat.get(CHAT_SENDER).toString();
                                String chat_content = public_chat.get(CHAT_CONTENT).toString();
                                String chat_type = public_chat.get(CHAT_TYPE).toString();
                                String chat_created_date = public_chat.get(CHAT_CREATED_DATE).toString();
                                String chat_status = "";
                                try {
                                    chat_status = public_chat.get(CHAT_STATUS).toString();
                                }
                                catch (Exception E){}
                                chat chat = new chat(chat_id, chat_sender, chat_content, chat_type, chat_created_date,chat_status);
                                Global.array_public_chats.add(chat);
                            } catch (Exception E) {
                                Log.e(TAG, E.toString());
                            }

                        }
                        Collections.sort(Global.array_public_chats, new Global.FishNameComparator());
                    }
                    catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                }
                try {
                    public_chat_room.reset_chat();
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
    private void setPhoto(String base64photo) {
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);
//        ImageView userPhoto  = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_header_userphoto);
//        userPhoto.setImageBitmap(bitmap);
    }
    public static MainActivity getInstance(){
        return mainActivity;
    }
}
