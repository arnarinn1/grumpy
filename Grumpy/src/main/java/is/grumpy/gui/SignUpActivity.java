package is.grumpy.gui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import is.grumpy.R;
import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import is.grumpy.rest.GrumpyApi;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 1.2.2014.
 */
public class SignUpActivity extends ActionBarActivity
{
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    private Button mSignup;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private EditText mFullNameField;
    private ImageView mUsernameStatus;
    private PostUser mNewUser = new PostUser();

    private GrumpyApi grumpyApi;

    private Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Initialize();
    }

    private void Initialize()
    {
        mSignup = (Button) findViewById(R.id.signupUserButton);
        mUsernameField = (EditText) findViewById(R.id.signupUsername);
        mPasswordField = (EditText) findViewById(R.id.signupPassword);
        mPasswordConfirmField = (EditText) findViewById(R.id.signupConfirmPassword);
        mFullNameField = (EditText) findViewById(R.id.signupFullName);
        mUsernameStatus = (ImageView) findViewById(R.id.signupUsernameStatus);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .build();

        grumpyApi = restAdapter.create(GrumpyApi.class);

        mSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SignUpUser();
            }
        });

        mUsernameField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    String usernameCheck = mUsernameField.getText().toString();
                    grumpyApi.checkIfUserExists(usernameCheck, checkUserExistCallback);
                }
            }
        });
    }

    private void SignUpUser()
    {
        if(ValidateInputFields())
        {
            grumpyApi.createUser(mNewUser, createNewUserCallback);
        }
    }

    Callback<ServerResponse> createNewUserCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse response, retrofit.client.Response response2)
        {
            Toast.makeText(getContext(), "Created New Account", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getContext(), "You Fail So Hard", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<UserAvailable> checkUserExistCallback = new Callback<UserAvailable>()
    {
        @Override
        public void success(UserAvailable userAvailable, Response response)
        {
            if (userAvailable.getUserExists())
                mUsernameStatus.setImageResource(R.drawable.valid);
            else
                mUsernameStatus.setImageResource(R.drawable.invalid);
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            //HMM: Do something ?
        }
    };

    private boolean ValidateInputFields()
    {
        String userName = mUsernameField.getText().toString();
        String fullName = mFullNameField.getText().toString();
        String password = mPasswordField.getText().toString();
        String confirmedPassword = mPasswordConfirmField.getText().toString();

        if ((password.length() < 6) || (confirmedPassword.length() < 6))
        {
            Toast.makeText(this, "Password Length Must Exceed 5 Characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmedPassword))
        {
            Toast.makeText(this, "Passwords Must Match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userName.length() == 0 || fullName.length() == 0)
        {
            Toast.makeText(this, "Must Enter a Username and Full Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        mNewUser.setPassword(password);
        mNewUser.setUsername(userName);

        return true;
    }
}
