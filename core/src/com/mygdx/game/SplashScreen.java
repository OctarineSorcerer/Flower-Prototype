package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A screen to show upon loading! :)
 */
class SplashScreen implements Screen {
    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    private Label splashLabel = new Label("Splash screen!", skin);
    private Stage stage = new Stage();

    private final FlowerPrototype game;

    public SplashScreen(FlowerPrototype gam)
    {
        this.game = gam;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.6f, 1f, 1f); //blue background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw(); //Do stage things, then draw
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        splashLabel.setY(FlowerPrototype.HEIGHT/2);
        splashLabel.setX(FlowerPrototype.WIDTH/2 - splashLabel.getWidth()/2); //Center in middle of screen
        stage.addActor(splashLabel);

        splashLabel.addAction(Actions.sequence(Actions.alpha(0) //Fade text in, then out
                , Actions.fadeIn(0.5f), Actions.delay(2), Actions.fadeOut(0.5f), Actions.run(new Runnable() {
            @Override
            public void run() { //After fade, load MainMenuScreen
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
        stage.dispose();
    }

}
