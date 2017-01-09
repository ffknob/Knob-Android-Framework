package br.org.knob.android.framework.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import br.org.knob.android.framework.R;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "SeekBarPreference";

    private static final String ATTR_MIN_VALUE = "minValue";
    private static final String ATTR_MAX_VALUE = "maxValue";

    private SeekBar seekBar;

    private int progress, minValue, maxValue, defaultValue, currentValue, interval;
    private String units;

    private TextView currentValueView;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Make custom preference look like android's native preferences
        //setWidgetLayoutResource(defStyle); //Resources.getSystem().getIdentifier("dialogPreferenceStyle", "attr", "android"));

        TypedArray dialogType = context.obtainStyledAttributes(attrs, new int[]{Resources.getSystem().getIdentifier("DialogPreference", "styleable", "android")}, 0, 0);
        TypedArray seekBarType = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, 0, 0);

        minValue = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_minValue, 0);
        maxValue = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_maxValue, 0);
        interval = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_interval, 0);
        units = seekBarType.getString(R.styleable.SeekBarPreference_seekBarPreference_units);

        defaultValue = dialogType.getInt(Resources.getSystem().getIdentifier("Preference_defaultValue", "styleable", "android"), minValue);

        dialogType.recycle();
        seekBarType.recycle();
    }

    @Override
    protected View onCreateDialogView() {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.preference_seekbar, null);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(maxValue);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(this);

        // Current value label
        currentValueView = (TextView) view.findViewById(R.id.seekbar_current_value);
        String currentValueText = units != null ? String.valueOf(progress) + units : String.valueOf(progress);
        currentValueView.setText(currentValueText);

        // Min value label
        TextView minValueView = (TextView) view.findViewById(R.id.seekbar_min_value);
        minValueView.setText(String.valueOf(minValue));

        // Max value label
        TextView maxValueView = (TextView) view.findViewById(R.id.seekbar_max_value);
        maxValueView.setText(String.valueOf(maxValue));

        seekBar.setProgress(currentValue);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int newValue = progress + minValue;

        if (newValue > maxValue)
            newValue = maxValue;
        else if (newValue < minValue)
            newValue = minValue;
        else if (interval != 1 && newValue % interval != 0)
            newValue = Math.round(((float) newValue) / interval) * interval;

        if (!callChangeListener(newValue)) {
            seekBar.setProgress(currentValue - minValue);
            return;
        }

        currentValue = newValue;

        currentValueView.setText(String.valueOf(progress) + units);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(currentValue);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            currentValue = getPersistedInt(currentValue);
        } else {
            int temp = (Integer) defaultValue;

            setValue(temp);
            currentValue = temp;
        }
    }

    public void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != progress) {
            progress = value;
            notifyChanged();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }
}
