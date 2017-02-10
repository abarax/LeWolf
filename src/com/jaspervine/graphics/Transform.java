package com.jaspervine.graphics;

import com.jaspervine.math.Matrix4;
import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 12/12/14
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transform {

    private static float zNear;
    private static float zFar;
    private static float width;
    private static float height;
    private static float fov;

    private static Camera camera;


    private Vector3 translation;
    private Vector3 rotation;
    private Vector3 scale;

    public Transform() {
        translation = new Vector3(1, 1, 1);
        rotation = new Vector3(0, 0, 0);
        scale = new Vector3(1, 1 , 1);
    }

    public Matrix4 getTransformation() {

        Matrix4 translationMatrix = new Matrix4().initTranslation(translation.getX(), translation.getY(), translation.getZ());
        Matrix4 rotationMatrix = new Matrix4().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
        Matrix4 scaleMatrix = new Matrix4().initScale(scale.getX(), scale.getY(), scale.getZ());


        return translationMatrix.multiply(rotationMatrix.multiply(scaleMatrix));

    }

    public Matrix4 getProjectedTransformation() {

        Matrix4 transformationMatrix = getTransformation();
        Matrix4 projectionMatrix = new Matrix4().initProjection(fov, width, height, zNear, zFar);
        Matrix4 cameraRotationMatrix = new Matrix4().initCamera(camera.getForward(), camera.getUp());
        Matrix4 cameraTranslationMatrix = new Matrix4().initTranslation(
                -camera.getPosition().getX(),
                -camera.getPosition().getY(),
                -camera.getPosition().getZ());

        return projectionMatrix.multiply(cameraRotationMatrix.multiply(cameraTranslationMatrix.multiply(transformationMatrix)));

    }

    public static void setProjection(float fov, float width, float height, float zNear, float zFar){
        Transform.fov = fov;
        Transform.width = width;
        Transform.height = height;
        Transform.zNear = zNear;
        Transform.zFar = zFar;
    }

    public Vector3 getTranslation() {
        return translation;
    }

    public void setTranslation(Vector3 translation) {
        this.translation = translation;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public void setTranslation(float x, float y, float z) {
        this.translation = new Vector3(x, y, z);
    }

    public void setRotation(float x, float y, float z){
        this.rotation = new Vector3(x, y, z);
    }

    public void setScale(float x, float y, float z){
        this.scale = new Vector3(x, y, z);
    }

    public Vector3 getScale() {
        return scale;
    }

    public void setScale(Vector3 scale) {
        this.scale = scale;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static void setCamera(Camera camera) {
        Transform.camera = camera;
    }

}
