package is.grumpy.gui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import is.grumpy.R;
import is.grumpy.contracts.PostUser;
import is.grumpy.contracts.ServerResponse;
import is.grumpy.contracts.UserAvailable;
import is.grumpy.rest.ExifUtil;
import is.grumpy.rest.GrumpyService;
import is.grumpy.rest.RetrofitUtil;
import is.grumpy.utils.BitmapHelper;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Arnar on 1.2.2014.
 */
public class SignUpActivity extends ActionBarActivity
{
    private static int RESULT_LOAD_IMAGE = 1;

    private Button mSignup;
    private EditText mUsernameField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mAboutField;
    private ImageView mUsernameStatus;
    private ImageView mProfileAvatar;
    private PostUser mNewUser = new PostUser();

    private GrumpyService grumpyApi;

    private Context getContext() { return this; }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Initialize();
    }

    private void Initialize()
    {
        mSignup = (Button) findViewById(R.id.signupUserButton);
        mUsernameField = (EditText) findViewById(R.id.signupUsername);
        mPasswordField = (EditText) findViewById(R.id.signupPassword);
        mPasswordConfirmField = (EditText) findViewById(R.id.signupConfirmPassword);
        mFirstNameField = (EditText) findViewById(R.id.signupFirstName);
        mLastNameField = (EditText) findViewById(R.id.signupLastName);
        mAboutField = (EditText) findViewById(R.id.signupAbout);
        mUsernameStatus = (ImageView) findViewById(R.id.signupUsernameStatus);
        mProfileAvatar = (ImageView) findViewById(R.id.signupAvatar);

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
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Toast.makeText(getContext(), "Created New Account", Toast.LENGTH_SHORT).show();
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
            //HMM: Do something ?
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
}
