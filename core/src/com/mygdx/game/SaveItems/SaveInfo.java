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
    public String Name = "Default Save.json"; //Makes sure it has a default name to save

    HeadSave headDetails;
    StemSave stemDetails;
    GrowthSave growthDetails;
    ArrayList<PetalGroupSave> petalDetails = new ArrayList<PetalGroupSave>();
    ArrayList<Integer> petalIndices = new ArrayList<Integer>();
    boolean beingDug, dug; //All the hole details that are needed

    public SaveInfo() {} //This constructor's required so I can just throw the serializer at it
    public SaveInfo(String name, HeadSave headSave, StemSave stemSave, GrowthSave growthSave, ArrayList<PetalGroupSave> petalGroupSaves,
                    ArrayList<Integer> indices) {
        headDetails = headSave;
        stemDetails = stemSave;
        growthDetails = growthSave;
        petalDetails = petalGroupSaves;
        petalIndices = indices;
        if(!name.endsWith(".json")) {
            name += ".json"; //Append .json if it's not there - that is the desired extension
        }
        Name = name;
        WriteSave();
    }

    /**
     * Writes save to the location specified by name
     */
    public void WriteSave() {
        Json json = new Json(); //JSON serializer
        json.setOutputType(JsonWriter.OutputType.json); //Makes sure it outputs in json format
        String saveText = json.prettyPrint(this); //JSON representation of this class. <3 Serializers
        FileHandle file = Gdx.files.local("saves/" + Name); //Where to write to
        file.writeString(saveText, false); //Writes text to location
    }

    /**
     * Loads details from a flower into this SaveInfo
     * @param flower Flower to load details from
     */
    public void LoadFromFlower(Flower flower) {
        headDetails = new HeadSave(flower.head.color, flower.head.GetMonoName());
        stemDetails = new StemSave(flower.stem.curveInfo.GetSeed(), flower.stem.colour, flower.stem.thickness,
                flower.stem.curveNumber, new Point2D(FlowerPrototype.WIDTH / 2, 20));
        growthDetails = new GrowthSave(flower.growth.Growth,flower.growth.GetPreviousGrowth(),
                flower.growth.bloomInfo.GetBloomStart(), flower.growth.bloomInfo.GetBloomLength(),
                flower.growth.GrowthRate);
        petalDetails = new ArrayList<PetalGroupSave>();
        for(PetalGroup petalGroup : flower.petals) { //Create save for each petalGroup
            PetalGroupSave save = new PetalGroupSave(petalGroup.color, petalGroup.GetMonoName(), petalGroup.GetBloomGrowthRate(), petalGroup.GetXGrowthAfter());
            petalDetails.add(save);
        }
        petalIndices = flower.petalIndices;
        beingDug = flower.hole.beingDug;
        dug = flower.hole.dug;
    }

    /**
     * Load save of specified name within "saves/"
     * @param saveFileName Name of savefile to load
     * @return A saveInfo representing the save file loaded
     */
    public static SaveInfo LoadSave(String saveFileName) {
        Json json = new Json(); //Once more, the serializer is used
        FileHandle file = Gdx.files.local("saves/" + saveFileName);
        String fileText = file.readString();

        return json.fromJson(SaveInfo.class, fileText); //This is what needs those no-arg constructors
    }

    /**
     * Creates a flower from this SaveInfo
     * @return A flower with details matching that of this SaveInfo
     */
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
        flower.ApplyLoadGrowth(); //Make sure the flower is grown to the appropriate level
        return flower;
    }

}
