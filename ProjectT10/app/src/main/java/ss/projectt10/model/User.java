package ss.projectt10.model;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private String email;
    private String avatar;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username, String email, String avatar) {
        this.username = username;
        this.email = email;
        this.avatar = avatar;
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

    public Map<String, Object> toMapUser() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("avatar", avatar);
        result.put("email", email);
        return  result;
    }
}
