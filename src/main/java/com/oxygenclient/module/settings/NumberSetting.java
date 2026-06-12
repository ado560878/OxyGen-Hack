package com.oxygenclient.module.settings;

public class NumberSetting extends Setting {
    private double value;
    private final double min;
    private final double max;
    private final double increment;
    private final double defaultValue;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double increment) {
        super(name, description);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = Math.max(min, Math.min(max, value)); }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }
}
