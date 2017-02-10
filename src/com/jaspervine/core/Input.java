package com.jaspervine.core;

import com.jaspervine.math.Vector2;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Input {

    public static final int NUM_KEYCODES = 349;
    public static final int NUM_MOUSE_BUTTONS = 5;

    private static ArrayList<Integer> currentKeys = new ArrayList<Integer>();
    private static ArrayList<Integer> downKeys = new ArrayList<Integer>();
    private static ArrayList<Integer> upKeys = new ArrayList<Integer>();

    private static ArrayList<Integer> currentMouse = new ArrayList<Integer>();
    private static ArrayList<Integer> downMouse = new ArrayList<Integer>();
    private static ArrayList<Integer> upMouse = new ArrayList<Integer>();

    private static GLFWCursorPosCallback cursor_pos_callback;

    // Mouse positions
    private static int mouseX, mouseY;
    private static int mouseDX, mouseDY;

    public static void init() {

        mouseX = mouseY = mouseDX = mouseDY = 0;

        glfwSetCursorPosCallback(Window.getWindow(), cursor_pos_callback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {

                // Add delta of x and y mouse coordinates
                mouseDX += (int)xpos - mouseX;
                mouseDY += (int)xpos - mouseY;
                // Set new positions of x and y
                mouseX = (int) xpos;
                mouseY = (int) ypos;
            }
        });
    }

    public static void update() {

        upKeys.clear();

        for(int i = 0; i < NUM_KEYCODES; i++) {
            if(!getKey(i) && currentKeys.contains(i)) {
                upKeys.add(i);
            }
        }
        upMouse.clear();

        for(int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
            if(!getMouse(i) && currentMouse.contains(i)) {
                upMouse.add(i);
            }
        }

        downKeys.clear();

        for(int i = 0; i < NUM_KEYCODES; i++) {
            if(getKey(i) && !currentKeys.contains(i)) {
                downKeys.add(i);
            }
        }

        downMouse.clear();

        for(int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
            if(getMouse(i) && !currentMouse.contains(i)) {
                downMouse.add(i);
            }
        }

        currentKeys.clear();

        for(int i = 0; i < NUM_KEYCODES; i++) {
            if(getKey(i)) {
                currentKeys.add(i);
            }
        }

        currentMouse.clear();

        for(int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
            if(getMouse(i)){
                currentMouse.add(i);
            }
        }


    }

    public static boolean getKey(int keyCode) {
        return glfwGetKey(Window.getWindow(), keyCode) == GLFW_PRESS;
    }

    public static boolean getKeyDown(int keyCode) {
       return downKeys.contains(keyCode);
    }

    public static boolean getKeyUp(int keyCode) {
        return upKeys.contains(keyCode);
    }

    public static boolean getMouse(int mouseButton){
        return glfwGetMouseButton(Window.getWindow(), mouseButton) == GLFW_PRESS;
    }

    public static boolean getMouseDown(int mouseButton) {
        return downMouse.contains(mouseButton);
    }

    public static boolean getMouseUp(int mouseButton) {
        return upMouse.contains(mouseButton);
    }

    public static int getDX(){
        // Return mouse delta x and set delta x to 0
        return mouseDX | (mouseDX = 0);
    }

    public static int getDY(){
        // Return mouse delta y and set delta y to 0
        return mouseDY | (mouseDY = 0);
    }

    public static Vector2 getMouseDelta() {
        return new Vector2(Input.getDX(), Input.getDY());
    }

    public static Vector2 getMousePosition() {
        return new Vector2(mouseX, mouseY);
    }

    public static void setMousePosition(Vector2 position) {
        glfwSetCursorPos(Window.getWindow(), position.getX(), position.getY());
    }

    public static void setCursor(boolean enable) {
        if (!enable) {
            glfwSetInputMode(Window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        } else {
            glfwSetInputMode(Window.getWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }
}
