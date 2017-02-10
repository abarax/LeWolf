package com.jaspervine.core;

import com.jaspervine.graphics.*;
import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: abarax
 * Date: 26/12/14
 * Time: 1:21 AM
 */
public class Monster {

    private static float SCALE = 0.7f;
    private static float SIZEY = SCALE;
    private static float SIZEX = (float) ((double) SIZEY / (1.75 * 2.0));

    public static final float OFFSET_X = 0.05f;
    public static final float OFFSET_Y = 0.01f;

    private static float TEX_MIN_X = - OFFSET_X;
    private static float TEX_MAX_X = 1 - OFFSET_X;

    private static float TEX_MIN_Y = - OFFSET_Y;
    private static float TEX_MAX_Y = 1 - OFFSET_Y;

    public static float MOVEMENT_SPEED = 1.0f;
    public static float MOVEMENT_STOP_DISTANCE = 1.1f;
    public static float WIDTH = 0.2f;
    public static float LENGTH = 0.2f;
    public static float GUN_RANGE = 1000f;
    public static final float SHOT_ANGLE = 10;

    private Random rand;



    public enum STATE {
        IDLE,
        CHASE,
        ATTACK,
        DYING,
        DEAD
    }

    private STATE state = STATE.CHASE;

    private static Mesh mesh;
    private List<Line> bullets;
    private Transform transform;
    private Material material;

    public Monster(Transform transform) {

        this.transform = transform;
        this.material = new Material(ResourceLoader.loadTexture("SSWVA1.png"));
        this.rand = new Random();
        this.bullets = new ArrayList<Line>();
        if(mesh == null) {
            mesh = new Mesh();

            Vertex[] vertices = new Vertex[] {
                    new Vertex(new Vector3(-SIZEX, 0, 0),       new Vector2(TEX_MAX_X, TEX_MAX_Y)),
                    new Vertex(new Vector3(-SIZEX, SIZEY, 0),   new Vector2(TEX_MAX_X, TEX_MIN_Y)),
                    new Vertex(new Vector3(SIZEX, SIZEY, 0),    new Vector2(TEX_MIN_X, TEX_MIN_Y)),
                    new Vertex(new Vector3(SIZEX, 0, 0),        new Vector2(TEX_MIN_X, TEX_MAX_Y)),
            };

            int [] indices = new int[] {
                    0,  1,  2,
                    0,  2,  3,
            };

            mesh.addVertices(vertices, indices);
        }
    }

    private void faceCamera(Vector3 directionToCamera) {

        float angleToFaceTheCamera = (float)Math.toDegrees(Math.atan(directionToCamera.getZ() / directionToCamera.getX()));

        if (directionToCamera.getX() > 0) {
           angleToFaceTheCamera += 180;
        }

        transform.getRotation().setY(angleToFaceTheCamera + 90);

    }

    public void update() {

        Vector3 directionToCamera = transform.getTranslation().subtract(Transform.getCamera().getPosition());

        float distanceToCamera = directionToCamera.length();
        Vector3 orientation = directionToCamera.divide(distanceToCamera);

        transform.getTranslation().setY(1);

        faceCamera(directionToCamera);

        switch (state) {
            case IDLE:
                idleUpdate(orientation, distanceToCamera);
                break;
            case CHASE:
                chaseUpdate(orientation, distanceToCamera);
                break;
            case ATTACK:
                attackUpdate(orientation, distanceToCamera);
                break;
            case DYING:
                dyingUpdate(orientation, distanceToCamera);
                break;
            case DEAD:
                deadUpdate(orientation, distanceToCamera);
                break;
        }

    }

    public void input () {

    }

    public void render() {
        Shader shader = Level.shader;

        shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);

        mesh.draw();

    }

    private void idleUpdate(Vector3 orientation, float distanceToCamera) {

    }

    private void chaseUpdate(Vector3 orientation, float distanceToCamera) {

       if(distanceToCamera > MOVEMENT_STOP_DISTANCE) {
           float movementAmount = -MOVEMENT_SPEED * (float) Time.getDelta();

           Vector3 oldPosition = this.transform.getTranslation();
           Vector3 newPosition = transform.getTranslation().add(orientation.multiply(movementAmount));

           Vector3 collision = Game.getLevel().checkCollisions(oldPosition, newPosition, WIDTH, LENGTH);

           Vector3 movementVector = collision.multiply(orientation);

           if (movementVector.length() > 0) {
                transform.setTranslation(transform.getTranslation().add(movementVector.multiply(movementAmount)));
           }

           if (movementVector.subtract(orientation).length() != 0) {
               Game.getLevel().openDoor(transform.getTranslation());
           }
       }

        state = STATE.ATTACK;

    }

    private void attackUpdate(Vector3 orientation, float distanceToCamera) {

        Vector2 lineStart = new Vector2(transform.getTranslation().getX(), transform.getTranslation().getZ());
        Vector2 castDirection = new Vector2(-orientation.getX(), -orientation.getZ()).rotate((rand.nextFloat() - 0.5f) * SHOT_ANGLE);
        Vector2 lineEnd = new Vector2(castDirection.multiply(GUN_RANGE));

//        new Line(transform, lineStart, lineEnd).render();

        Vector2 collisionVector = Game.getLevel().checkIntersections(lineStart, lineEnd);

        Vector2 playerIntersectVector = Game.getLevel().lineIntersectRect(
                lineStart,
                lineEnd,
                Transform.getCamera().getPosition().getXZ(),
                new Vector2(Player.PLAYER_SIZE, Player.PLAYER_SIZE));

        if (playerIntersectVector != null &&
                (collisionVector == null ||
                 playerIntersectVector.subtract(lineStart).length() < collisionVector.subtract(lineStart).length() )) {
            System.out.println("The player has been shot.");

        }

        if (collisionVector == null)
            System.out.println("WE've missed everything");
        else
            System.out.println("We've hit something");

        state = STATE.CHASE;

    }

    private void dyingUpdate(Vector3 orientation, float distanceToCamera) {

    }

    private void deadUpdate(Vector3 orientation, float distanceToCamera) {

    }
}
