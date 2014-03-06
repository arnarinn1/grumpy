package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Arnar on 4.2.2014.
 */
public class FeedData implements Serializable
{
    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private UserData user;

    @SerializedName("created_at")
    private String timeCreated;

    @SerializedName("post")
    private String post;

    @SerializedName("likes")
    private List<LikeData> likes;

    @SerializedName("comments")
    private List<CommentData> comments;

    public UserData getUser() { return this.user; }
    public String getTimeCreated() { return timeCreated; }
    public String getPost() { return post; }
    public String getId() { return id;}
    public List<LikeData> getLikes() { return likes; }
    public List<CommentData> getComments() { return comments; }
}
