package com.jaspervine.core;

import com.jaspervine.graphics.RenderUtil;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 9:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private static int HEIGHT = 600;
    private static int WIDTH = 800;
    private static String TITLE = "LEngine";

    private static double FRAMECAP = 5000.0;

    private boolean isRunning;

    private Game game;

    Main(){
        isRunning = false;
    }

    public void start() {

        if(isRunning) {
            return;
        }

        Window.createWindow(WIDTH, HEIGHT, TITLE);
        Input.init();

        System.out.println("OpenGL Version: " + RenderUtil.getOpenGLVersion());

        game = new Game();

        run();
    }

    public void stop() {
        if(!isRunning){
            return;
        }
        isRunning = false;
    }

    public void run() {

        isRunning = true;

        long lastTime = Time.getTime();
        double unprocessedTime = 0;
        final double frameTime = 1.0 / FRAMECAP;

        long frameCounter = 0;
        int frames = 0;

        try {

            // Run the rendering loop until the user has attempted to close
            // the window or has pressed the ESCAPE key.
            while ( isRunning ) {

                boolean render = false;

                long startTime = Time.getTime();
                long passedTime = startTime - lastTime;
                lastTime = startTime;

                unprocessedTime += passedTime / (double) Time.SECOND;
                frameCounter += passedTime;

                while(unprocessedTime > frameTime) {
                    render = true;
                    unprocessedTime -= frameTime;

                    if (Window.isCloseRequested() == GL_TRUE) {
                        stop();
                    }

                    Time.setDelta(frameTime);

                    game.input();
                    Input.update();

                    game.update();

                    if(frameCounter >= Time.SECOND) {
                        System.out.println(frames);
                        frames = 0;
                        frameCounter = 0;
                    }
                }

                if (render) {
                    render();
                    frames++;
                } else {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }
            }

        } finally {
            cleanUp();
        }
    }

    public void render() {
        RenderUtil.clearScreen();
        game.render();
        Window.render();
    }

    public void cleanUp() {
        Window.destroyWindow();
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Main().start();
    }

}

