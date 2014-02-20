package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 20.2.2014.
 */
public class GrumpyPostRequest
{
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("post")
    private String postMessage;

    public void setAccessToken(String accessToken) { this.accessToken = accessToken;}
    public void setPostMessage(String postMessage) { this.postMessage = postMessage;}
}
