package social.media.meeting.chat;

public class chat {
    public String chat_id;
    public String chat_sender;
    public String chat_content;
    public String chat_type;
    public String chat_created_date;

    public chat(){

    }

    public chat(String chat_id, String chat_sender, String chat_content, String chat_type, String chat_created_date){
        this.chat_id = chat_id;
        this.chat_sender = chat_sender;
        this.chat_content = chat_content;
        this.chat_type = chat_type;
        this.chat_created_date = chat_created_date;
    }

    public String getChat_content() {
        return chat_content;
    }

    public String getChat_created_date() {
        return chat_created_date;
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getChat_sender() {
        return chat_sender;
    }

    public String getChat_type() {
        return chat_type;
    }
}
