package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 14.2.2014.
 */
public class UserAvailable
{
    @SerializedName("user_available")
    private boolean userExists;

    public boolean getUserExists() { return userExists;}
}
