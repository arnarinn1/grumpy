package is.grumpy.gui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.GrumpyPostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.rest.GrumpyApi;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Arnar on 16.2.2014.
 */
public class NewPostActivity extends ActionBarActivity
{
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    private Button mSendNewPost;
    private EditText mPostData;
    private GrumpyApi mGrumpyApi;

    private Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mSendNewPost = (Button) findViewById(R.id.send_new_post);
        mPostData = (EditText) findViewById(R.id.edit_post);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request)
            {
                request.addHeader("Content-Type", "application/json");
                request.addHeader("Accept", "application/json");
                //If Connection header is not absent Java will throw an IO Error
                request.addHeader("Accept-Encoding", "" );
                request.addHeader("Connection", "Close");
            }
        };

        //NOTE: There seems to be some bug in Retrofit(Might be an Android Bug).  If Connection header is not set to Close
        //      and the client is not set to ApacheClient, java will throw an EOFException.  This seems to work for now
        //      but will need to keep a close a eye on this one.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .setClient(new ApacheClient())
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mGrumpyApi = restAdapter.create(GrumpyApi.class);

        mSendNewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String post = mPostData.getText().toString();

                GrumpyPostRequest postData = new GrumpyPostRequest();
                postData.setAccessToken(new Credentials(getContext()).GetAccessToken());
                postData.setPostMessage(post);

                mGrumpyApi.postGrumpyMessage(postData, postMessageCallback);
            }
        });
    }

    Callback<ServerResponse> postMessageCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse serverResponse, Response response)
        {
            if (serverResponse.getStatus())
            {
                finish();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getContext(), "Something went horribly wrong!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}
