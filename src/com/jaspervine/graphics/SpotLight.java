package com.jaspervine.graphics;

import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 18/12/14
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpotLight {

    private PointLight pointLight;
    private Vector3 direction;
    private float cutoff;

    public SpotLight(PointLight pointLight, Vector3 direction, float cutoff) {
        this.pointLight = pointLight;
        this.direction = direction.normalized();
        this.cutoff = cutoff;
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction.normalized();
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }
}
