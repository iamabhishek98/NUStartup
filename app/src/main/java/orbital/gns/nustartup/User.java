package orbital.gns.nustartup;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {
    public String email;
    public String password;
    public Uri photoUri;
    public String biography;
    public String phoneNumber;
    public String name;
    public String url;
    public HashMap<String, String> organisations;
    public HashMap<String, String> notifications;


    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.photoUri = null;
        this.biography = null;
        this.phoneNumber = null;
        this.name = null;
        this.url = null;
        this.organisations = new HashMap<>();
        this.notifications = new HashMap<>();
    }

    public User() {
        
    }

    public User(User user) {
        this.email = user.email;
        this.password = user.password;
        this.photoUri = user.photoUri;
        this.biography = user.biography;
        this.phoneNumber = user.phoneNumber;
        this.name = user.name;
        this.url = user.url;
        this.organisations = user.organisations;
        this.notifications = user.notifications;
    }
}
