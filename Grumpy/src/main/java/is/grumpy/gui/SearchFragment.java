package is.grumpy.gui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.adapters.SearchAdapter;
import is.grumpy.contracts.UserData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 15.2.2014.
 */
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener
{
    @InjectView(R.id.lvSearchUsers) ListView mListView;
    @InjectView(R.id.edtSearchUser) EditText mSearchUser;
    @InjectView(R.id.noUsersFound) TextView mNoResults;

    private SearchAdapter mAdapter;
    private GrumpyService grumpyApi;

    public static SearchFragment newInstance(int position)
    {
        SearchFragment fragment = new SearchFragment();

        Bundle args = new Bundle();
        args.putInt(BaseNavigationDrawer.DRAWER_POSITION, position);

        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnItemClickListener(this);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterGetInstance();
        grumpyApi = restAdapter.create(GrumpyService.class);

        mSearchUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    HideKeyboard();
                }
            }
        });

        InitializeEditTextSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_search_user, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void InitializeEditTextSearch()
    {
        mSearchUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    PerformSearch();
                    HideKeyboard();

                    return true;
                }
                return false;
            }
        });

        mSearchUser.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence userName, int start, int before, int count)
            {
                if (userName.toString().length() > 2)
                {
                    grumpyApi.searchUsers(userName.toString(), searchUserCallback);
                }
            }

            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    private void PerformSearch()
    {
        String username = mSearchUser.getText().toString();

        grumpyApi.searchUsers(username, searchUserCallback);
    }

    private void HideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) IActivity.context().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //Close the keyboard
    }

    Callback<List<UserData>> searchUserCallback = new Callback<List<UserData>>()
    {
        @Override
        public void success(List<UserData> users, Response response)
        {
            if (users.size() > 0)
            {
                mNoResults.setVisibility(View.GONE);
                mAdapter = new SearchAdapter(IActivity.context(), users);
                mListView.setAdapter(mAdapter);
            }
            else
            {
                mListView.setAdapter(null);
                mNoResults.setVisibility(View.VISIBLE);

                Animation ani = AnimationUtils.loadAnimation(IActivity.context(), R.anim.slide_in_left);
                mNoResults.startAnimation(ani);
            }
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            //Do some cray shit
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        UserData user = mAdapter.getItem(position);

        ProfileFragment fragment = ProfileFragment.newInstance(user.getId());

        FragmentManager manager = getFragmentManager();

        manager.beginTransaction()
               .replace(R.id.frame_container, fragment)
               .addToBackStack(null)
               .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
               .commit();
    }
}
