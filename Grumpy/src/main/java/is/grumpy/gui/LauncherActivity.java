package is.grumpy.gui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.contracts.LoginResponse;
import is.grumpy.contracts.PostUser;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyApi;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 17.2.2014.
 */
public class LauncherActivity extends Activity
{
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    private Button mSignUp;
    private Button mLogin;
    private EditText mUsername;
    private EditText mPassword;
    private GrumpyApi mGrumpyApi;

    private ProgressDialog mProgressDialog;

    public Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request)
            {
                request.addHeader("Content-Type", "application/json");
                //If Connection header is not absent Java will throw an IO Error
                request.addHeader("Connection", "close");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .setRequestInterceptor(requestInterceptor)
                .build();

        mGrumpyApi = restAdapter.create(GrumpyApi.class);

        mProgressDialog = new ProgressDialog(this, 2);
        mProgressDialog.setMessage("Logging In");

        AttachViews();
        AttachEventListeners();
    }

    public void AttachEventListeners()
    {
        mSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Add some field validation
                String userName = mUsername.getText().toString();
                String passWord = mPassword.getText().toString();

                PostUser user = new PostUser();
                user.setUsername(userName);
                user.setPassword(passWord);

                mGrumpyApi.loginUser(user, loginCallback);
                mProgressDialog.show();
            }
        });
    }

    Callback<LoginResponse> loginCallback = new Callback<LoginResponse>()
    {
        @Override
        public void success(LoginResponse loginResponse, Response response)
        {
            mProgressDialog.dismiss();

            if (loginResponse.getStatus())
            {
                new Credentials(getContext()).WriteCredentialsToCache(loginResponse);

                Intent intent = new Intent(getContext(), BaseNavigationDrawer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Clear Activity Stack
                startActivity(intent);
            }
            else
            {
                Dialog.LoginFailedDialog(getContext());
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            mProgressDialog.dismiss();
            Dialog.LoginFailedDialog(getContext());
        }
    };

    private void AttachViews()
    {
        mSignUp = (Button) findViewById(R.id.signupUser);
        mLogin = (Button) findViewById(R.id.btnLoginUsername);
        mUsername = (EditText) findViewById(R.id.loginUsername);
        mPassword = (EditText) findViewById(R.id.loginPassword);
    }
}
