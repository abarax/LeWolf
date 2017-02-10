package com.jaspervine.graphics;

import com.jaspervine.math.Vector2;
import com.jaspervine.math.Vector3;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 13/12/14
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Material {
    private Texture texture;
    private Vector3 colour;
    private float specularIntensity;
    private float specularExponent;

    public Material(Texture texture) {
        this(texture, new Vector3(1,1,1));
    }

    public Material(Texture texture, Vector3 colour) {
        this(texture, colour, 20, 64);
    }

    public Material(Texture texture, Vector3 colour, float specularIntensity, float specularExponent) {
        this.texture = texture;
        this.colour = colour;
        this.specularIntensity = specularIntensity;
        this.specularExponent = specularExponent;
    }


    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector3 getColour() {
        return colour;
    }

    public void setColour(Vector3 colour) {
        this.colour = colour;
    }

    public float getSpecularIntensity() {
        return specularIntensity;
    }

    public void setSpecularIntensity(float specularIntensity) {
        this.specularIntensity = specularIntensity;
    }

    public float getSpecularExponent() {
        return specularExponent;
    }

    public void setSpecularExponent(float specularExponent) {
        this.specularExponent = specularExponent;
    }
}
