package is.grumpy.gui.base;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.DrawerListAdapter;
import is.grumpy.gui.FeedFragment;
import is.grumpy.gui.MessagesFragment;
import is.grumpy.gui.SignUpActivity;
import is.grumpy.gui.navigationdrawer.DrawerHeader;
import is.grumpy.gui.navigationdrawer.DrawerListItem;
import is.grumpy.gui.navigationdrawer.DrawerUserInfo;
import is.grumpy.gui.navigationdrawer.IDrawerItem;

/**
 * Created by Arnar on 27.1.2014.
 */
public class BaseNavigationDrawer extends BaseFragmentActivity
{
    public static final String DRAWER_POSITION = "is.grumpy.gui.base.POSITION";

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void InitializeNavigationDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        List<IDrawerItem> items = new ArrayList<IDrawerItem>();

        items.add(new DrawerUserInfo("Arnar Heimisson"));
        items.add(new DrawerHeader("FAVORITES"));
        items.add(new DrawerListItem("Grumpy Feed", R.drawable.ic_home, false));
        items.add(new DrawerListItem("Following", R.drawable.ic_people, false));
        items.add(new DrawerListItem("Messages", R.drawable.ic_messages, true));
        items.add(new DrawerListItem("Search Users", R.drawable.ic_search, false));
        items.add(new DrawerHeader("SETTINGS"));
        items.add(new DrawerListItem("App Settings", R.drawable.ic_settings, false));
        items.add(new DrawerListItem("Edit Profile", R.drawable.ic_settings, false));

        mDrawerList.setAdapter(new DrawerListAdapter(this, items));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        SetOnBackStackListener();

        StartAction(2);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item) || mDrawerToggle.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);

            StartAction(position);
        }
    }

    private void StartAction(int position)
    {
        Intent intent;
        Fragment fragment = null;

        switch(position)
        {
            case 2:
                fragment = FeedFragment.newInstance(position);
                break;
            case 4:
                fragment = MessagesFragment.newInstance(position);
                break;
            case 8:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }

        if (fragment != null)
        {
            FragmentManager fragmentManager = getFragmentManager();

            //Need to look better into these transactions so the flow will be more smooth, figure out how to remove specific
            //fragments if they are on the stack
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            closeNavigationDrawer();
        }
    }

    private void closeNavigationDrawer()
    {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        }, 150);
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager manager = getFragmentManager();

        if (manager.getBackStackEntryCount() != 1)
            manager.popBackStack();
        else
            super.onBackPressed();
    }

    private void SetOnBackStackListener()
    {
        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
        {
            @Override
            public void onBackStackChanged()
            {
                Bundle args = getFragmentManager().findFragmentById(R.id.frame_container).getArguments();

                int position = args.getInt(DRAWER_POSITION);

                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
            }
        });
    }
}
