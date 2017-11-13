package chatapp.amrhal.example.com.chatapp;

/**
 * Created by hp on 12/11/2017.
 */

public class Data {


        private String text; //lazem fe el data base tkon nafs el "text"
        private String name;
        private String photoUrl;
        private String profilePhoto;

        public Data() {
        }

        public Data(String text, String name, String photoUrl) {
            this.text = text;
            this.name = name;
            this.photoUrl = photoUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }


