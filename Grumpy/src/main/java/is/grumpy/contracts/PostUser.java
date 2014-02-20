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

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("about")
    public String about;

    @SerializedName("avatar")
    public String avatar;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName;}
    public String getAbout() { return about;}
    public String getAvatar() { return avatar;}

    public void setUsername(String username) { this.username = username;}
    public void setPassword(String password) { this.password = password;}
    public void setFirstName(String firstName) { this.firstName = firstName;}
    public void setLastName(String lastName) { this.lastName = lastName;}
    public void setAbout(String about) { this.about = about;}
    public void setAvatar(String avatar) { this.avatar = avatar;}
}
