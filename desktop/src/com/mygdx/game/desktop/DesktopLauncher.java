package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.FlowerPrototype;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Flower Test";
        config.width = FlowerPrototype.WIDTH;
        config.height = FlowerPrototype.HEIGHT;
		new LwjglApplication(new FlowerPrototype(), config);
	}
}
