package interware.coolapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chelixpreciado on 7/18/16.
 */
public class Comment implements Parcelable {

    private String uid;
    private String user;
    private String text;

    public Comment() {
    }

    public Comment(String uid, String user, String text) {
        this.uid = uid;
        this.user = user;
        this.text = text;
    }

    protected Comment(Parcel in) {
        uid = in.readString();
        user = in.readString();
        text = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("user", user);
        result.put("text", text);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(user);
        parcel.writeString(text);
    }
}
