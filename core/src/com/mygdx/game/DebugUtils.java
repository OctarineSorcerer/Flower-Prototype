package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;
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
}
