package com.jaspervine.graphics;

import com.jaspervine.core.Util;
import com.jaspervine.math.Vector3;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh
{
    private int vao;
    private int vbo;
    private int ibo;
    private int size;

    public Mesh()
    {

        size = 0;

        vbo = glGenBuffers();
        ibo = glGenBuffers();
        vao = glGenVertexArrays();

    }

    public void addVertices(Vertex[] vertices, int [] indices)
    {
        addVertices(vertices, indices, false);
    }

    public void addVertices(Vertex[] vertices, int [] indices, boolean calcNormals)
    {
        if (calcNormals){
            calcNormals(vertices, indices);
        }

        size = indices.length;

        glBindBuffer (GL_ARRAY_BUFFER, vbo);
        glBufferData (GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
        glBindVertexArray(vao);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);

        glEnableVertexAttribArray (0);
        glEnableVertexAttribArray (1);
        glEnableVertexAttribArray (2);

        glVertexAttribPointer (0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer (1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer (2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);

    }

    public void draw()
    {

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);

    }

    private void calcNormals(Vertex[] vertices, int [] indices) {

        for (int i = 0; i < indices.length; i += 3) {
              int i0 = indices[i];
              int i1 = indices[i + 1];
              int i2 = indices[i + 2];

            Vector3 v1 = vertices[i1].getPosition().subtract(vertices[i0].getPosition());
            Vector3 v2 = vertices[i2].getPosition().subtract(vertices[i0].getPosition());

            Vector3 normal = v1.crossProduct(v2).normalized();

            vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
            vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
            vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
        }

        for( int i = 0; i < vertices.length; i++){
            vertices[i].setNormal(vertices[i].getNormal().normalized());
        }

    }
}
