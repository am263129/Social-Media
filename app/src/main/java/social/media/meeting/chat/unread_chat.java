package social.media.meeting.chat;

import java.util.ArrayList;

public class unread_chat {
    private String sender;
    private String key;
    private ArrayList<String> subkey;

    public unread_chat(){

    }
    public unread_chat(String sender, String key, ArrayList<String> subkey){
        this.sender = sender;
        this.key = key;
        this.subkey = subkey;
    }

    public ArrayList<String> getSubkey() {
        return subkey;
    }

    public String getKey() {
        return key;
    }

    public String getSender() {
        return sender;
    }
}
