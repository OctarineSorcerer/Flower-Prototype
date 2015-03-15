package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.FlowerItems.Head;
import com.mygdx.game.FlowerItems.PetalGroup;
import com.mygdx.game.FlowerItems.Stem;
import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;

/**
 * Created by Dan on 27/02/2015.
 */
public class SaveInfo {
    HeadSave headDetails;
    StemSave stemDetails;
    GrowthInfo growthDetails;
    ArrayList<PetalGroupSave> petalDetails;
    ArrayList<Integer> petalIndices = new ArrayList<Integer>();
    Flower.PetalStyle petalStyle = Flower.PetalStyle.Touching;

    public SaveInfo() {}
    public SaveInfo(Flower flower) {
        headDetails = new HeadSave(flower.head.color, flower.head.MonoPath());
        stemDetails = new StemSave(flower.stem.curveInfo.GetSeed(), flower.stem.colour, flower.stem.thickness,
                new Point2D(FlowerPrototype.WIDTH / 2, 20));
        growthDetails = new GrowthInfo(flower.growth.Growth, flower.growth.bloomInfo.bloomStart, flower.growth.bloomInfo.bloomLength);
        petalDetails = new ArrayList<PetalGroupSave>();
        for(PetalGroup petalGroup : flower.petals) {
            PetalGroupSave save = new PetalGroupSave(petalGroup.color, petalGroup.MonoPath(), petalGroup.GetBloomGrowthRate(), petalGroup.GetXGrowthAfter());
            petalDetails.add(save);
        }
        petalIndices = flower.petalIndices;
        petalStyle = flower.style;
    }
    public SaveInfo(HeadSave headSave, StemSave stemSave, GrowthInfo growthInfo, ArrayList<PetalGroupSave> petalGroupSaves) {
        headDetails = headSave;
        stemDetails = stemSave;
        growthDetails = growthInfo;
        petalDetails = petalGroupSaves;
    }

    public Flower ConstructFlower() {
        ArrayList<PetalGroup> petals = new ArrayList<PetalGroup>();
        Head flowerHead = new Head(headDetails.monochromePath, headDetails.tintColour);
        Stem stem = new Stem(stemDetails.seed);
        stem.colour = stemDetails.colour; stem.thickness = stemDetails.thickness;
        for(PetalGroupSave petalSave : petalDetails) {
            PetalGroup petal = new PetalGroup(petalSave.monochromePath, petalSave.tintColour);
            petal.SetBlooms(petalSave.bloomGrowthRate, petalSave.xGrowthAfter);
            petals.add(petal);
        }
        petalIndices = Flower.GetIndexMix(petals.size() - 1, petalIndices.size());
        Flower flower = new Flower(petals, petalIndices, flowerHead, stem, petalStyle,
                stemDetails.root);
        return flower;
    }

    public class HeadSave {
        String monochromePath;
        Color tintColour;
        public HeadSave(Color tintColour, String monochromePath) {
            this.tintColour = tintColour;
            this.monochromePath = monochromePath;
        }
    }
    public class PetalGroupSave {
        public float bloomGrowthRate;
        public float xGrowthAfter;
        Color tintColour;
        String monochromePath;
        public PetalGroupSave(Color tintColour, String monochromePath, float bloomGrowth, float xGrowth) {
            this.tintColour = tintColour;
            this.monochromePath = monochromePath;
            this.bloomGrowthRate = bloomGrowth;
            this.xGrowthAfter = xGrowth;
        }
    }
    public class StemSave {
        long seed;
        Point2D root;
        Color colour;
        int thickness;
        public StemSave(long seed, Color colour, int thickness, Point2D root) {
            this.seed = seed;
            this.colour = colour;
            this.thickness = thickness;
            this.root = root;
        }
    }
    public class GrowthInfo {
        float latestGrowth;
        float bloomStart, bloomLength;
        public GrowthInfo(float currentGrowth, float bloomStart, float bloomLength) {
            this.latestGrowth = currentGrowth;
            this.bloomStart = bloomStart;
            this.bloomLength = bloomLength;
        }
    }
}
