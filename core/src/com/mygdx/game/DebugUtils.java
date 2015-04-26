package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.*;

/**
 * Some debug/miscellaneous utilities, likely to change through development
 */
class DebugUtils {
    private static ArrayList<FileHandle> GetMonochromes(String path) {
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
