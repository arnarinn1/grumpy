package is.grumpy.gui;

import android.app.Activity;
import android.os.Bundle;

import is.grumpy.R;

/**
 * Created by Arnar on 17.2.2014.
 */
public class LauncherActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
}
