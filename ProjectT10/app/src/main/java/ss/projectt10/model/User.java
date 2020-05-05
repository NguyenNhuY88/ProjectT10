package ss.projectt10.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String username;
    private String email;
    private String avatar;

    private String dateOfBirth;
    private String address;
    private String phoneNumber;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username, String email, String avatar, String dateOfBirth, String address, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Object> toMapUser() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("avatar", avatar);
        result.put("email", email);
        result.put("dateOfBirth", dateOfBirth);
        result.put("address",address);
        result.put("phoneNumber", phoneNumber);
        return  result;
    }
}
