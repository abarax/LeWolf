package com.jaspervine.core;

import com.jaspervine.graphics.*;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;


/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class Game {


    private static Level level;


    public Game(){

       level = new Level("level1.png", "WolfCollection.png", new Player(new Vector3(8, 1.4375f, 8)));

//        player = new Player(new Vector3(0, 0, 0));





    }

    public void input() {

        level.input();

//        Transform.getCamera().input();


    }



    public void update() {


        level.update();

    }

    public void render() {

        level.render();


    }

    public static Level getLevel() {
        return level;
    }
}
