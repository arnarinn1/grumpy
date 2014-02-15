package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 15.2.2014.
 */
public class UserData
{
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    public String getId() { return id; }
    public String getUsername() { return username; }
}
