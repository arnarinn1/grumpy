package is.grumpy.rest;

import java.util.List;

import is.grumpy.contracts.GrumpyFeedData;
import is.grumpy.contracts.GrumpyPostRequest;
import is.grumpy.contracts.GrumpyUserData;
import is.grumpy.contracts.LoginResponse;
import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
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

    @POST("/login")
    void loginUser(@Body PostUser loginUser, Callback<LoginResponse> callback);

    @POST("/post")
    void postGrumpyMessage(@Body GrumpyPostRequest postRequest, Callback<ServerResponse> callback);

    @POST("/logout")
    void logOutUser(@Body GrumpyPostRequest postRequest, Callback<ServerResponse> callback);

    @GET("/user/{username}/exists")
    void checkIfUserExists(@Path("username") String username, Callback<UserAvailable> callback);

    @GET("/user/{username}/search")
    void searchUsers(@Path("username") String username, Callback<List<GrumpyUserData>> users);

    @GET("/post")
    List<GrumpyFeedData> getPosts();

    @DELETE("/post/{post_id}/{access_token}")
    ServerResponse deletePost(@Path("post_id") String postId, @Path("access_token") String accessToken);
}
