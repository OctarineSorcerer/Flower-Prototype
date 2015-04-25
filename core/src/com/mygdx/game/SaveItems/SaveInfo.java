package com.mygdx.game.SaveItems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.FlowerItems.*;
import com.mygdx.game.FlowerPrototype;
import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;

/**
 * Class for managing saving of flowers
 */
public class SaveInfo {
    public String Name = "Default Save.json";

    HeadSave headDetails;
    StemSave stemDetails;
    GrowthInfo growthDetails;
    ArrayList<PetalGroupSave> petalDetails = new ArrayList<PetalGroupSave>();
    ArrayList<Integer> petalIndices = new ArrayList<Integer>();
    boolean beingDug, dug;

    public SaveInfo() {} //This constructor's required so I can just throw the serializer at it
    public SaveInfo(String name, HeadSave headSave, StemSave stemSave, GrowthInfo growthInfo, ArrayList<PetalGroupSave> petalGroupSaves,
                    ArrayList<Integer> indices) {
        headDetails = headSave;
        stemDetails = stemSave;
        growthDetails = growthInfo;
        petalDetails = petalGroupSaves;
        petalIndices = indices;
        if(!name.endsWith(".json")) {
            name += ".json";
        }
        Name = name;
        WriteSave();
    }

    public void WriteSave() {
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        String saveText = json.prettyPrint(this);
        FileHandle file = Gdx.files.local("bin/" + Name);
        file.writeString(saveText, false);
    }
    public void LoadFromFlower(Flower flower) {
        headDetails = new HeadSave(flower.head.color, flower.head.GetMonoName());
        stemDetails = new StemSave(flower.stem.curveInfo.GetSeed(), flower.stem.colour, flower.stem.thickness,
                flower.stem.curves, new Point2D(FlowerPrototype.WIDTH / 2, 20));
        growthDetails = new GrowthInfo(flower.growth.Growth,flower.growth.GetPreviousGrowth(),
                flower.growth.bloomInfo.GetBloomStart(), flower.growth.bloomInfo.GetBloomLength(),
                flower.growth.GrowthRate);
        petalDetails = new ArrayList<PetalGroupSave>();
        for(PetalGroup petalGroup : flower.petals) {
            PetalGroupSave save = new PetalGroupSave(petalGroup.color, petalGroup.GetMonoName(), petalGroup.GetBloomGrowthRate(), petalGroup.GetXGrowthAfter());
            petalDetails.add(save);
        }
        petalIndices = flower.petalIndices;
        beingDug = flower.hole.beingDug;
        dug = flower.hole.dug;
    }
    public static SaveInfo LoadSave(String saveFileName) {
        Json json = new Json();
        FileHandle file = Gdx.files.local("bin/" + saveFileName);
        String fileText = file.readString();

        return json.fromJson(SaveInfo.class, fileText);
    }

    public Flower ConstructFlower() {
        ArrayList<PetalGroup> petals = new ArrayList<PetalGroup>();
        Head flowerHead = new Head(headDetails.monochromePath, headDetails.tintColour);
        Stem stem = new Stem(stemDetails.seed, stemDetails.curves);
        stem.colour = stemDetails.colour; stem.thickness = stemDetails.thickness;
        for(PetalGroupSave petalSave : petalDetails) {
            PetalGroup petal = new PetalGroup(petalSave.monochromePath, petalSave.tintColour);
            petal.SetBlooms(petalSave.bloomGrowthRate, petalSave.xGrowthAfter);
            petals.add(petal);
        }
        GrowthHandling growth = new GrowthHandling(growthDetails);
        Flower flower = new Flower(petals, petalIndices, flowerHead, stem, stemDetails.root, growth);
        flower.hole.dug = dug;
        flower.hole.beingDug = beingDug;
        flower.ApplyLoadGrowth();
        return flower;
    }

}
