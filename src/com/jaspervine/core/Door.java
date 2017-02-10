package com.jaspervine.core;

import com.jaspervine.graphics.*;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 20/12/14
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class Door {

    private static Mesh mesh;
    private Material material;

    private Transform transform;
    private Shader shader;

    public static float THICKNESS = 0.125f;
    public static float WIDTH =     1.0f;

    private static double TIME_TO_OPEN = 1.0;
    private static double CLOSE_DELAY = 2.0;
    public static float DISTANCE_TO_OPEN = 0.8f;


    private boolean isOpening = false;

    private double openStartTime;
    private double closeStartTime;
    private double closedTime;
    private double openTime;

    private Vector3 openPosition;
    private Vector3 closePosition;

    public Door(Transform transform, Material material, Vector3 openPosition) {

        this.transform = transform;
        this.material = material;

        this.closePosition = transform.getTranslation().multiply(1);
        this.openPosition =   openPosition;

        if(mesh == null) {
            mesh = new Mesh();

            Vertex [] vertices = new Vertex[] {
                    new Vertex(new Vector3(0, 0, 0), new Vector2(0.5f, 1.0f)),
                    new Vertex(new Vector3(0, 1, 0), new Vector2(0.5f, 0.75f)),
                    new Vertex(new Vector3(1, 1, 0), new Vector2(0.75f, 0.75f)),
                    new Vertex(new Vector3(1, 0, 0), new Vector2(0.75f, 1.0f )),

                    new Vertex(new Vector3(0, 0, 0.1f), new Vector2(0.5f, 1.0f)),
                    new Vertex(new Vector3(0, 1, 0.1f), new Vector2(0.5f, 0.75f)),
                    new Vertex(new Vector3(1, 1, 0.1f), new Vector2(0.75f, 0.75f)),
                    new Vertex(new Vector3(1, 0, 0.1f), new Vector2(0.75f, 1.0f)),

                    new Vertex(new Vector3(0, 0, 0),    new Vector2(0.73f, 1.0f)),
                    new Vertex(new Vector3(0, 1, 0),    new Vector2(0.73f, 0.75f)),
                    new Vertex(new Vector3(0, 1, 0.1f), new Vector2(0.75f, 0.75f)),
                    new Vertex(new Vector3(0, 0, 0.1f), new Vector2(0.75f, 1.0f)),

                    new Vertex(new Vector3(1, 0, 0f),   new Vector2(0.73f, 1.0f)),
                    new Vertex(new Vector3(1, 1, 0f),   new Vector2(0.73f, 0.75f)),
                    new Vertex(new Vector3(1, 1, 0.1f), new Vector2(0.75f, 0.75f)),
                    new Vertex(new Vector3(1, 0, 0.1f), new Vector2(0.75f, 1.0f)),

            };

            int [] indices = new int[] {
                0,  1,  2,
                0,  2,  3,
                6,  5,  4,
                7,  6,  4,
                10, 9,  8,
                11, 10, 8,
                12, 13, 14,
                12, 14, 15
            };

            mesh.addVertices(vertices, indices);
        }
    }

    private Vector3 vectorLERP(Vector3 startPosition, Vector3 endPosition, float LERPFactor) {
        return startPosition.add(endPosition.subtract(startPosition)).multiply(LERPFactor);
    }

    public void update() {
        if (isOpening) {
          //  this.transform ;

            double time = Time.getTime()/Time.SECOND;

            if (time < openTime) {
                float LERPFactor =  (float)((time - openStartTime) / TIME_TO_OPEN);
                this.getTransform().setTranslation(vectorLERP(closePosition, openPosition, LERPFactor));
            } else if (time < closeStartTime){
                this.getTransform().setTranslation(openPosition);
            } else if ( time < closedTime) {
                float LERPFactor =  (float)((time - closeStartTime) / TIME_TO_OPEN);
                this.getTransform().setTranslation(vectorLERP(openPosition, closePosition, LERPFactor));
            } else {
                this.getTransform().setTranslation(closePosition);
                this.isOpening = false;
            }
        }

    }

    public void render() {
        shader = Level.shader;
        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
        mesh.draw();
    }

    public void open() {
        if (isOpening) {
            return;
        }

        openStartTime = Time.getTime()/Time.SECOND;
        openTime = openStartTime + TIME_TO_OPEN;
        closeStartTime = openTime + CLOSE_DELAY;
        closedTime = closeStartTime + TIME_TO_OPEN;

        isOpening = true;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public static Mesh getMesh() {
        return mesh;
    }

    public static void setMesh(Mesh mesh) {
        Door.mesh = mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public Vector2 getSize() {

        if (this.getTransform().getRotation().getY() == 90f){
            return new Vector2(THICKNESS, WIDTH);
        } else {
            return new Vector2(WIDTH, THICKNESS);
        }
    }
}
