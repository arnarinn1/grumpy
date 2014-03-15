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

    public void setPostMessage(String postMessage) { this.postMessage = postMessage;}
    public void setComment(String comment) { this.comment = comment; }
}
