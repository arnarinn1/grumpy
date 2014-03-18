package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 14.2.2014.
 */
public class PostUser
{
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("about")
    private String about;

    @SerializedName("base64image")
    private String base64image;

    public void setUsername(String username) { this.username = username;}
    public void setPassword(String password) { this.password = password;}
    public void setFirstName(String firstName) { this.firstName = firstName;}
    public void setLastName(String lastName) { this.lastName = lastName;}
    public void setAbout(String about) { this.about = about;}
    public void setBase64image(String base64image) { this.base64image = base64image; }
}
