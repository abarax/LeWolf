package com.jaspervine.graphics;

import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 14/12/14
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class DirectionalLight {

    private BaseLight baseLight;
    private Vector3 direction;

    public DirectionalLight(BaseLight baseLight, Vector3 direction) {
        this.baseLight = baseLight;
        this.direction = direction.normalized();
    }

    public BaseLight getBaseLight() {
        return baseLight;
    }

    public void setBaseLight(BaseLight baseLight) {
        this.baseLight = baseLight;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }
}
