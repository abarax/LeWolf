package com.jaspervine.graphics;

import com.jaspervine.core.Util;
import com.jaspervine.math.Matrix4;
import com.jaspervine.math.Vector3;

import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 11/12/14
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Shader {

    private int program;
    private HashMap<String, Integer> uniforms;

    public Shader() {
        program = glCreateProgram();
        uniforms = new HashMap<String, Integer>();

        if(program == 0){
            System.err.println("Shader creation failed");
            System.exit(1);
        }
    }

    public void bind() {
        glUseProgram(program);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {

    }

    public void addUniform(String uniform) {
        int uniformLocation = glGetUniformLocation(program, uniform);

        if(uniformLocation == 0xFFFFFFFF){
            System.err.println("Error: could not find uniform: " + uniform);
            new Exception().printStackTrace();
        }
        uniforms.put(uniform, uniformLocation);
    }

    public void addVertexShader(String text){
        addProgram(text, GL_VERTEX_SHADER);

    }

    public void addGeometryShader(String text) {
        addProgram(text, GL_GEOMETRY_SHADER);
    }

    public void addFragmentShader(String text) {
        addProgram(text, GL_FRAGMENT_SHADER);
    }

    public void compileShader(){
       glLinkProgram(program);

        if(glGetProgrami(program, GL_LINK_STATUS) == 0){
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }

        glValidateProgram(program);

        if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0){
            System.err.println("Program Validation Failed");
            System.err.println(glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
    }

    public void addProgram(String text, int type) {

        int shader = glCreateShader(type);

        if(shader == 0) {
            System.err.println("Shader Creation Failed");
            System.exit(1);
        }

        glShaderSource(shader, text);

        glCompileShader(shader);

        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0){
            System.err.println(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(program, shader);
    }

    public void setUniformi(String uniformName, int value){
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniformf(String uniformName, float value){
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3 value){
        glUniform3f(uniforms.get(uniformName), value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(String uniformName, Matrix4 value){
        glUniformMatrix4(uniforms.get(uniformName), true, Util.createFlippedBuffer(value));
    }
}
