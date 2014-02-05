package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyFeedData
{
    @SerializedName("username")
    private String userName;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("post")
    private String timeCreated;

    @SerializedName("time")
    private String post;

    public String getUserName() { return userName; }
    public String getProfilePicture() { return profilePicture; }
    public String getTimeCreated() { return timeCreated; }
    public String getPost() { return post; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public void setTimeCreated(String timeCreated){ this.timeCreated = timeCreated; }
    public void setPost(String post) { this.post = post; }
}
