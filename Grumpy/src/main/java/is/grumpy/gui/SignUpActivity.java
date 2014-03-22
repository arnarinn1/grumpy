package is.grumpy.gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import is.grumpy.R;
import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import is.grumpy.gui.dialogs.SignupSuccessFulDialog;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import is.grumpy.utils.BitmapHelper;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 1.2.2014.
 */
public class SignUpActivity extends ActionBarActivity implements SignupSuccessFulDialog.OnSignupSuccessfulListener
{
    private static int RESULT_LOAD_IMAGE = 1;

    @InjectView(R.id.signupUserButton)      Button mSignup;
    @InjectView(R.id.signupUsername)        EditText mUsernameField;
    @InjectView(R.id.signupPassword)        EditText mPasswordField;
    @InjectView(R.id.signupConfirmPassword) EditText mPasswordConfirmField;
    @InjectView(R.id.signupFirstName)       EditText mFirstNameField;
    @InjectView(R.id.signupLastName)        EditText mLastNameField;
    @InjectView(R.id.signupAbout)           EditText mAboutField;
    @InjectView(R.id.signupUsernameStatus)  ImageView mUsernameStatus;
    @InjectView(R.id.signupAvatar)          ImageView mProfileAvatar;

    private PostUser mNewUser = new PostUser();
    private GrumpyService grumpyApi;

    private Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ButterKnife.inject(this);
        Initialize();
    }

    private void Initialize()
    {
        RestAdapter restAdapter = RetrofitUtil.RestAdapterPostInstance(this);
        grumpyApi = restAdapter.create(GrumpyService.class);

        mSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SignUpUser();
            }
        });

        mProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBitmapFromGallery();
            }
        });

        mUsernameField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                {
                    String usernameCheck = mUsernameField.getText().toString();
                    grumpyApi.checkIfUserExists(usernameCheck, checkUserExistCallback);
                }
            }
        });
    }

    private void GetBitmapFromGallery()
    {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = BitmapHelper.getBitmapFromGallery(this, selectedImage);

            String base64image = BitmapHelper.getBase64StringFromBitmap(bitmap);
            mNewUser.setBase64image(base64image);
            mProfileAvatar.setImageBitmap(bitmap);
        }
    }

    private void SignUpUser()
    {
        if(ValidateInputFields())
        {
            grumpyApi.createUser(mNewUser, createNewUserCallback);
        }
    }

    Callback<ServerResponse> createNewUserCallback = new Callback<ServerResponse>()
    {
        @Override
        public void success(ServerResponse response, retrofit.client.Response response2)
        {
            if (response.getStatus())
            {
                SignupSuccessFulDialog dialog = new SignupSuccessFulDialog();
                dialog.show(getFragmentManager(), "signup_tag");
            }
            else
                Toast.makeText(getContext(), "Username exists", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getContext(), "Error Communicating With Server", Toast.LENGTH_SHORT).show();
        }
    };

    Callback<UserAvailable> checkUserExistCallback = new Callback<UserAvailable>()
    {
        @Override
        public void success(UserAvailable userAvailable, Response response)
        {
            if (userAvailable.getUserExists())
                mUsernameStatus.setImageResource(R.drawable.valid);
            else
                mUsernameStatus.setImageResource(R.drawable.invalid);
        }

        @Override
        public void failure(RetrofitError retrofitError)
        {
            Toast.makeText(getContext(), "Hmmm", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean ValidateInputFields()
    {
        String userName = mUsernameField.getText().toString();
        String password = mPasswordField.getText().toString();
        String confirmedPassword = mPasswordConfirmField.getText().toString();
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String about  = mAboutField.getText().toString();

        if ((password.length() < 6) || (confirmedPassword.length() < 6))
        {
            Toast.makeText(this, "Password Length Must Exceed 5 Characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmedPassword))
        {
            Toast.makeText(this, "Passwords Must Match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userName.length() == 0)
        {
            Toast.makeText(this, "Must Enter a Username", Toast.LENGTH_SHORT).show();
            return false;
        }

        mNewUser.setPassword(password);
        mNewUser.setUsername(userName);
        mNewUser.setFirstName(firstName.equals("") ? null : firstName);
        mNewUser.setLastName(lastName.equals("") ? null : lastName);
        mNewUser.setAbout(about.equals("") ? null : about);

        return true;
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onSuccess()
    {
        finish();
    }
}
