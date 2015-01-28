package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Debug utilities! Yayyy!
 */
public class DebugUtils {
    public static class CrossManager
    {
        List<Point2D> locations = new ArrayList<Point2D>();
        List<Color> colors = new ArrayList<Color>();
        List<String> labels = new ArrayList<String>();
        BitmapFont font = new BitmapFont();
        public boolean ShowLabels;

        public CrossManager(boolean showLabels) {
            ShowLabels = showLabels;
            font.setColor(Color.WHITE);
        }

        public void AddCross(Point2D center, String label, Color color) {
            locations.add(center);
            labels.add(label);
            colors.add(color);
        }
        public void DrawCrosses(SpriteBatch batch) {
            DebugCross template = new DebugCross(0, Color.PINK);
            for(int i = 0; i < locations.size(); i++)
            {
                Point2D loc = locations.get(i);
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
            locations = new ArrayList<Point2D>();
            colors = new ArrayList<Color>();
        }
    }

    static class DebugCross extends TintableElement {
        DebugCross(int monochromeIndex, Color tintColour) {
            super("textures/Debug", monochromeIndex, tintColour);
            //sprite.setOriginCenter();
            //sprite.setCenter(loc.x, loc.y);
        }
    }
}
