package social.media.meeting.chat;

import java.util.Date;

public class chatMessage {
    public String chatAuthor;
    public String chatContent;
    public String crated_date;
    public chatMessage(){

    }
    public chatMessage (String chatAuthor, String chatContent, String created){
        this.chatAuthor = chatAuthor;
        this.chatContent = chatContent;
        this.crated_date = created;
    }

    public String  getCrated_date() {
        return crated_date;
    }

    public String getChatAuthor() {
        return chatAuthor;
    }

    public String getChatContent() {
        return chatContent;
    }
}
