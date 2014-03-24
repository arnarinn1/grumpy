package is.grumpy.gui;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by thdg9_000 on 17.2.2014.
 */
public class EditProfileFragment extends BaseFragment
{
    @InjectView(R.id.first_name) EditText mFirstName;
    @InjectView(R.id.last_name) EditText mLastName;
    @InjectView(R.id.birth_year) EditText mBirthday;
    @InjectView(R.id.about) EditText mAbout;
    @InjectView(R.id.email) EditText mEmail;
    @InjectView(R.id.spinner) Spinner mSex;

    private GrumpyService grumpyApi;

    public static EditProfileFragment newInstance(int position)
    {
        EditProfileFragment fragment = new EditProfileFragment();

        Bundle args = new Bundle();
        args.putInt(BaseNavigationDrawer.DRAWER_POSITION, position);

        fragment.setRetainInstance(true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSex.setAdapter(adapter);

        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(getActivity());
        grumpyApi = restAdapter.create(GrumpyService.class);

        Button update = (Button) getView().findViewById(R.id.updateUserButton);
        update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateUser();
            }
        });
    }

    private void updateUser()
    {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String birthday = mBirthday.getText().toString();
        String sex = mSex.getSelectedItem().toString();
        String about = mAbout.getText().toString();
        String email = mEmail.getText().toString();

        UserData profile = new UserData();
        profile.setFirstName(firstName.trim());
        profile.setLastName(lastName.trim());
        profile.setBirthday(birthday.trim());
        profile.setSex(sex);
        profile.setAbout(about.trim());
        profile.setEmail(email.trim());

        grumpyApi.updateUser(profile, updateUserCallback);
    }

    Callback<ServerResponse> updateUserCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse response, retrofit.client.Response response2)
        {
            //TODO: Return updated user info to update cache credentials
            if (response.getStatus())
            {
                Toast.makeText(getActivity(), "Updated User Info!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getActivity(), "Whoops, I have no idea what heppened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getActivity(), "Error Communicating With Server", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

}
