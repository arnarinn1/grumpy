package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 20.2.2014.
 */
public class LoginResponse
{
    @SerializedName("message")
    private String message;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user")
    private UserData user;

    @SerializedName("status")
    private boolean status;

    public String getMessage() { return message; }
    public String getAccessToken() { return accessToken; }
    public UserData getUser() { return user; }
    public boolean getStatus() { return status; }
}
