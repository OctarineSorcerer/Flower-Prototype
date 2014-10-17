package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.util.*;

/**
 * Created by Dan on 17/10/2014.
 */
public class DebugUtils {
    public static class CrossManager
    {
        java.util.List<Point2D> locations = new ArrayList<Point2D>();
        java.util.List<Color> colors = new ArrayList<Color>();
        BitmapFont font = new BitmapFont();
        public boolean ShowCoOrds;

        public CrossManager(boolean showCoOrds)
        {
            ShowCoOrds = showCoOrds;
            font.setColor(Color.WHITE);
        }

        public void AddCross(Point2D center, Color color) {
            locations.add(center);
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
                if(ShowCoOrds) {
                    font.draw(batch, Float.toString(loc.x) + ", " + Float.toString(loc.y), loc.x + 10, loc.y);
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
            super("textures", monochromeIndex, tintColour);
            //sprite.setOriginCenter();
            //sprite.setCenter(loc.x, loc.y);
        }
    }
}
