package com.jaspervine.graphics;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 18/12/14
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Attenuation {
    private float constant;
    private float linear;
    private float exponent;

    public Attenuation(float constant, float linear, float exponent) {
        this.constant = constant;
        this.linear = linear;
        this.exponent = exponent;
    }

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getExponent() {
        return exponent;
    }

    public void setExponent(float exponent) {
        this.exponent = exponent;
    }
}
