package com.paradisetechnologies.brithwine.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paradisetechnologies.brithwine.R;

public  class CustomDialog extends Dialog
{
    private TextView tv_message;

    public CustomDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.custom_dialog);
        setCancelable(true);

        initializeViews();
    }

    private void initializeViews()
    {
        tv_message = findViewById(R.id.tv_message);
    }

    public CustomDialog(@NonNull Context context, int themeResId)
    {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }

    public void setMessage(String message)
    {
        tv_message.setText(message);
    }

}
