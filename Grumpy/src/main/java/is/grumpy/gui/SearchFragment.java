package is.grumpy.gui;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import is.grumpy.R;
import is.grumpy.adapters.SearchAdapter;
import is.grumpy.contracts.UserData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 15.2.2014.
 */
public class SearchFragment extends BaseFragment
{
    public static final String ApiUrl = "http://arnarh.com/grumpy/public";

    private ListView mListView;
    private EditText mSearchUser;
    private TextView mNoResults;
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

        mListView = (ListView) getView().findViewById(R.id.lvSearchUsers);
        mSearchUser = (EditText) getView().findViewById(R.id.edtSearchUser);
        mNoResults = (TextView) getView().findViewById(R.id.noUsersFound);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiUrl)
                .build();

        grumpyApi = restAdapter.create(GrumpyService.class);

        InitializeEditTextSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }

    private void InitializeEditTextSearch()
    {
        mSearchUser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    PerformSearch();

                    InputMethodManager imm = (InputMethodManager) IActivity.context().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //Close the keyboard

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

            //Mayby we want to use these events
            @Override public void afterTextChanged(Editable s) {}
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
    }

    private void PerformSearch()
    {
        String username = mSearchUser.getText().toString();

        grumpyApi.searchUsers(username, searchUserCallback);
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
}
