package is.grumpy.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arnar on 14.2.2014.
 */
public class ServerResponse
{
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    @SerializedName("status_message")
    private String statusMessage;

    public String getMessage() { return message; }
    public boolean getStatus() { return status; }
    public String getStatusMessage() { return statusMessage; }
}
