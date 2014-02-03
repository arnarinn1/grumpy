package is.grumpy.gui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import is.grumpy.R;

/**
 * Created by Arnar on 1.2.2014.
 */
public class SignUpActivity extends ActionBarActivity
{
    private Button mSignup;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private EditText mFullNameField;
    private ImageView mUsernameStatus;

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
                    //Call Rest Api to check if username exists.
                    new CheckCredentialsWorker(getApplicationContext()).execute();
                    mUsernameStatus.setImageResource(R.drawable.valid);
                }
            }
        });
    }

    private void SignUpUser()
    {
        if(ValidateInputFields())
        {
            //Call Rest Api and Create User
            new SignUpUserWorker().execute();
        }
    }

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

        return true;
    }

    private class CheckCredentialsWorker extends AsyncTask<String, Void, Boolean>
    {
        private Context mContext;

        public CheckCredentialsWorker(Context context)
        {
            this.mContext = context;
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            try
            {
                return CheckIfUserExists(params[0]);
            }
            catch (Exception ex)
            {
                Log.e(getClass().getName(), ex.getMessage());
            }

            return null;
        }

        public boolean CheckIfUserExists(String userName)
        {
            return false;
        }
    }

    private class SignUpUserWorker extends AsyncTask<String, Void, Boolean>
    {
        @Override
        protected Boolean doInBackground(String... params)
        {
            return null;
        }
    }
}
