package chatapp.amrhal.example.com.chatapp.oneToOne;

/**
 * Created by hp on 13/11/2017.
 */

public class oneToOneData {
    private String name;
    private String text;
    private String photoUrl;

    public oneToOneData(String name, String text, String photoUrl) {
        this.name = name;
        this.text = text;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
