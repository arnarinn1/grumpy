package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 20.2.2014.
 */
public class PostRequest
{
    @SerializedName("post")
    private String postMessage;

    @SerializedName("comment")
    private String comment;

    @SerializedName("base64")
    public String base64;

    public void setPostMessage(String postMessage) { this.postMessage = postMessage;}
    public void setComment(String comment) { this.comment = comment; }
    public void setBase64(String base64) { this.base64 = base64;}
}
