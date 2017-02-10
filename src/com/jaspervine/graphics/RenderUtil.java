package com.jaspervine.graphics;

import com.jaspervine.math.Vector3;
import org.lwjgl.opengl.GLContext;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class RenderUtil {

    public static void clearScreen() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void initGraphics() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRange(0.0f, 1.0f);

        glEnable(GL_DEPTH_CLAMP);

        glEnable(GL_TEXTURE_2D);
        //glEnable(GL_FRAMEBUFFER_SRGB);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public static String getOpenGLVersion() {
        return glGetString(GL_VERSION);
    }

    public static void setTextures(boolean enabled) {
        if (enabled){
            glEnable(GL_TEXTURE_2D);
        }
        else {
            glDisable(GL_TEXTURE_2D);
        }
    }

    public static void setClearColour(Vector3 colour) {
        glClearColor(colour.getX(), colour.getY(), colour.getZ(), 1.0f);
    }

    public static void unbindTextures() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
