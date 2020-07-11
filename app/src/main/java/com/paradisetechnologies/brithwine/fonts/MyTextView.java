package com.paradisetechnologies.brithwine.fonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import com.paradisetechnologies.brithwine.R;

public class MyTextView extends AppCompatTextView {

    public MyTextView(Context context) {
        super(context);
        init(null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontface);
            if (fontName != null)
            {
                Typeface myTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName+".ttf");
                setTypeface(myTypeFace);
            }
            a.recycle();
        }
    }
}
