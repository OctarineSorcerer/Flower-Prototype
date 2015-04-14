package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.*;
import java.util.List;

/**
 * Debug utilities! Yayyy!
 */
public class DebugUtils {
    public static class CrossManager
    {
        List<Vector2> locations = new ArrayList<Vector2>();
        List<Color> colors = new ArrayList<Color>();
        List<String> labels = new ArrayList<String>();
        BitmapFont font = new BitmapFont();
        public boolean ShowLabels;

        public CrossManager(boolean showLabels) {
            ShowLabels = showLabels;
            font.setColor(Color.WHITE);
        }

        public void AddCross(Vector2 center, String label, Color color) {
            locations.add(center);
            labels.add(label);
            colors.add(color);
        }
        public void DrawCrosses(SpriteBatch batch) {
            DebugCross template = new DebugCross("DebugCross.png", Color.PINK);
            for(int i = 0; i < locations.size(); i++)
            {
                Vector2 loc = locations.get(i);
                Color color = colors.get(i);
                template.sprite.setCenter(loc.x, loc.y);
                if(color != template.color) {
                    template.Tint(colors.get(i));
                }
                template.sprite.draw(batch);
                if(ShowLabels) {
                    font.draw(batch, labels.get(i), loc.x + 10, loc.y);
                }
            }
        }
        public void ClearCrosses() {
            locations = new ArrayList<Vector2>();
            colors = new ArrayList<Color>();
        }
    }

    static class DebugCross extends TintableElement {
        DebugCross(String monochromeName, Color tintColour) {
            super("textures/Debug/", monochromeName, tintColour);
            //sprite.setOriginCenter();
            //sprite.setCenter(loc.x, loc.y);
        }
    }

    public static ArrayList<FileHandle> GetMonochromes(String path) {
        ArrayList<FileHandle> output = new ArrayList<FileHandle>();
        FileHandle dirHandle;
        dirHandle = Gdx.files.internal(path);
        for (FileHandle entry: dirHandle.list()) {
            if (entry.name().endsWith(".png")) {
                output.add(entry);
            }
        }
        return output;
    }
    public static ArrayList<com.badlogic.gdx.scenes.scene2d.ui.Image> GetImages(String path) {
        ArrayList<FileHandle> files = GetMonochromes(path);
        ArrayList<Image> images = new ArrayList<Image>();
        for(FileHandle entry : files) {
            final Image image = new Image(new Texture(entry));
            image.setName(entry.name());
            images.add(image);
        }
        return images;
    }
}
