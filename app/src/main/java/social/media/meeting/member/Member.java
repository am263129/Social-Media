package social.media.meeting.member;

public class Member {
    public String name;
    public String email;
    public String gender;
    public boolean hired;
    public String photo;
    public String address;
    public String location;
    public String official_phone;
    public String birthday;
    public String password;
    public String personal_phone;
    public String official_email;
    public String designation;


    public Member(){

    }
    public Member(String name, String email, String gender, String photo, String birthday, String address, String location, String password, String designation, String official_email, String official_phone, String personal_phone){
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.photo = photo;
        this.address = address;
        this.location = location;
        this.birthday = birthday;
        this.password = password;
        this.designation = designation;
        this.official_email = official_email;
        this.official_phone = official_phone;
        this.personal_phone = personal_phone;
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getGender(){
        return this.gender;
    }
    public boolean getStatus(){
        return this.hired;
    }
    public String getPhoto() {
        return photo;
    }
    public String getAddress() {
        return address;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getLocation() {
        return location;
    }
    public String getOfficial_phone() {
        return official_phone;
    }
    public String getPassword() {
        return password;
    }

    public String getDesignation() {
        return designation;
    }

    public String getPersonal_phone() {
        return personal_phone;
    }

    public String getOfficial_email() {

        return official_email;
    }


}
