package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.SaveItems.SaveInfo;

import java.util.Random;

//Overarching class for game
public class FlowerPrototype extends Game {
	SpriteBatch batch; //Instance used for drawing sprites
    SaveInfo info; //Each game will use a SaveInfo

    public static Random rand = new Random();

    public static int WIDTH, HEIGHT;

	@Override
	public void create () {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		batch = new SpriteBatch();
        this.setScreen(new SplashScreen(this));
	}

	@Override
	public void render () {
        super.render(); //Render the current screen. Very important
	}
}
