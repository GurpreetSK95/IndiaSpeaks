package gurpreetsk.me.indiaspeaks.Models;

import android.location.Location;

import java.util.Date;

/**
 * Created by Gurpreet on 17/08/16.
 */
public class Feed {

    private String complaint;
    private String image;
    private String date_time;
    private String pincode;
    private String name;
    private String email;
    private String link;

    public Feed() {
    }

    public Feed(String complaint, String image, String date_time, String pincode, String name, String email, String link) {
        this.complaint = complaint;
        this.image = image;
        this.date_time = date_time;
        this.pincode = pincode;
        this.name= name;
        this.email=email;
        this.link = link;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
