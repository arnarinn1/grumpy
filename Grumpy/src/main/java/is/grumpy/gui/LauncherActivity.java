package is.grumpy.gui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import is.grumpy.R;

/**
 * Created by Arnar on 17.2.2014.
 */
public class LauncherActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
}
