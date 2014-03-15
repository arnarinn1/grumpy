package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 15.3.2014.
 */
public class FollowingData
{
    @SerializedName("user_following")
    private UserData user;

    public UserData getUser() { return user; }
}
