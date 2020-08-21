package com.paradisetechnologies.brigthwing.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatEditText;

import com.paradisetechnologies.brigthwing.R;

public class MyEditText extends AppCompatEditText {

    public MyEditText(Context context) {
        super(context);
        init(null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyEditText);
            String fontName = a.getString(R.styleable.MyEditText_etFamily);
            if (fontName != null)
            {
                Typeface myTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName+".ttf");
                setTypeface(myTypeFace);
            }
            a.recycle();
        }
    }
}
