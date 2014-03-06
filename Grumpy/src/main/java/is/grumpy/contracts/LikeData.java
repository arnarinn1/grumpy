package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Arnar on 5.3.2014.
 */
public class LikeData implements Serializable
{
    @SerializedName("id")
    private String id;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("user")
    private UserData user;

    public String getId() { return id; }
    public String getCreatedAt() { return createdAt; }
    public UserData getUser() { return user; }
}
