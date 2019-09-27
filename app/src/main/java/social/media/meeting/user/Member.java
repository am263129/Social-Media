package social.media.meeting.user;

public class Member {
    public String name;
    public String email;
    public String address;
    public String birthday;
    public String phone;
    public String area;
    public String password;
    public String gender;
    public String membership;
    public String photo;
    public boolean is_private;


    public Member(){

    }
    public Member(String name,
                  String email,
                  String address,
                  String birthday,
                  String phone,
                  String area,
                  String password,
                  String gender,
                  String membership,
                  String photo,
                  boolean is_private){
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.birthday = birthday;
        this.phone = phone;
        this.area = area;
        this.password = password;
        this.membership = membership;
        this.photo = photo;
        this.is_private =  is_private;
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

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoto() {
        return photo;
    }

    public String getArea() {
        return area;
    }

    public String getMembership() {
        return membership;
    }
}
