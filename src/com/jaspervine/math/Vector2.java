package com.jaspervine.math;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector2 {


    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public float cross(Vector2 other) {
        return this.getX() * other.getY() - this.getY() * other.getX();
    }

    public float length(){
        return (float)Math.sqrt(x * x + y * y);
    }

    public float dot(Vector2 v) {
        return x * v.getX() + y * v.getY();
    }

    public Vector2 normalize(){
        float length = this.length();
        x /= length;
        y /= length;

        return this;
    }

    public Vector2 rotate(float angle){

        double radians = Math.toRadians(angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        return new Vector2((float)(x * cos - y * sin), (float)(x * sin + y * cos));

    }

    public Vector2 add(Vector2 v) {

        return new Vector2(x + v.getX(), y + v.getY()) ;
    }

    public Vector2 add(float v) {

        return new Vector2(x + v, y + v) ;
    }

    public Vector2 subtract(Vector2 v) {

        return new Vector2(x - v.getX(), y - v.getY()) ;
    }

    public Vector2 subtract(float v) {

        return new Vector2(x - v, y - v) ;
    }

    public Vector2 multiply(Vector2 v) {

        return new Vector2(x * v.getX(), y * v.getY()) ;
    }

    public Vector2 multiply(float v) {

        return new Vector2(x * v, y * v) ;
    }

    public Vector2 divide(Vector2 v) {

        return new Vector2(x / v.getX(), y / v.getY()) ;
    }

    public Vector2 divide(float v) {

        return new Vector2(x / v, y / v) ;
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

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
