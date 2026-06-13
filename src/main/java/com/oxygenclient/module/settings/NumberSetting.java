package com.oxygenclient.module.settings;

public class NumberSetting extends Setting {
    private double value;
    private final double min;
    private final double max;
    private final double increment;
    
    public NumberSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }
    
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }
}
