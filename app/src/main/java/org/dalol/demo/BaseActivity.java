package org.dalol.demo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author Filippo
 * @version 1.0.0
 * @since Sun, 01/07/2018 at 09:36.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private int mSelectedIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        onActivityCreated(savedInstanceState);
    }

    @CallSuper
    protected void onActivityCreated(Bundle savedInstanceState) {
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(String message) {
        dismissDialog();
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        mProgressDialog.show();
    }

    protected void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showSimpleDialog(String message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    protected void showChoosableItemsDialog(String title, String[] items, int selectedPosition, String buttonText) {
        mSelectedIndex = selectedPosition;
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setCancelable(true)
                .setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedIndex = which;
                    }
                })
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onItemSelected(mSelectedIndex);
                    }
                })
                .create();
        dialog.show();
    }

    @CallSuper
    protected void onItemSelected(int index) {
    }

    protected abstract int getContentView();
}
