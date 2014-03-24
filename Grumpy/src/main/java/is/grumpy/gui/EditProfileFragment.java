package is.grumpy.gui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.net.URI;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserData;
import is.grumpy.gui.base.BaseFragment;
import is.grumpy.gui.base.BaseNavigationDrawer;
import is.grumpy.gui.dialogs.SignupSuccessFulDialog;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by thdg9_000 on 17.2.2014.
 */
public class EditProfileFragment extends BaseFragment {

    private UserData profile;

    EditText mFirstName;
    EditText mLastName;
    EditText mBirthday;
    EditText mAbout;
    EditText mEmail;
    Spinner mSex;

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

        mFirstName = (EditText) getView().findViewById(R.id.first_name);
        mLastName = (EditText) getView().findViewById(R.id.last_name);
        mBirthday = (EditText) getView().findViewById(R.id.birth_year);
        mAbout = (EditText) getView().findViewById(R.id.about);
        mEmail = (EditText) getView().findViewById(R.id.email);
        mSex = (Spinner) getView().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSex.setAdapter(adapter);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request)
            {
                request.addHeader("Content-Type", "application/json");
                //If Connection header is not absent Java will throw an IO Error
                request.addHeader("Connection", "close");
            }
        };

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
        String email = mEmail.toString().toString();

        profile = new UserData();
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
            if (response.getStatus())
            {
                Toast.makeText(getActivity(), "Yayyy!", Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

}
