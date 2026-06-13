package com.oxygenclient.module.settings;

public class NumberSetting extends Setting {
    private double value;
    private double min;
    private double max;
    private double increment;
    
    public NumberSetting(String name, String description, double defaultValue, double min, double max, double increment) {
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
