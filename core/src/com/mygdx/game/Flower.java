package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.*;
/**
 * Not an interface - the flower should be procedural
 */
public class Flower {
    public class Petal {
        public Sprite sprite;
        public Color color;
        public int index = -1;
        Petal(int monochromeIndex, Color tintColour) {
            Texture petalTexture = GetMonochromePetal(monochromeIndex);
            Sprite outputSprite = new Sprite(petalTexture);
            outputSprite.setColor(tintColour);
            this.sprite = outputSprite;
            this.color = tintColour;
            this.index = monochromeIndex;
        }
    }
    class Stem {
        int thickness;

    }

    static Texture GetMonochromePetal(int monochromeIndex)
    {
        FileHandle[] files = Gdx.files.internal("textures/petals/monochrome/").list();
        FileHandle target = files[monochromeIndex];
        return new Texture(target);
    }
}
