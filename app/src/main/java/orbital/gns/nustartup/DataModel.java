package orbital.gns.nustartup;

import java.io.Serializable;
import java.util.HashMap;

public class DataModel implements Serializable {


    private String description;
    public String skills;
    private String founder;
    private String location;
    private String contact;
    public String name;
    public HashMap<String, String> members;
    public String ownerUid;
    public String dataModelUid;
//    private Uri image;

    public DataModel(String name, String description, String skills, String founder, String contact, String location, String ownerUid, String dataModelUid){
        this.name = name;
        this.description = description;
        this.skills = skills;
        this.founder = founder;
        this.contact = contact;
        this.location = location;
        this.members = new HashMap<>();
        this.ownerUid = ownerUid;
        this.dataModelUid = dataModelUid;
    }

    public DataModel() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

//    public Uri getImage() {
//        return image;
//    }

//    public void setImage(Uri image) {
//        this.image = image;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
