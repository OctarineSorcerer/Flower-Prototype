package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.javafx.geom.Point2D;

import java.awt.*;

/**
 * Uses a monochrome image to allow for images that are tinted a specific colour
 */
public abstract class TintableElement {
    public Sprite sprite;
    public Color color;
    String monochromeName;
    public float scale = 1;
    public float rotation;
    String monochromePath;
    /**
     * Constructor
     * @param textureFolder Folder containing the monochrome images (eg "textures/petals/monochrome/")
     * @param monochromeName Index of the monochrome image
     * @param tintColour Colour you wish to tint the image
     */
    public TintableElement(String textureFolder, String monochromeName, Color tintColour)
    {
        this.monochromePath = textureFolder;
        this.monochromeName = monochromeName;
        Tint(tintColour);
    }

    public TintableElement(Sprite sprite)
    {
        this.sprite = sprite;
    }

    /***
     * Just please supply a sprite later
     */
    public TintableElement() {}

    public void Tint(Color tintColour)
    {
        if(sprite == null)
        {
            Texture itemTexture = GetMonochromeImage();
            sprite = new Sprite(itemTexture);
        }
        sprite.setColor(tintColour);
        color = tintColour;
    }

    public void Scale(float sizeMultiply)
    {
        sprite.scale(sizeMultiply);
        scale = sizeMultiply;
        //sprite.setCenter(sprite.getWidth()/2, 0);
    }

    public void Rotate(float degreesClockwise)
    {
        sprite.rotate(degreesClockwise);
    }

    /**
     * Sets the position of the sprite, given a translational origin
     * @param destination Destination you want the origin to rest at
     * @param translationOrigin Origin of the translation
     */
    public void SetPosWithOrigin(Point2D destination, Point2D translationOrigin) {
        sprite.setPosition(destination.x, destination.y);
        sprite.translate(-(translationOrigin.x), -(translationOrigin.y));
    }

    /**
     * Sets pos of sprite, using scale/rot origin as translational origin
     * @param destination Destination of sprite
     */
    public void SetPosWithRotationalOrigin(Point2D destination) {
        sprite.setPosition(destination.x, destination.y);
        float yShift = -(sprite.getOriginY());
        float xShift = -(sprite.getOriginX());
        sprite.translate(xShift, yShift);
    }
    /**
     * Gets the monochrome image for this specific TintableElement
     * @return A monochrome texture specified in fields
     */
    Texture GetMonochromeImage() {
        return new Texture(Gdx.files.internal(monochromePath + monochromeName));
    }
    public Point2D GetCenter(){
        return new Point2D(sprite.getX() + (sprite.getWidth()/2), sprite.getY() + (sprite.getHeight()/2));
    }

}
