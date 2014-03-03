package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thdg9_000 on 16.2.2014.
 */
public class UserData
{
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("avatar")
    private String avatar;

    public void setUsername(String username) { this.username = username; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getUsername() { return this.username; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getAvatar() { return this.avatar; }
    public String getId() { return this.id; }

    public String getFullName() { return this.firstName + " " + this.lastName; }
}
