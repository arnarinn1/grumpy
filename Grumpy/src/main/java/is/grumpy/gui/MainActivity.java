package is.grumpy.gui;

import android.os.Bundle;

import is.grumpy.R;
import is.grumpy.gui.base.BaseNavigationDrawer;

public class MainActivity extends BaseNavigationDrawer
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeNavigationDrawer();
    }
}
