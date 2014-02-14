package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 14.2.2014.
 */
public class ServerResponse
{
    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public String status;

    public String getMessage() { return message; }
    public String getStatus() { return status; }
}
