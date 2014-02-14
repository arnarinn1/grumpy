package is.grumpy.rest;

import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by Arnar on 14.2.2014.
 */
public interface GrumpyApi
{
    @POST("/user")
    void createUser(@Body PostUser newUser, Callback<ServerResponse> callback);
}
