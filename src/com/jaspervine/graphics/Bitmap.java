package com.jaspervine.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: abarax
 * Date: 19/12/14
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Bitmap {

    private int width;
    private int height;
    private int [] pixels;


    public Bitmap(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File("./res/bitmaps/" + filename));

            width = image.getWidth();
            height = image.getHeight();

            pixels = new int[width * height];

            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap FlipX() {
        int [] temp = new int[pixels.length];

        for(int i = 0; i < width; i++)
            for(int j = 0; j < height; j++)
                temp[i + j * width] = pixels[(width - i - 1) + j * width];

        pixels = temp;

        return this;
    }

    public Bitmap FlipY() {
        int [] temp = new int[pixels.length];

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                temp[i + j * width] = pixels[i + (height - j - 1) * width];
            }
        }

        pixels = temp;

        return this;
    }

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getPixel(int x, int y) {
        return pixels[x + (y * width)];
    }

    public void setPixel(int x, int y, int value) {
        pixels[x + y] = value;
    }
}
