package com.wdziemia.githubtimes.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Toast;

import com.wdziemia.githubtimes.R;

/**
 * A preference that allows a range of number inputs, defaulting from 0 to 100
 */
public class RangeEditTextPreference extends EditTextPreference {

    private int maxValue = 100;
    private int minValue = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RangeEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public RangeEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RangeEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeEditTextPreference(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.RangeEditTextPreference,
                0,
                R.style.RangeEditTextPreference);

        try {
            minValue = a.getInteger(R.styleable.RangeEditTextPreference_minValue, 0);
            maxValue = a.getInteger(R.styleable.RangeEditTextPreference_maxValue, 0);
            boolean showMinMaxHint = a.getBoolean(R.styleable.RangeEditTextPreference_showMinMaxHint, true);
            if (showMinMaxHint) {
                String minMaxHint = context.getString(R.string.range_edit_text_pref_hint, minValue, maxValue);
                getEditText().setHint(minMaxHint);
            }
        } finally {
            a.recycle();
        }
    }

    /**
     * Override default impl and only set value if it falls within our range. Throw toast if value
     * isnt in range.
     *
     * @param positiveResult
     */
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = getEditText().getText().toString();
            if (!TextUtils.isEmpty(value) && TextUtils.isDigitsOnly(value)) {
                int intValue = Integer.parseInt(value);
                if (intValue >= minValue && intValue <= maxValue) {
                    if (callChangeListener(value)) {
                        setText(value);
                        return;
                    }
                }
            }
            String errorMessage = getContext().getString(R.string.range_edit_text_pref_error, minValue, maxValue);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
