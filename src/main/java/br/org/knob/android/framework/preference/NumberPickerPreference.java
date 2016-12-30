package br.org.knob.android.framework.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import br.org.knob.android.framework.R;

public class NumberPickerPreference extends DialogPreference {
    public static final String TAG = "NumberPickerPreference";

    private static final String ATTR_MIN_VALUE = "minValue";
    private static final String ATTR_MAX_VALUE = "maxValue";

    private NumberPicker numberPicker;
    private int minValue, maxValue, defaultValue;
    private String minExternalKey, maxExternalKey;
    private Integer number = 0;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs,  android.R.attr.dialogPreferenceStyle);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Make custom preference look like android's native preferences
        //setWidgetLayoutResource(defStyleAttr); //Resources.getSystem().getIdentifier("dialogPreferenceStyle", "attr", "android"));

        TypedArray dialogType = context.obtainStyledAttributes(attrs, new int[]{Resources.getSystem().getIdentifier("DialogPreference", "styleable", "android")}, 0, 0);
        TypedArray numberPickerType = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, 0, 0);

        minExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_minExternal);
        maxExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_maxExternal);

        minValue = numberPickerType.getInt(R.styleable.NumberPickerPreference_minValue, 0);
        maxValue = numberPickerType.getInt(R.styleable.NumberPickerPreference_maxValue, 0);

        defaultValue = dialogType.getInt(Resources.getSystem().getIdentifier("Preference_defaultValue", "styleable", "android"), minValue);

        dialogType.recycle();
        numberPickerType.recycle();
    }

    @Override
    protected View onCreateDialogView() {
        int max = maxValue;
        int min = minValue;

        // External values
        if (maxExternalKey != null) {
            max = getSharedPreferences().getInt(maxExternalKey, maxValue);
        }
        if (minExternalKey != null) {
            min = getSharedPreferences().getInt(minExternalKey, minValue);
        }

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.preference_number_picker_dialog, null);

        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        // Initialize
        numberPicker.setMaxValue(max);
        numberPicker.setMinValue(min);
        numberPicker.setValue(Integer.parseInt(getPersistedString((new Integer(defaultValue).toString()))));
        numberPicker.setWrapSelectorWheel(false);

        // No keyboard popup
        EditText textInput = (EditText) numberPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
        if(textInput != null) {
            textInput.setCursorVisible(false);
            textInput.setFocusable(false);
            textInput.setFocusableInTouchMode(false);
        }

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            numberPicker.clearFocus();
            persistInt(numberPicker.getValue());
        }
    }

    public void setMinValue(int value) {
        numberPicker.setMinValue(value);
    }

    public void setMaxValue(int value) {
        numberPicker.setMaxValue(value);
    }
}