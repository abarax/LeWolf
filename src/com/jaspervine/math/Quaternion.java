package com.jaspervine.math;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Quaternion {
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion normalize(){
        float length = this.length();
        x /= length;
        y /= length;
        z /= length;
        w /= length;

        return this;
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion multiply(Quaternion q) {
        float _x = x * q.getW() + w * q.getX() + y * q.getZ() - z * q.getY();
        float _y = y * q.getW() + w * q.getY() + z * q.getX() - x * q.getZ();
        float _z = z * q.getW() + w * q.getZ() + x * q.getY() - y * q.getX();
        float _w = w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ();
        return new Quaternion(_x, _y, _z, _w);
    }

    public Quaternion multiply(Vector3 v) {
        float _x = w * v.getX() + y * v.getZ() - z * v.getY();
        float _y = w * v.getY() + z * v.getX() - x * v.getZ();
        float _z = w * v.getZ() + x * v.getY() - y * v.getX();
        float _w = -x * v.getX() - y * v.getY() - z * v.getZ();
        return new Quaternion(_x, _y, _z, _w);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }
}
