package is.grumpy.gui;

import android.content.Context;
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
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 16.2.2014.
 */
public class NewPostActivity extends ActionBarActivity
{
    public static boolean CallbackCreatedNewPost = false;

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

        RestAdapter restAdapter = RetrofitUtil.GetRetrofitRestAdapter();

        mGrumpyApi = restAdapter.create(GrumpyApi.class);

        mSendNewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String post = mPostData.getText().toString();

                GrumpyPostRequest postData = new GrumpyPostRequest();
                postData.setAccessToken(new Credentials(getContext()).GetCacheToken(Credentials.mAccessToken));
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
                CallbackCreatedNewPost = true;
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
