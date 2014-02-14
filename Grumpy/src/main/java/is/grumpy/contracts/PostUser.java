package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 14.2.2014.
 */
public class PostUser
{
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username;}
    public void setPassword(String password) { this.password = password;}
}
