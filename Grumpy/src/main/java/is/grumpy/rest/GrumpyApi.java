package is.grumpy.rest;

import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Arnar on 14.2.2014.
 */
public interface GrumpyApi
{
    @POST("/user")
    void createUser(@Body PostUser newUser, Callback<ServerResponse> callback);

    @GET("/userexists/{username}")
    void checkIfUserExists(@Path("username") String username, Callback<UserAvailable> callback);
}
