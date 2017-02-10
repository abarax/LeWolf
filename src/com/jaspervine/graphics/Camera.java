package com.jaspervine.graphics;

import com.jaspervine.core.Input;
import com.jaspervine.core.Time;
import com.jaspervine.core.Window;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 13/12/14
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class Camera {

    public static final Vector3 yAxis = new Vector3(0, 1, 0);

    private Vector3 position;
    private Vector3 forward;
    private Vector3 up;

    private boolean mouseLocked = false;
    Vector2 centerPosition = new Vector2(Window.getWidth()/2, Window.getHeight()/2);
    boolean rotX = true;
    boolean rotY = true;

    double prevX = centerPosition.getX();
    double prevY;

    Vector2 deltaPos = centerPosition;

    float horizontalAngle;
    float verticalAngle;

    private static final float MaxVerticalAngle = 85.0f; //must be less than 90 to avoid gimbal lock

    public Camera() {
        this(new Vector3(1, 1, -2), new Vector3(0,0,1), new Vector3(0,1,0));
    }

    public Camera (Vector3 position, Vector3 forward, Vector3 up) {
        this.position = position;
        this.forward = forward.normalized();
        this.up = up.normalized();
    }

    public void input() {
        float movementAmount = (float)( 10.0f * Time.getDelta());
        float sensitivity = 0.0005f;

        float rotateAmount = (float)( 100.0f * Time.getDelta());

        if(Input.getKey(GLFW_KEY_ESCAPE)){
            Input.setCursor(true);
            mouseLocked = false;
        }

        if (Input.getMouse(GLFW_MOUSE_BUTTON_1)) {
            Input.setMousePosition(centerPosition);
            glfwSetInputMode(Window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
           // Input.setCursor(false);
            mouseLocked = true;
            prevX = centerPosition.getX();
            prevY = centerPosition.getY();

        }

//        if (Input.getMouse(GLFW_MOUSE_BUTTON_1)) {
//            System.out.println("Mouse Position: " + Input.getMousePosition());
//        }

        if(Input.getKey(GLFW_KEY_W))
            move(getForward(), movementAmount);
        if(Input.getKey(GLFW_KEY_S))
            move(getForward(), -movementAmount);
        if(Input.getKey(GLFW_KEY_A))
            move(getLeft(), movementAmount);
        if(Input.getKey(GLFW_KEY_D))
            move(getRight(), movementAmount);

        if(Input.getKey(GLFW_KEY_SPACE))
            move(getUp(), movementAmount);

        if(Input.getKey(GLFW_KEY_UP))
            rotateY(-rotateAmount);
        if(Input.getKey(GLFW_KEY_DOWN))
            rotateY(rotateAmount);
        if(Input.getKey(GLFW_KEY_LEFT))
            rotateX(-rotateAmount);
        if(Input.getKey(GLFW_KEY_RIGHT))
            rotateX(rotateAmount);

        if (mouseLocked) {

            Vector2 input = Input.getMousePosition();

            deltaPos = new Vector2((input.getX() - centerPosition.getX()), (input.getY() - centerPosition.getY()));

            rotX = deltaPos.getX() < -1 || deltaPos.getX() > 1;
            rotY = deltaPos.getY() < -1 || deltaPos.getY() > 1;


            if(rotY) {
                rotateY(deltaPos.getY() * sensitivity);
            }
            if(rotX) {
                rotateX(deltaPos.getX() * sensitivity );
            }

            Input.setMousePosition(centerPosition);
            if(rotY || rotX) {
                Input.setMousePosition(centerPosition);
                prevX = centerPosition.getX();
                prevY = centerPosition.getY();
                rotY = false;
                rotX = false;
                deltaPos.setX(centerPosition.getX());
                deltaPos.setY(centerPosition.getY());
            }
        }


    }

    public void move(Vector3 direction, float amount) {
        position = position.add(direction.multiply(amount));
    }

    public void rotateX(float angle) {

        float newAngle = normalizeHorizontalAngle(angle);
        Vector3 horizontal = yAxis.crossProduct(forward).normalized();

        this.forward = forward.rotate(newAngle, yAxis).normalized();
        this.up = forward.crossProduct(horizontal).normalized();
    }

    public void rotateY(float angle) {

        float newAngle = normalizeVerticalAngle(angle);
        Vector3 horizontal = yAxis.crossProduct(forward).normalized();

        this.forward = forward.rotate(newAngle, horizontal).normalized();
        this.up = forward.crossProduct(horizontal).normalized();
    }

    public Vector3 getLeft(){
        Vector3 left = forward.crossProduct(up).normalized();
        return left;
    }

    public Vector3 getRight(){
        Vector3 right = up.crossProduct(forward).normalized();
        return right;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getForward() {
        return forward;
    }

    public void setForward(Vector3 forward) {
        this.forward = forward;
    }

    public Vector3 getUp() {
        return up;
    }

    public void setUp(Vector3 up) {
        this.up = up;
    }

    private float normalizeHorizontalAngle(float angle) {
        angle = angle % 360.0f;

        if(angle < 0.0f)
            angle += 360.0f;

        return angle;
    }

    private float normalizeVerticalAngle(float angle) {

        if(angle > MaxVerticalAngle){
            angle = MaxVerticalAngle;
        }
        else if(angle < -MaxVerticalAngle) {
            angle = -MaxVerticalAngle;
        }

        return angle;
    }
}
