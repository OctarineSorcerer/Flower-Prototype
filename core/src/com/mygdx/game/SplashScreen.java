package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SplashScreen implements Screen {
    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));
    private Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
    //private Image splashImage = new Image(texture);
    private Label splashLabel = new Label("Splash screen!", skin, "MotorStyle");
    private Stage stage = new Stage();


    final FlowerPrototype game;

    public SplashScreen(FlowerPrototype gam)
    {
        this.game = gam;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        splashLabel.setY(FlowerPrototype.HEIGHT/2);
        splashLabel.setX(FlowerPrototype.WIDTH/2 - splashLabel.getWidth()/2);
        stage.addActor(splashLabel);

        splashLabel.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(0.5f), Actions.delay(2), Actions.fadeOut(0.5f), Actions.run(new Runnable() {
            @Override
            public void run() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
            }
        })));
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        texture.dispose();
        stage.dispose();
    }

}
