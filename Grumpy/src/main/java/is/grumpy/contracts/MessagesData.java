package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 5.2.2014.
 */
public class MessagesData
{
    @SerializedName("user_id")
    private int userid;

    @SerializedName("user_message")
    private String userMessage;

    public int getUserid() { return userid; }
    public String getUserMessage() { return userMessage; }
}
