package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.SaveItems.SaveInfo;

import java.util.Random;

//Overarching class for game
public class FlowerPrototype extends Game {
	SpriteBatch batch; //Instance used for drawing sprites
    SaveInfo info; //Each game will use a SaveInfo

    public static Random rand = new Random();

    public static int WIDTH=480,HEIGHT=600;

	@Override
	public void create () {
		batch = new SpriteBatch();
        this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
        super.render(); //Render the current screen. Very important
	}
}
