package com.jaspervine.math;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Matrix4 {

    private float [][] m;

    public Matrix4() {
        m = new float [4][4];
    }

    public Matrix4 Identity() {
        m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = 0;
        m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = 0;
        m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = 0;
        m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

        return this;
    }

    public Matrix4 initTranslation(float x, float y, float z) {
        m[0][0] = 1;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = x;
        m[1][0] = 0;    m[1][1] = 1;    m[1][2] = 0;    m[1][3] = y;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = 1;    m[2][3] = z;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return this;
    }

    public Matrix4 initRotation(float x, float y, float z) {

        Matrix4 rx = new Matrix4();
        Matrix4 ry = new Matrix4();
        Matrix4 rz = new Matrix4();

        float _x = (float) Math.toRadians(x);
        float _y = (float) Math.toRadians(y);
        float _z = (float) Math.toRadians(z);


        rx.m[0][0] = 1;                     rx.m[0][1] = 0;                     rx.m[0][2] = 0;                     rx.m[0][3] = 0;
        rx.m[1][0] = 0;                     rx.m[1][1] = (float)Math.cos(_x);   rx.m[1][2] = -(float) Math.sin(_x); rx.m[1][3] = 0;
        rx.m[2][0] = 0;                     rx.m[2][1] = (float)Math.sin(_x);   rx.m[2][2] = (float) Math.cos(_x);  rx.m[2][3] = 0;
        rx.m[3][0] = 0;                     rx.m[3][1] = 0;                     rx.m[3][2] = 0;                     rx.m[3][3] = 1;

        ry.m[0][0] = (float) Math.cos(_y);  ry.m[0][1] = 0;                     ry.m[0][2] = -(float) Math.sin(_y); ry.m[0][3] = 0;
        ry.m[1][0] = 0;                     ry.m[1][1] = 1;                     ry.m[1][2] = 0;                     ry.m[1][3] = 0;
        ry.m[2][0] = (float)Math.sin(_y);   ry.m[2][1] = 0;                     ry.m[2][2] = (float) Math.cos(_y);  ry.m[2][3] = 0;
        ry.m[3][0] = 0;                     ry.m[3][1] = 0;                     ry.m[3][2] = 0;                     ry.m[3][3] = 1;

        rz.m[0][0] = (float)Math.cos(_z);   rz.m[0][1] = -(float) Math.sin(_z); rz.m[0][2] = 0;                     rz.m[0][3] = 0;
        rz.m[1][0] = (float)Math.sin(_z);   rz.m[1][1] = (float) Math.cos(_z);  rz.m[1][2] = 0;                     rz.m[1][3] = 0;
        rz.m[2][0] = 0;                     rz.m[2][1] = 0;                     rz.m[2][2] = 1;                     rz.m[2][3] = 0;
        rz.m[3][0] = 0;                     rz.m[3][1] = 0;                     rz.m[3][2] = 0;                     rz.m[3][3] = 1;

        m = rz.multiply(ry.multiply(rx)).getM();

        return this;
    }

    public Matrix4 initScale(float x, float y, float z) {
        m[0][0] = x;    m[0][1] = 0;    m[0][2] = 0;    m[0][3] = 0;
        m[1][0] = 0;    m[1][1] = y;    m[1][2] = 0;    m[1][3] = 0;
        m[2][0] = 0;    m[2][1] = 0;    m[2][2] = z;    m[2][3] = 0;
        m[3][0] = 0;    m[3][1] = 0;    m[3][2] = 0;    m[3][3] = 1;

        return this;
    }

    public Matrix4 initProjection(float fov, float width, float height, float zNear, float zFar) {

        float aspectRatio = width / height;

        float tanHalfFOV = (float)Math.tan(Math.toRadians(fov)) / 2;

        float scaledFOV = tanHalfFOV * aspectRatio;

        float zRange = zNear - zFar;

        m[0][0] = 1 / scaledFOV;   m[0][1] = 0;                 m[0][2] = 0;                         m[0][3] = 0;
        m[1][0] = 0;                m[1][1] = 1 / tanHalfFOV;   m[1][2] = 0;                         m[1][3] = 0;
        m[2][0] = 0;                m[2][1] = 0;                m[2][2] = (-zNear - zFar )/ zRange;  m[2][3] = 2 * zFar * zNear / zRange;
        m[3][0] = 0;                m[3][1] = 0;                m[3][2] = 1;                         m[3][3] = 0;

        return this;
    }

    public Matrix4 initCamera(Vector3 forward, Vector3 up) {

        Vector3 f = forward.normalized();
        Vector3 right = up.normalized();

        right = right.crossProduct(f);

        Vector3 u = f.crossProduct(right);

        m[0][0] = right.getX();     m[0][1] = right.getY();     m[0][2] = right.getZ() ;    m[0][3] = 0;
        m[1][0] = u.getX();         m[1][1] = u.getY();         m[1][2] = u.getZ();         m[1][3] = 0;
        m[2][0] = f.getX();         m[2][1] = f.getY();         m[2][2] = f.getZ();         m[2][3] = 0;
        m[3][0] = 0;                m[3][1] = 0;                m[3][2] = 0;                m[3][3] = 1;

        return this;
    }

    public Matrix4 multiply(Matrix4 other) {
        Matrix4 result = new Matrix4();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.set(i, j, m[i][0] * other.get(0, j) +
                        m[i][1] * other.get(1, j) +
                        m[i][2] * other.get(2, j) +
                        m[i][3] * other.get(3, j));
            }

        }

        return result;
    }

    public float get(int x, int y){
        return m[x][y];
    }

    public void set(int x, int y, float value){
        m[x][y] = value;
    }

    public float[][] getM() {
        float[][] result =  new float [4][4];

        for (int i = 0; i < m.length; i++) {
            System.arraycopy(m[i], 0, result[i], 0, m[0].length);
        }

        return result;
    }

    public void setM(float[][] m) {
        this.m = m;
    }
}
