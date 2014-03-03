package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arnar on 3.3.2014.
 */
public class UserProfileData
{
    @SerializedName("user")
    private UserData user;

    @SerializedName("posts")
    private List<FeedData> posts;

    public UserData getUser() { return user;}
    public List<FeedData> getPosts() { return posts; }
}
