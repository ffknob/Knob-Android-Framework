package br.org.knob.android.framework.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.SeekBar;
import android.widget.TextView;

import br.org.knob.android.framework.R;

public class SeekBarPreference extends Preference implements SeekBar.OnSeekBarChangeListener {
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

        TypedArray seekBarType = context.obtainStyledAttributes(attrs, R.styleable.SeekBarPreference, 0, 0);

        minValue = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_minValue, 0);
        maxValue = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_maxValue, 0);
        interval = seekBarType.getInt(R.styleable.SeekBarPreference_seekBarPreference_interval, 0);
        units = seekBarType.getString(R.styleable.SeekBarPreference_seekBarPreference_units);

        defaultValue = seekBarType.getInt(Resources.getSystem().getIdentifier("Preference_defaultValue", "styleable", "android"), minValue);

        setWidgetLayoutResource(R.layout.preference_seekbar);

        seekBarType.recycle();
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);

        return view;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setMax(maxValue);
        seekBar.setProgress(progress);
        seekBar.setOnSeekBarChangeListener(this);

        setWidgetLayoutResource(R.layout.preference_seekbar);

        ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekbar_container);
        ViewParent oldContainer = seekBar.getParent();

            if (oldContainer != newContainer) {
                if (oldContainer != null) {
                    ((ViewGroup) oldContainer).removeView(seekBar);
                }

                newContainer.removeAllViews();
                newContainer.addView(seekBar, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        if (view != null && !view.isEnabled()) {
            seekBar.setEnabled(false);
        }

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

        persistInt(newValue);

        currentValueView.setText(String.valueOf(progress));
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
