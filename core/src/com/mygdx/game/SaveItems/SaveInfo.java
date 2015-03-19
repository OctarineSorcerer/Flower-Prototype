package com.mygdx.game.SaveItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.FlowerItems.Head;
import com.mygdx.game.FlowerItems.PetalGroup;
import com.mygdx.game.FlowerItems.Stem;
import com.mygdx.game.FlowerPrototype;
import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;

/**
 * Created by Dan on 27/02/2015.
 */
public class SaveInfo {
    public String thisName = "Default Save.json";

    HeadSave headDetails;
    StemSave stemDetails;
    GrowthInfo growthDetails;
    ArrayList<PetalGroupSave> petalDetails;
    public ArrayList<Integer> petalIndices = new ArrayList<Integer>();
    Flower.PetalStyle petalStyle = Flower.PetalStyle.Touching;

    public SaveInfo() { }
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
    public SaveInfo(HeadSave headSave, StemSave stemSave, GrowthInfo growthInfo, ArrayList<PetalGroupSave> petalGroupSaves,
                    ArrayList<Integer> indices) {
        headDetails = headSave;
        stemDetails = stemSave;
        growthDetails = growthInfo;
        petalDetails = petalGroupSaves;
        petalIndices = indices;
        WriteSave();
    }

    public void WriteSave() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String saveText = json.prettyPrint(this);
        FileHandle file = Gdx.files.local("bin/" + thisName);
        System.out.println(saveText);
        file.writeString(saveText, false);
    }
    public static SaveInfo LoadSave(String saveFileName) {
        Json json = new Json();
        FileHandle file = Gdx.files.local("bin/" + saveFileName);

        SaveInfo info = json.fromJson(SaveInfo.class, file.readString());
        return info;
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
        Flower flower = new Flower(petals, petalIndices, flowerHead, stem, petalStyle,
                stemDetails.root);
        return flower;
    }

}
