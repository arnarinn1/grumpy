package is.grumpy.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import is.grumpy.R;
import is.grumpy.cache.Credentials;
import is.grumpy.gui.base.BaseNavigationDrawer;

public class MainActivity extends Activity
{
    private Credentials mCredentials = new Credentials(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mCredentials.UserLoggedIn())
        {
            Intent intent = new Intent(this, BaseNavigationDrawer.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
        }
    }
}
