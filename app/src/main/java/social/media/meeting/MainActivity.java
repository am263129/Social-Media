package social.media.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import social.media.meeting.chat.livechat;

public class MainActivity extends AppCompatActivity {
    public static MainActivity myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button chat = (Button)findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, livechat.class);
                startActivity(intent);
            }
        });
        myself = this;
    }












    public static MainActivity getInstance(){
        return myself;
    }
}
