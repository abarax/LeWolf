package com.jaspervine.math;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector3 {


    private float x;
    private float y;
    private float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length(){
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float dotProduct(Vector3 v) {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public Vector3 normalized(){
        float length = this.length();
        float _x = this.x / length;
        float _y = this.y / length;
        float _z = this.z / length;

        return new Vector3(_x, _y, _z);
    }

    public Vector3 crossProduct(Vector3 v) {

        float _x = y * v.getZ() - z * v.getY();
        float _y = z * v.getX() - x * v.getZ();
        float _z = x * v.getY() - y * v.getX();

        return new Vector3(_x, _y, _z);

    }

    public Vector3 rotate(float angle, Vector3 axis){

        float sinHalfAngle = (float) Math.sin(Math.toRadians(angle/2));
        float cosHalfAngle = (float) Math.cos(Math.toRadians(angle / 2));

        float rX = axis.getX() * sinHalfAngle;
        float rY = axis.getY() * sinHalfAngle;
        float rZ = axis.getZ() * sinHalfAngle;
        float rW = cosHalfAngle;

        Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
        Quaternion conjugate = rotation.conjugate();

        Quaternion w = rotation.multiply(this).multiply(conjugate);

        return new Vector3(w.getX(), w.getY(), w.getZ());

    }

    public Vector3 add(Vector3 v) {

        return new Vector3(x + v.getX(), y + v.getY(), z + v.getZ()) ;
    }

    public Vector3 add(float v) {

        return new Vector3(x + v, y + v, z + v) ;
    }

    public Vector3 subtract(Vector3 v) {

        return new Vector3(x - v.getX(), y - v.getY(), z - v.getZ()) ;
    }

    public Vector3 subtract(float v) {

        return new Vector3(x - v, y - v, z - v) ;
    }

    public Vector3 multiply(Vector3 v) {

        return new Vector3(x * v.getX(), y * v.getY(), z * v.getZ()) ;
    }

    public Vector3 multiply(float v) {

        return new Vector3(x * v, y * v, z * v) ;
    }

    public Vector3 divide(Vector3 v) {

        return new Vector3(x / v.getX(), y / v.getY(), z / v.getZ()) ;
    }

    public Vector3 divide(float v) {

        return new Vector3(x / v, y / v, z / v) ;
    }

    public Vector3 abs() {
        return new Vector3(Math.abs(x), Math.abs(y), Math.abs(z));
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

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Vector2 getXZ()  {
        return new Vector2(x, z);
    }
}
