package is.grumpy.gui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Arnar on 22.3.2014.
 */
public class SignupSuccessFulDialog extends DialogFragment
{
    private OnSignupSuccessfulListener mListener;

    public static SignupSuccessFulDialog newInstance()
    {
        SignupSuccessFulDialog f = new SignupSuccessFulDialog();
        return f;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mListener = (OnSignupSuccessfulListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnSignupSuccessfulListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 2);
        builder
                .setMessage("Please Login")
                .setTitle("Signup Successful")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        mListener.onSuccess();
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface OnSignupSuccessfulListener
    {
        void onSuccess();
    }
}
