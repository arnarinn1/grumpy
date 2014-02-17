package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 4.2.2014.
 */
public class GrumpyFeedData
{
    @SerializedName("user")
    private GrumpyUserData user;

    @SerializedName("created_at")
    private String timeCreated;

    @SerializedName("post")
    private String post;

    public GrumpyUserData getUser() { return this.user; }
    public String getTimeCreated() { return timeCreated; }
    public String getPost() { return post; }

    public void setUser(GrumpyUserData user) { this.user = user; }
    public void setTimeCreated(String timeCreated){ this.timeCreated = timeCreated; }
    public void setPost(String post) { this.post = post; }
}
