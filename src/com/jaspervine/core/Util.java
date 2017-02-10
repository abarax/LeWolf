package com.jaspervine.core;

import com.jaspervine.graphics.Vertex;
import com.jaspervine.math.Matrix4;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 2:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static FloatBuffer createFloatbuffer(int size) {
        return BufferUtils.createFloatBuffer(size);
    }

    public static IntBuffer createIntBugffer(int size) {
        return BufferUtils.createIntBuffer(size);
    }

    public static IntBuffer createFlippedBuffer(int... values){
        IntBuffer buffer = Util.createIntBugffer(values.length);

        buffer.put(values);
        buffer.flip();

        return buffer;

    }

    public static FloatBuffer createFlippedBuffer(Vertex[] vertices){
        FloatBuffer buffer = Util.createFloatbuffer(vertices.length * Vertex.SIZE);

        for( int i = 0; i < vertices.length; i++) {
            buffer.put(vertices[i].getPosition().getX());
            buffer.put(vertices[i].getPosition().getY());
            buffer.put(vertices[i].getPosition().getZ());
            buffer.put(vertices[i].getTextureCoordinates().getX());
            buffer.put(vertices[i].getTextureCoordinates().getY());
            buffer.put(vertices[i].getNormal().getX());
            buffer.put(vertices[i].getNormal().getY());
            buffer.put(vertices[i].getNormal().getZ());
        }

        buffer.flip();


        return buffer;

    }

    public static FloatBuffer createFlippedBuffer(Matrix4 value) {

        FloatBuffer buffer = createFloatbuffer(4 * 4);

        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++){
                buffer.put(value.get(i, j));
            }
        }
        buffer.flip();

        return buffer;
    }

    public static int[] toIntArray(Integer [] array) {
        int [] result = new int [array.length];

        for(int i = 0; i < array.length; i++)
            result[i] = array[i];

        return result;
    }
}
