package is.grumpy.gui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Arnar on 5.3.2014.
 */
public class LogOutDialog extends DialogFragment
{
    public static LogOutDialog newInstance()
    {
        LogOutDialog f = new LogOutDialog();
        return f;
    }

    private OnLogOutListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try
        {
            mListener = (OnLogOutListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement OnLogOutListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 2);
        builder
                .setMessage("Are you sure you want to log out ?")
                .setTitle("Log Out")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        mListener.onLogOut();
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public interface OnLogOutListener
    {
        public void onLogOut();
    }
}
