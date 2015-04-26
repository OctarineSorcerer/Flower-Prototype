package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Uses a monochrome image to allow for images that are tinted a specific colour
 */
public abstract class TintableElement {
    public Sprite sprite;
    public Color color;
    private String monochromeName;
    private String monochromePath;

    /**
     * Constructor
     * @param textureFolder Folder containing the monochrome images (eg "textures/petals/monochrome/")
     * @param monochromeName Index of the monochrome image
     * @param tintColour Colour you wish to tint the image
     */
    protected TintableElement(String textureFolder, String monochromeName, Color tintColour)
    {
        this.monochromePath = textureFolder;
        this.monochromeName = monochromeName;
        Tint(tintColour);
    }

    public String GetMonoName() {
        return monochromeName;
    }

    private void Tint(Color tintColour)
    {
        if(sprite == null)
        {
            Texture itemTexture = GetMonochromeImage();
            sprite = new Sprite(itemTexture);
        }
        sprite.setColor(tintColour);
        color = tintColour;
    }

    /**
     * Sets the position of the sprite, given a translational origin
     * @param destination Destination you want the origin to rest at
     * @param translationOrigin Origin of the translation
     */
    protected void SetPosWithOrigin(Vector2 destination, Vector2 translationOrigin) {
        sprite.setPosition(destination.x, destination.y);
        sprite.translate(-(translationOrigin.x), -(translationOrigin.y));
    }

    /**
     * Sets pos of sprite, using scale/rot origin as translational origin
     * @param destination Destination of sprite
     */
    protected void SetPosWithRotationalOrigin(Vector2 destination) {
        sprite.setPosition(destination.x, destination.y);
        float yShift = -(sprite.getOriginY());
        float xShift = -(sprite.getOriginX());
        sprite.translate(xShift, yShift);
    }
    /**
     * Gets the monochrome image for this specific TintableElement
     * @return A monochrome texture specified in fields
     */
    private Texture GetMonochromeImage() {
        return new Texture(Gdx.files.internal(monochromePath + monochromeName));
    }
    public Vector2 GetCenter(){
        return new Vector2(sprite.getX() + (sprite.getWidth()/2), sprite.getY() + (sprite.getHeight()/2));
    }

}
