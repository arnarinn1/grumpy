package is.grumpy.gui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import is.grumpy.R;

/**
 * Created by Arnar on 1.2.2014.
 */
public class SignUpActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Initialize();
    }

    private void Initialize()
    {
        
    }
}
