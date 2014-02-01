package is.grumpy.gui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        mSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Call Rest api
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
                }
            }
        });
    }
}
