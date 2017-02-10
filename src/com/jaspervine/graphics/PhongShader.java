package com.jaspervine.graphics;

import com.jaspervine.core.ResourceLoader;
import com.jaspervine.math.Matrix4;
import com.jaspervine.math.Vector3;

import static org.lwjgl.opengl.GL20.glUniform1f;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 13/12/14
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhongShader extends Shader {

    private static final int MAX_POINT_LIGHTS = 4;
    private static final int MAX_SPOT_LIGHTS = 4;

    private static final PhongShader instance = new PhongShader();

    private static Vector3 ambientLight = new Vector3(0.5f, 0.5f, 0.5f);
    private static DirectionalLight directionalLight = new DirectionalLight(
            new BaseLight(new Vector3(1.0f, 1.0f, 1.0f), 1.0f),
            new Vector3(0, 0, 0)
    );

    private static PointLight[] pointLights = new PointLight[MAX_POINT_LIGHTS];
    private static SpotLight[] spotLights = new SpotLight[MAX_SPOT_LIGHTS];

    public static PhongShader getInstance() {
        return instance;
    }

    private PhongShader() {
        super();

        addVertexShader(ResourceLoader.loadShader("phongVertex.vert"));
        addFragmentShader(ResourceLoader.loadShader("phongFragment.frag"));

        compileShader();

        addUniform("transform");
        addUniform("transformProjected");
        addUniform("baseColour");
        addUniform("ambientLight");
        addUniform("directionalLight.base.color");
        addUniform("directionalLight.base.intensity");
        addUniform("directionalLight.direction");
        addUniform("specularIntensity");
        addUniform("specularExponent");
        addUniform("eyePosition");

        for(int i = 0; i < MAX_POINT_LIGHTS; i++) {
            addUniform("pointLights[" + i + "].base.color");
            addUniform("pointLights[" + i + "].base.intensity");
            addUniform("pointLights[" + i + "].attenuation.constant");
            addUniform("pointLights[" + i + "].attenuation.linear");
            addUniform("pointLights[" + i + "].attenuation.exponent");
            addUniform("pointLights[" + i + "].position");
            addUniform("pointLights[" + i + "].range");
        }

        for(int i = 0; i < MAX_SPOT_LIGHTS; i++) {
            addUniform("spotLights[" + i + "].pointLight.base.color");
            addUniform("spotLights[" + i + "].pointLight.base.intensity");
            addUniform("spotLights[" + i + "].pointLight.attenuation.constant");
            addUniform("spotLights[" + i + "].pointLight.attenuation.linear");
            addUniform("spotLights[" + i + "].pointLight.attenuation.exponent");
            addUniform("spotLights[" + i + "].pointLight.position");
            addUniform("spotLights[" + i + "].pointLight.range");
            addUniform("spotLights[" + i + "].direction");
            addUniform("spotLights[" + i + "].cutoff");
        }
    }

    public void updateUniforms(Matrix4 worldMatrix, Matrix4 projectedMatrix, Material material) {

        if( material.getTexture() != null) {
            material.getTexture().bind();
        } else {
            RenderUtil.unbindTextures();
        }

        setUniform("transformProjected", projectedMatrix);
        setUniform("transform", worldMatrix);
        setUniform("baseColour", material.getColour());
        setUniform("ambientLight", ambientLight);
        setUniform("directionalLight", directionalLight);

        setUniformf("specularIntensity", material.getSpecularIntensity());
        setUniformf("specularExponent", material.getSpecularExponent());
        setUniform("eyePosition", Transform.getCamera().getPosition());

        for(int i = 0; i < pointLights.length; i++) {
            setUniform("pointLights[" + i + "]", pointLights[i]);
        }

        for(int i = 0; i < spotLights.length; i++) {
            setUniform("spotLights[" + i + "]", spotLights[i]);
        }

    }

    public static Vector3 getAmbientLight() {
        return ambientLight;
    }

    public static void setAmbientLight(Vector3 ambientLight) {
        PhongShader.ambientLight = ambientLight;
    }

    public static void setDirectionalLight(DirectionalLight light) {
        PhongShader.directionalLight = light;
    }

    public static void setPointLights(PointLight [] pointLights) {
        if (pointLights.length > MAX_POINT_LIGHTS) {
            System.err.println("Too mant point lights, max allowed is " + MAX_POINT_LIGHTS + ", you passed in " + pointLights.length);
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.pointLights = pointLights;
    }

    public static void setSpotLights(SpotLight [] spotLights) {
        if (spotLights.length > MAX_SPOT_LIGHTS) {
            System.err.println("Too many spot lights, max allowed is " + MAX_SPOT_LIGHTS + ", you passed in " + spotLights.length);
            new Exception().printStackTrace();
            System.exit(1);
        }

        PhongShader.spotLights = spotLights;
    }

    public void setUniform(String uniformName, BaseLight baseLight) {
        setUniform(uniformName + ".color", baseLight.getColor());
        setUniformf(uniformName + ".intensity", baseLight.getIntensity());
    }
    public void setUniform(String uniformName, DirectionalLight value){
        setUniform(uniformName + ".base", value.getBaseLight());
        setUniform(uniformName + ".direction", value.getDirection());
    }

    public void setUniform(String uniformName, PointLight value){
        setUniform(uniformName + ".base", value.getBaseLight());

        setUniformf(uniformName + ".attenuation.constant", value.getAttenuation().getConstant());
        setUniformf(uniformName + ".attenuation.linear", value.getAttenuation().getLinear());
        setUniformf(uniformName + ".attenuation.exponent", value.getAttenuation().getExponent());


        setUniform(uniformName + ".position", value.getPosition());
        setUniformf(uniformName + ".range", value.getRange());
    }

    public void setUniform(String uniformName, SpotLight value){
        setUniform(uniformName + ".pointLight", value.getPointLight());
        setUniform(uniformName + ".direction", value.getDirection());
        setUniformf(uniformName + ".cutoff", value.getCutoff());
    }
}
