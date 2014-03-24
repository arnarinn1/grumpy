package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by thdg9_000 on 16.2.2014.
 */
public class UserData implements Serializable
{
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("about")
    private String about;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("sex")
    private String sex;

    @SerializedName("following")
    private Boolean following = false;

    public void setUsername(String username) { this.username = username; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAbout(String about) { this.about = about; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setSex(String sex) { this.sex= sex; }

    public String getUsername() { return this.username; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getAvatar() { return this.avatar; }
    public String getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getBirthday() { return this.birthday; }
    public String getAbout() { return this.about; }
    public String getSex() { return this.sex; }

    public Boolean getFollowing() {
        return this.following;
    }

    public String getFullName() { return this.firstName + " " + this.lastName; }
}
