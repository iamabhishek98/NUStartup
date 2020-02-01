package orbital.gns.nustartup;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;

public class NotiModel implements Serializable {

    private String personName;
    private String companyName;

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    private String messageUser;

    public Boolean getWorker() {
        return isWorker;
    }

    public void setWorker(Boolean worker) {
        isWorker = worker;
    }

    private Boolean isWorker;

    public NotiModel(String personName, String companyName, Boolean isWorker, String messageUser){
        this.personName = personName;
        this.companyName = companyName;
        this.isWorker = isWorker;
        this.messageUser = messageUser;
    }

    public NotiModel() {

    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
