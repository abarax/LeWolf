package com.jaspervine.core;

import com.jaspervine.graphics.*;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import java.util.ArrayList;


import static org.lwjgl.glfw.GLFW.*;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 20/12/14
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Level {

    private static final float SPOT_WIDTH = 1;
    private static final float SPOT_HEIGHT = 1;
    private static final float SPOT_LENGTH = 1;
    private static final int NUM_TEXTURES = 16;
    private static final int NUM_TEXTURES_EXP = 4;

    private Bitmap level;
    public static Shader shader;
    private Material material;
    private Mesh mesh;
    private Transform transform;
    private Player player;
    private ArrayList<Door> doors;
    private ArrayList<Vector2> collisionPositionStart;
    private ArrayList<Vector2> collisionPositionEnd;

    //Temp
    private Monster monster;
    private Line line;

    public Level(String levelName, String textureName, Player player) {

        this.level = new Bitmap(levelName).FlipY();
        this.mesh = new Mesh();
        this.player = player;
        this.material = new Material(ResourceLoader.loadTexture(textureName));
        this.doors = new ArrayList<Door>();
        this.transform = new Transform();
        this.collisionPositionStart = new ArrayList<Vector2>();
        this.collisionPositionEnd = new ArrayList<Vector2>();

        Transform.setProjection(50f, Window.getWidth(), Window.getHeight(), 0.01f, 100f);
        Transform.setCamera(player.getCamera());

        generateLevel();

        Level.shader = BasicShader.getInstance();


        Transform t = new Transform();

        t.setTranslation(8, 1, 8);

        monster = new Monster(t);

        line = new Line(transform, new Vector2(8, 8), new Vector2(9, 9));


    }

    public void openDoor(Vector3 position) {
        for (Door d: doors) {
            if (d.getTransform().getTranslation().subtract(position).length() < Door.DISTANCE_TO_OPEN) {
                d.open();
            }
        }
    }

    public void input() {

        if (Input.getKeyDown(GLFW_KEY_E)) {
            openDoor(player.getCamera().getPosition());
        }
         player.input();


    }

    public void update() {
        player.update();
        for( Door d : doors) {
            d.update();
        }

        monster.update();

    }

    public void render() {

        shader.bind();
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();

        for( Door d : doors) {
            d.render();
        }

        player.render();
        monster.render();
        line.render();

    }

    private float [] calculateTextureCoordinates(int value) {
        int texX = value / NUM_TEXTURES;
        int texY = texX % NUM_TEXTURES_EXP;
        texX /= NUM_TEXTURES_EXP;

        float xHigh = 1f - (float)texX/NUM_TEXTURES_EXP;
        float xLow = xHigh - (1f/NUM_TEXTURES_EXP);
        float yLow = 1f - (float)texY/NUM_TEXTURES_EXP;
        float yHigh = yLow - (1f/NUM_TEXTURES_EXP);

        return new float[] {xHigh, xLow, yHigh, yLow};
    }

    private void addFace(ArrayList<Integer> indices, int startLocation, boolean direction) {
        if (direction) {
            indices.add(startLocation + 2);
            indices.add(startLocation + 1);
            indices.add(startLocation + 0);
            indices.add(startLocation + 3);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
        } else {
            indices.add(startLocation + 0);
            indices.add(startLocation + 1);
            indices.add(startLocation + 2);
            indices.add(startLocation + 0);
            indices.add(startLocation + 2);
            indices.add(startLocation + 3);
        }
    }


    public Vector3 checkCollisions(Vector3 oldPosition, Vector3 newPosition, float objectWidth, float objectsLength){
        Vector2 collisionVector = new Vector2(1, 1);
        Vector3 movementVector = newPosition.subtract(oldPosition);

        if (movementVector.length() > 0) {

            Vector2 blockSize = new Vector2(SPOT_WIDTH, SPOT_LENGTH);
            Vector2 objectSize = new Vector2(objectWidth, objectsLength);

            Vector2 oldPosition2 = new Vector2(oldPosition.getX(), oldPosition.getZ());
            Vector2 newPosition2 = new Vector2(newPosition.getX(), newPosition.getZ());

            for (int  i = 0; i < level.getWidth(); i++) {
                for(int j = 0; j < level.getHeight(); j++) {
                    if ((level.getPixel(i, j) & 0xFFFFFF) == 0) {
                        collisionVector = collisionVector.multiply(
                                rectCollide(oldPosition2, newPosition2, objectSize, blockSize, blockSize.multiply(new Vector2(i, j))));
                    }
                }
            }

            for (Door d : doors) {
                collisionVector = collisionVector.multiply(
                    rectCollideDoor(oldPosition2, newPosition2, objectSize, d.getSize(), d.getTransform().getTranslation().getXZ()));
            }

        }

        return new Vector3(collisionVector.getX(), 0, collisionVector.getY());

    }

    public Vector2 checkIntersections(Vector2 lineStart, Vector2 lineEnd) {
        Vector2 nearestIntersection = new Vector2(Float.MAX_VALUE, Float.MAX_VALUE);

        for (int i = 0; i < collisionPositionStart.size(); i++) {
            Vector2 collisionVector = lineIntersect(lineStart, lineEnd, collisionPositionStart.get(i), collisionPositionEnd.get(i));

            if (collisionVector != null &&  (nearestIntersection == null ||
                    nearestIntersection.subtract(lineStart).length() > collisionVector.subtract(lineStart).length())) {
                nearestIntersection = collisionVector;

            }
        }

        for (Door d : doors) {
            Vector2 collisionVector = lineIntersect(lineStart, lineEnd, d.getTransform().getTranslation().getXZ(), d.getSize());
            nearestIntersection = findNearestVector(nearestIntersection, collisionVector, lineStart);
        }

        return nearestIntersection;
    }

    private Vector2 findNearestVector (Vector2 currentNearest, Vector2 candidate, Vector2 positionRelativeTo) {

        if (candidate != null && (currentNearest == null ||
                currentNearest.subtract(positionRelativeTo).length() > candidate.subtract(positionRelativeTo).length())) {
            return candidate;
        }

        return currentNearest;
    }

    public Vector2 lineIntersectRect(Vector2 lineStart, Vector2 lineEnd, Vector2 rectPos, Vector2 rectSize) {

        Vector2 result = null;

        Vector2 collisionVector = lineIntersect(lineStart, lineEnd, rectPos, new Vector2(rectPos.getX() + rectSize.getX(), rectPos.getY()));
        result = findNearestVector(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, rectPos, new Vector2(rectPos.getX(), rectSize.getY() + rectPos.getY()));
        result = findNearestVector(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, new Vector2(rectPos.getX(), rectSize.getY() + rectPos.getY()), rectPos.add(rectSize));
        result = findNearestVector(result, collisionVector, lineStart);

        collisionVector = lineIntersect(lineStart, lineEnd, new Vector2(rectPos.getX() + rectSize.getX(), rectPos.getY()), rectPos.add(rectSize));
        result = findNearestVector(result, collisionVector, lineStart);


        return result;
    }

    private Vector2 lineIntersect(Vector2 lineStart1, Vector2 lineEnd1, Vector2 lineStart2, Vector2 lineEnd2) {

        Vector2 line1 = lineEnd1.subtract(lineStart1);
        Vector2 line2 = lineEnd2.subtract(lineStart2);


        float cross = line1.cross(line2);

        if (cross == 0)
                return null;

        Vector2 distanceBetweenStart = lineStart2.subtract(lineStart1);

        float a = distanceBetweenStart.cross(line2) / cross;
        float b = distanceBetweenStart.cross(line1) / cross;

        if (0.0f < a && a < 1.0f && 0.0f < b && b < 1.0f) {
            return lineStart1.add(line1.multiply(a));
        }

        return null;
    }

    private Vector2 rectCollideDoor(Vector2 oldPosition, Vector2 newPosition, Vector2 size1, Vector2 size2, Vector2 position2) {

        Vector2 result = new Vector2(0, 0);


        if (
                newPosition.getX() + size1.getX() < position2.getX()||
                        newPosition.getX() - size1.getX() > position2.getX() + size2.getX() ||

                        oldPosition.getY() + size1.getY() < position2.getY() ||
                        oldPosition.getY() - size1.getY() > position2.getY() + size2.getY())
        {
            result.setX(1);
        }

        if (
                oldPosition.getX() + size1.getX() < position2.getX() ||
                        oldPosition.getX() - size1.getX() > position2.getX() + size2.getX() ||

                        newPosition.getY() + size1.getY() < position2.getY() ||
                        newPosition.getY() - size1.getY() > position2.getY() + size2.getY()
                ){
            result.setY(1);
        }

        return result;

    }

    private Vector2 rectCollide(Vector2 oldPosition, Vector2 newPosition, Vector2 size1, Vector2 size2, Vector2 position2) {

        Vector2 result = new Vector2(0, 0);


        if (
            newPosition.getX() + size1.getX() < position2.getX() + size2.getX()||
            newPosition.getX() - size1.getX() > position2.getX() + (2 * size2.getX()) ||

            oldPosition.getY() + size1.getY() < position2.getY() + size2.getX() ||
            oldPosition.getY() - size1.getY() > position2.getY() + (2 * size2.getY()))
        {
            result.setX(1);
        }

        if (
            oldPosition.getX() + size1.getX() < position2.getX() + size2.getX()||
            oldPosition.getX() - size1.getX() > position2.getX() + (2 * size2.getX())||

            newPosition.getY() + size1.getY() < position2.getY() + size2.getX() ||
            newPosition.getY() - size1.getY() > position2.getY() + (2 * size2.getY())
        ){
            result.setY(1);
        }

        return result;

    }

    private void addSpecial(int blueValue, int x, int y) {

        if (blueValue == 16) {
            addDoor(x, y);
        }

    }

    private void addDoor(int x, int y) {

        Transform doorTransform = new Transform();

        boolean yDoor =  (level.getPixel(x- 1, y) & 0xFFFFFF) == 0 && (level.getPixel(x + 1, y) & 0xFFFFFF) == 0;
        boolean xDoor =  (level.getPixel(x, y - 1) & 0xFFFFFF) == 0 && (level.getPixel(x, y + 1) & 0xFFFFFF) == 0;

        Vector3 openPosition = null;

        if (xDoor) {
            doorTransform.setTranslation(x + 1.5f, 1, y+ 1);
            doorTransform.setRotation(0, 90, 0);
            openPosition = doorTransform.getTranslation().subtract(new Vector3(0.0f, 0.0f, 0.9f));
        } else if (yDoor) {
            doorTransform.setTranslation(x + 1, 1, y+ 1.5f);
            openPosition = doorTransform.getTranslation().subtract(new Vector3(0.9f, 0.0f, 0.0f));

        } else {
            System.err.println("Door placed at x: " + x + " y: " + y + " is invalid");
        }


        doors.add(new Door(doorTransform, material,   openPosition  ));
    }

    private void generateLevel() {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        ArrayList<Integer> indices = new ArrayList<Integer>();


        for(int i = 0; i < level.getWidth(); i++) {
            for(int j = 0; j < level.getHeight(); j++) {
                if ((level.getPixel(i, j) & 0xFFFFFF) == 0) {
                    continue;
                }

                addSpecial(level.getPixel(i, j) & 0x0000FF, i, j);


                float [] textureCoordinates = calculateTextureCoordinates((level.getPixel(i, j) & 0x00FF00) >> 8);

                float xHigh = textureCoordinates[0];
                float xLow = textureCoordinates[1];
                float yHigh = textureCoordinates[2];
                float yLow = textureCoordinates[3];

                //GENERATE FLOOR

                addFace(indices, vertices.size(), true);

                vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xLow, yLow)));
                vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, (j + 1) * SPOT_LENGTH), new Vector2(xLow, yHigh)));

                //GENERATE CEILING
                addFace(indices, vertices.size(), false);

                vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2(xLow, yLow)));
                vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xLow, yHigh)));


                //GENERATE WALLS
                textureCoordinates = calculateTextureCoordinates((level.getPixel(i, j) & 0xFF0000) >> 16);

                xHigh = textureCoordinates[0];
                xLow = textureCoordinates[1];
                yHigh = textureCoordinates[2];
                yLow = textureCoordinates[3];

                if ((level.getPixel(i, j - 1) & 0xFFFFFF) == 0) {
                    addFace(indices, vertices.size(), false);

                    collisionPositionStart.add(new Vector2(i * SPOT_WIDTH, j * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2((i + 1) * SPOT_WIDTH, j * SPOT_LENGTH));

                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xLow, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, j * SPOT_LENGTH), new Vector2(xLow, yHigh)));
                }

                if ((level.getPixel(i, j + 1) & 0xFFFFFF) == 0) {
                    addFace(indices, vertices.size(), true);

                    collisionPositionStart.add(new Vector2(i * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));

                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, (j+ 1) * SPOT_LENGTH), new Vector2(xLow, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xLow, yHigh)));
                }

                if ((level.getPixel(i - 1, j) & 0xFFFFFF) == 0) {
                    addFace(indices, vertices.size(), true);

                    collisionPositionStart.add(new Vector2(i * SPOT_WIDTH, j * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2(i * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));

                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xLow, yLow)));
                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, 0, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                    vertices.add(new Vertex(new Vector3(i * SPOT_WIDTH, SPOT_HEIGHT, j  * SPOT_LENGTH), new Vector2(xLow, yHigh)));
                }

                if ((level.getPixel(i + 1, j) & 0xFFFFFF) == 0) {
                    addFace(indices, vertices.size(), false);

                    collisionPositionStart.add(new Vector2((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));
                    collisionPositionEnd.add(new Vector2((i + 1) * SPOT_WIDTH, (j + 1) * SPOT_LENGTH));

                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, j * SPOT_LENGTH), new Vector2(xLow, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, 0, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yLow)));
                    vertices.add(new Vertex(new Vector3((i + 1) * SPOT_WIDTH, SPOT_HEIGHT, (j + 1) * SPOT_LENGTH), new Vector2(xHigh, yHigh)));
                    vertices.add(new Vertex(new Vector3((i + 1)  * SPOT_WIDTH, SPOT_HEIGHT, j  * SPOT_LENGTH), new Vector2(xLow, yHigh)));
                }

            }
        }

        Vertex[] vertArray = new Vertex[vertices.size()];
        Integer [] intArray = new Integer[indices.size()];

        vertices.toArray(vertArray);
        indices.toArray(intArray);

        for (Vertex v : vertArray ){
            System.out.println("v " + v.getPosition().getX() + " " + v.getPosition().getY() + " " + v.getPosition().getZ());
        }

        for (int i = 0; i < intArray.length; i+=3) {
            System.out.println("f " + intArray[i] + " " + intArray[i + 1] + " " + intArray[i + 2]);
        }

        mesh.addVertices(vertArray, Util.toIntArray(intArray));
    }

    public Shader getShader() {
        return shader;
    }

}
