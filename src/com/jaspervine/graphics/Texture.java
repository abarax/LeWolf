package com.jaspervine.graphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 13/12/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class Texture {

    private int id;

    public Texture(int id) {
        this.id = id;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public int getID() {
        return this.id;
    }
}
