package is.grumpy.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import is.grumpy.R;

/**
 * Created by Arnar on 17.2.2014.
 */
public class LauncherActivity extends Activity
{
    private Button mSignUp;
    private Button mLogin;

    public Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

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
                //try login mate
            }
        });

    }

    private void AttachViews()
    {
        mSignUp = (Button) findViewById(R.id.signupUser);
        mLogin = (Button) findViewById(R.id.btnLoginUsername);
    }
}
