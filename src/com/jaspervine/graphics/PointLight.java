package com.jaspervine.graphics;

import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 18/12/14
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointLight {

    private BaseLight baseLight;
    private Vector3 position;
    private Attenuation attenuation;
    private float range;

    public PointLight(BaseLight baseLight, Vector3 position, Attenuation attenuation, float range) {
        this.baseLight = baseLight;
        this.position = position;
        this.attenuation = attenuation;
        this.range = range;
    }

    public BaseLight getBaseLight() {
        return baseLight;
    }

    public void setBaseLight(BaseLight baseLight) {
        this.baseLight = baseLight;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Attenuation getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }
}
