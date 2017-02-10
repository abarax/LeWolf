package com.jaspervine.graphics;

import com.jaspervine.core.ResourceLoader;
import com.jaspervine.math.Matrix4;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 13/12/14
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicShader extends Shader {

    private static final BasicShader instance = new BasicShader();

    public static BasicShader getInstance() {
        return instance;
    }

    public BasicShader() {
        super();

        addVertexShader(ResourceLoader.loadShader("basicVertex.vert"));
        addFragmentShader(ResourceLoader.loadShader("basicFragment.frag"));

        compileShader();

        addUniform("transform");
        addUniform("colour");
    }

    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {

        if( material.getTexture() != null) {
            material.getTexture().bind();
        } else {
            RenderUtil.unbindTextures();
        }

        setUniform("transform", projectedMatrix);
        setUniform("colour", material.getColour());
    }

}
