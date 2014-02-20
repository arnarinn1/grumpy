package is.grumpy.rest;

import java.util.List;

import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import is.grumpy.contracts.UserData;
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

    @GET("/user/{username}/exists")
    void checkIfUserExists(@Path("username") String username, Callback<UserAvailable> callback);

    @GET("/user/{username}/search")
    void searchUsers(@Path("username") String username, Callback<List<UserData>> users);
}
