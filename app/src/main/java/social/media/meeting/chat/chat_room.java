package social.media.meeting.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import social.media.meeting.R;
import social.media.meeting.Util.Global;

import static java.security.AccessController.getContext;

public class chat_room extends AppCompatActivity {
    static String TAG = "chat_room";


    static String CHAT_ID = "Chat_Id";
    static String CHAT_SENDER = "Chat_Sender";
    static String CHAT_CONTENT = "Chat_Content";
    static String CHAT_TYPE = "Chat_Type";
    static String CHAT_CREATED_DATE = "Chat_Created_Date";
    static String CHAT_STATUS = "Chat_Status";
    static String CHAT_NOTIFICATION = "Chat_Notification";


    static ArrayList<chat> array_chat = new ArrayList<chat>();
    static String partner_name;
    static chat_room self;
    static ListView chat_view;
    EditText chat_message;
    Button btn_send;
    RoundedImageView avatar;
    TextView name_partner, email_partner;
    static String key;
    static String chat_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chat_view = (ListView)findViewById(R.id.chat_list);
        chat_message = (EditText)findViewById(R.id.chat_message);
        btn_send = (Button)findViewById(R.id.btn_send);
        avatar = (RoundedImageView)findViewById(R.id.avatar);
        name_partner = (TextView)findViewById(R.id.partner_name);
        email_partner= (TextView)findViewById(R.id.partner_email);

        chat_key = null;





        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chat_message.getText().toString().equals(""))
                    Toast.makeText(chat_room.this,"Invalid Input", Toast.LENGTH_SHORT).show();
                else{
                    FirebaseApp.initializeApp(chat_room.this);
                    String sub_id;
                    String chat_id;
                    if (chat_key == null){
                         sub_id = Global.current_user_name+ "&"+partner_name;
                        chat_id = "0001";
                    }

                    else {
                        sub_id = chat_key;
                        chat_id = String.valueOf(array_chat.size() + 1);
                        Integer limit = chat_id.length();
                        if (chat_id.length()<4)
                            for (int i = 0; i < 4 - limit; i ++){
                                chat_id = "0" + chat_id;
                            }
                    }

                    String id = "chats/" + sub_id + "/" + chat_id + "/";
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(id+ CHAT_ID);
                    myRef.setValue(chat_id);
                    myRef = database.getReference(id+CHAT_SENDER);
                    myRef.setValue(Global.current_user_name);
                    myRef = database.getReference(id+CHAT_CONTENT);
                    myRef.setValue(chat_message.getText().toString());
                    myRef = database.getReference(id+CHAT_TYPE);
                    myRef.setValue("Text");
                    myRef = database.getReference(id+CHAT_CREATED_DATE);
                    myRef.setValue(Global.getToday());
                    myRef = database.getReference(id+CHAT_STATUS);
                    myRef.setValue("Unread");
                    myRef = database.getReference(id+CHAT_NOTIFICATION);
                    myRef.setValue("Unmade");

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is:" + value);
                            chat_message.setText("");
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG,"Failed to rad value ", databaseError.toException());
                            Toast.makeText(chat_room.this,"Can't Send Message. Please check Internet Connection.",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        Intent intent = getIntent();
        Integer index = intent.getIntExtra("call", 0);
        String base = null;
        try {
            base = intent.getStringExtra("From");
        }catch (Exception E){}
        if (base != null){
            String receiver = intent.getStringExtra("Receiver");
            String message = intent.getStringExtra("Content");
        }
        partner_name  = Global.All_members.get(index).getName();
        String base64photo = Global.All_members.get(index).getPhoto();
        String imageDataBytes = base64photo.substring(base64photo.indexOf(",") + 1);
        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        avatar.setImageBitmap(bitmap);
        name_partner.setText(Global.All_members.get(index).getName());
        email_partner.setText(Global.All_members.get(index).getEmail());
        self = this;
        reset_list();
    }
    public static void reset_list(){
        array_chat.clear();

        for (int i = 0; i < Global.array_my_chats.size(); i++) {
            Object chat_data = Global.array_my_chats.get(i);
            key = Global.chat_key.get(i);
            if (key.split("&")[0].equals(partner_name) || key.split("&")[1].equals(partner_name)) {
                chat_key = key;
                try {
                    HashMap<String, Object> chat_list = (HashMap<String, Object>) chat_data;
                    for (String sub_key : chat_list.keySet()) {
                        Object chat_item = chat_list.get(sub_key);

                        HashMap<String, Object> chat_detail = (HashMap<String, Object>) chat_item;
                        try {
                            String chat_id = chat_detail.get(CHAT_ID).toString();
                            String chat_sender = chat_detail.get(CHAT_SENDER).toString();
                            String chat_content = chat_detail.get(CHAT_CONTENT).toString();
                            String chat_type = chat_detail.get(CHAT_TYPE).toString();
                            String chat_created_date = chat_detail.get(CHAT_CREATED_DATE).toString();

                            String chat_status = "";
                            try {
                                chat_status = chat_detail.get(CHAT_STATUS).toString();
                            }
                            catch (Exception E){}

                            chat chat = new chat(chat_id, chat_sender, chat_content, chat_type, chat_created_date,chat_status);
                            array_chat.add(chat);
                        } catch (Exception E) {
                            Log.e(TAG, E.toString());
                        }

                    }
                    Collections.sort(array_chat, new Global.FishNameComparator());
                } catch (Exception E) {
                    Log.e(TAG, E.toString());
                }
            }
        }

        set_adapter();
    }

    private static void set_adapter() {
        chat_adapter adapter = new chat_adapter(chat_room.getInstance(),R.layout.item_mine_message,array_chat);
        chat_view.setAdapter(adapter);
        chat_view.setSelection(adapter.getCount() - 1);
    }
    private static chat_room getInstance(){

        return self;
    }

    @Override
    public void onStart() {
        super.onStart();
        Global.chat_room_running = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        Global.chat_room_running = false;
    }


}
