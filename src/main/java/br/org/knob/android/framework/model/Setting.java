package br.org.knob.android.framework.model;

public class Setting<T> {
    public static final String TAG = "Setting";

    private String key;
    private T value;

    public Setting(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
