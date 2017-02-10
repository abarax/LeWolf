package com.jaspervine.core;

import com.jaspervine.graphics.Camera;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 20/12/14
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Player {

    private static final float MOVEMENT_SPEED = 4.0f;
    private static final float MOUSE_SENSITIVITY = 0.001f;
    private static final Vector3 ZERO_VECTOR = new Vector3(0,0,0);

    public static final float PLAYER_SIZE = 0.2f;


    private Camera camera;
    private boolean mouseLocked;

    boolean rotX = true;
    boolean rotY = true;

    double prevX = Window.getCenter().getX();
    double prevY;

    Vector2 deltaPos = Window.getCenter();

    private Vector3 movementVector;

    public Player(Vector3 position) {
        camera = new Camera(position, new Vector3(0, 0, 1), new Vector3(0, 1, 0));
    }

    public void input() {

        movementVector = ZERO_VECTOR;


        float rotateAmount = (float)( Time.getDelta());

        if(Input.getKey(GLFW_KEY_ESCAPE)){
            Input.setCursor(true);
            mouseLocked = false;
        }

        if (Input.getMouse(GLFW_MOUSE_BUTTON_1)) {
            Input.setMousePosition(Window.getCenter());
            glfwSetInputMode(Window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            // Input.setCursor(false);
            mouseLocked = true;
            prevX = Window.getCenter().getX();
            prevY = Window.getCenter().getY();

        }


        if(Input.getKey(GLFW_KEY_W))
            movementVector = movementVector.add(camera.getForward());
        if(Input.getKey(GLFW_KEY_S))
            movementVector = movementVector.subtract(camera.getForward());
        if(Input.getKey(GLFW_KEY_A))
            movementVector = movementVector.add(camera.getLeft());
        if(Input.getKey(GLFW_KEY_D))
            movementVector = movementVector.add(camera.getRight());
        if(Input.getKey(GLFW_KEY_SPACE))
            movementVector = movementVector.add(camera.getUp());



        if(Input.getKey(GLFW_KEY_UP))
            camera.rotateY(-rotateAmount);
        if(Input.getKey(GLFW_KEY_DOWN))
            camera.rotateY(rotateAmount);
        if(Input.getKey(GLFW_KEY_LEFT))
            camera.rotateX(-rotateAmount);
        if(Input.getKey(GLFW_KEY_RIGHT))
            camera.rotateX(rotateAmount);

        if (mouseLocked) {

            Vector2 input = Input.getMousePosition();

            deltaPos = new Vector2((input.getX() - Window.getCenter().getX()), (input.getY() - Window.getCenter().getY()));

            rotX = deltaPos.getX() < -1 || deltaPos.getX() > 1;
            rotY = deltaPos.getY() < -1 || deltaPos.getY() > 1;


            if(rotY) {
                camera.rotateY(deltaPos.getY() * MOUSE_SENSITIVITY);
            }
            if(rotX) {
                camera.rotateX(deltaPos.getX() * MOUSE_SENSITIVITY);
            }

            Input.setMousePosition(Window.getCenter());
            if(rotY || rotX) {
                Input.setMousePosition(Window.getCenter());
                prevX = Window.getCenter().getX();
                prevY = Window.getCenter().getY();
                rotY = false;
                rotX = false;
                deltaPos.setX(Window.getCenter().getX());
                deltaPos.setY(Window.getCenter().getY());
            }
        }
    }

    public void update() {

        float movementAmount = (float)( MOVEMENT_SPEED * Time.getDelta());

        movementVector.setY(0);
        if (movementVector.length() > 0)
            movementVector = movementVector.normalized();

        Vector3 oldPosition = camera.getPosition();
        Vector3 newPosition = oldPosition.add(movementVector.multiply(movementAmount));

        Vector3 collisionVector = Game.getLevel().checkCollisions(oldPosition, newPosition, PLAYER_SIZE, PLAYER_SIZE);

        movementVector = movementVector.multiply(collisionVector);

        camera.move(movementVector, movementAmount);

    }

    public void render() {

    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
