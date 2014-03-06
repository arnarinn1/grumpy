package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 20.2.2014.
 */
public class PostRequest
{
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("post")
    private String postMessage;

    @SerializedName("comment")
    private String comment;

    public void setAccessToken(String accessToken) { this.accessToken = accessToken;}
    public void setPostMessage(String postMessage) { this.postMessage = postMessage;}
    public void setComment(String comment) { this.comment = comment; }
}
