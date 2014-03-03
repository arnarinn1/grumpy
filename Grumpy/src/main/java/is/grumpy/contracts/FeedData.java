package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedData
{
    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private UserData user;

    @SerializedName("created_at")
    private String timeCreated;

    @SerializedName("post")
    private String post;

    public UserData getUser() { return this.user; }
    public String getTimeCreated() { return timeCreated; }
    public String getPost() { return post; }
    public String getId() { return id;}

    public void setUser(UserData user) { this.user = user; }
    public void setTimeCreated(String timeCreated){ this.timeCreated = timeCreated; }
    public void setPost(String post) { this.post = post; }
}
