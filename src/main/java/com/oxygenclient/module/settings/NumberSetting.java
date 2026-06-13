package com.oxygenclient.module.settings;

public class NumberSetting extends Setting {
    private double value, min, max, increment;
    public NumberSetting(String name, String desc, double def, double min, double max, double inc) {
        super(name, desc);
        this.value = def;
        this.min = min;
        this.max = max;
        this.increment = inc;
    }
    public double getValue() { return value; }
    public void setValue(double v) { this.value = Math.max(min, Math.min(max, v)); }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }
}
