package social.media.meeting.member;

public class hired_member {
    private String name;
    private String email;

    public hired_member(){

    }
    public hired_member(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
