package helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalBinObject {

    @SerializedName("link")
    private String link;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("timeStamp")
    private int timeStamp;
    public PersonalBinObject() {

    }

    public PersonalBinObject(String link,String fileName) {
        this.link = link;
        this.fileName = fileName;
    }

    // Getters
    public String getLink() {
        return link;
    }

    public int getTimeStamp() { return timeStamp;}

    public String getToken() {
        return token;
    }

    public String getFileName() {
        return fileName;
    }
}
