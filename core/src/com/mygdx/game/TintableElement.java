package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Uses a monochrome image to allow for images that are tinted a specific colour
 */
public abstract class TintableElement {
    public Sprite sprite;
    public Color color;
    int monoIndex;
    String monochromePath;
    /**
     * Constructor
     * @param textureFolder Folder containing the monochrome images (eg "textures/petals/monochrome/")
     * @param monochromeIndex Index of the monochrome image
     * @param tintColour Colour you wish to tint the image
     */
    TintableElement(String textureFolder, int monochromeIndex, Color tintColour)
    {
        this.monochromePath = textureFolder;
        Tint(tintColour);
        this.monoIndex = monochromeIndex;
    }

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

    /**
     * Gets the monochrome image for this specific TintableElement
     * @return A monochrome texture specified in fields
     */
    Texture GetMonochromeImage()
    {
        FileHandle[] files = Gdx.files.internal(monochromePath).list();
        FileHandle target = files[monoIndex];
        return new Texture(target);
    }
}
