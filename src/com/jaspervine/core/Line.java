package com.jaspervine.core;

import com.jaspervine.graphics.*;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import java.nio.FloatBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * User: abarax
 * Date: 26/12/14
 * Time: 1:21 AM
 */
public class Line {

    private Transform transform;
    private int vao;
    private int vbo;
    private static final int size = 2;

    public Line(Transform transform, Vector2 start, Vector2 end) {

        vbo = glGenBuffers();
        vao = glGenVertexArrays();

        this.transform = transform;

        FloatBuffer buffer = Util.createFloatbuffer(6);

        buffer.put(start.getX());
        buffer.put(0.5f);
        buffer.put(start.getY());

        buffer.put(end.getX());
        buffer.put(0.5f);
        buffer.put(end.getY());

        buffer.flip();

        glBindBuffer (GL_ARRAY_BUFFER, vbo);
        glBufferData (GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindVertexArray(vao);

        glEnableVertexAttribArray (0);

        glVertexAttribPointer (0, 3, GL_FLOAT, false, 6, 0);

    }

    public void update() {


    }

    public void input () {

    }

    public void render() {
        Shader shader = Level.shader;

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), new Material(ResourceLoader.loadTexture("bricks.jpg")));
        shader.setUniform("colour", new Vector3(1.0f, 0, 0));
        glBindVertexArray(vao);
        glDrawArrays(GL_LINES, 0, 2);
    }

}
