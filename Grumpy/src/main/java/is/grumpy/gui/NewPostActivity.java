package is.grumpy.gui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.contracts.PostRequest;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.gui.base.BaseActivity;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 16.2.2014.
 */
public class NewPostActivity extends BaseActivity
{
    @InjectView(R.id.send_new_post) Button mSendNewPost;
    @InjectView(R.id.edit_post) EditText mPostData;

    private GrumpyService mGrumpyApi;

    private Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_post);
        ButterKnife.inject(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(this);
        mGrumpyApi = restAdapter.create(GrumpyService.class);

        mSendNewPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String post = mPostData.getText().toString();

                PostRequest postData = new PostRequest();
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
                setResult(RESULT_OK);
                finish();
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getContext(), "Something went horribly wrong!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}
