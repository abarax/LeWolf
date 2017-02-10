package com.jaspervine.core;

import com.jaspervine.graphics.Mesh;
import com.jaspervine.graphics.Texture;
import com.jaspervine.graphics.Vertex;
import com.jaspervine.math.Vector3;
import org.newdawn.slick.opengl.TextureLoader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceLoader {

    public static String loadShader(String fileName) {
        StringBuilder shaderSource = new StringBuilder();

        BufferedReader shaderReader = null;

        try {
            shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));


            String line;
            while((line = shaderReader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }

        }   catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }

    public static Mesh loadMesh(String fileName){



        String  [] splitFilename = fileName.split("\\.");

        String extention = splitFilename[splitFilename.length - 1];

        if (!extention.equals("obj")) {
            System.err.print("File format not supported");
            new Exception().printStackTrace();
            System.exit(1);
        }

        Mesh mesh = new Mesh();

        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        BufferedReader meshReader = null;

        try {
            meshReader = new BufferedReader(new FileReader("./res/models/" + fileName));


            String line;
            while((line = meshReader.readLine()) != null) {

                List<String> tokens = new ArrayList<String>(Arrays.asList(line.split(" ")));

                tokens.removeAll(Arrays.asList("", null));

                if (tokens.get(0).equals("#") || tokens.size() == 0) {
                } else if (tokens.get(0).equals("v")) {
                   vertices.add(new Vertex(new Vector3(Float.valueOf(tokens.get(1)), Float.valueOf(tokens.get(2)), Float.valueOf(tokens.get(3)))));
                } else if (tokens.get(0).equals("f")) {
                    indices.add(Integer.parseInt(tokens.get(1)) -1);
                    indices.add(Integer.parseInt(tokens.get(2)) -1);
                    indices.add(Integer.parseInt(tokens.get(3)) -1);
                }
            }

        }   catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        Vertex [] verticesData = new Vertex[vertices.size()];

        vertices.toArray(verticesData);

        Integer [] indiciesData = new Integer[indices.size()];

        indices.toArray(indiciesData);

        mesh.addVertices(verticesData, Util.toIntArray(indiciesData));

        return mesh;


    }

    public static Texture loadTexture (String fileName) {

        try {

            String  [] splitFilename = fileName.split("\\.");

            String extention = splitFilename[splitFilename.length - 1];

            int id = TextureLoader.getTexture(extention, new FileInputStream(new File("./res/textures/" + fileName)), GL_NEAREST).getTextureID();

            return new Texture(id);


        }   catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }
}
