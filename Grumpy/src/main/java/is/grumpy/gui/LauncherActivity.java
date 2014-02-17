package is.grumpy.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import is.grumpy.R;

/**
 * Created by Arnar on 17.2.2014.
 */
public class LauncherActivity extends Activity
{
    private Button mSignUp;

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
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AttachViews()
    {
        mSignUp = (Button) findViewById(R.id.signupUser);
    }
}
