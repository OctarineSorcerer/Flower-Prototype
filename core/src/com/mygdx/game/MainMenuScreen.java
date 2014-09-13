package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class MainMenuScreen implements Screen {

    final FlowerPrototype game; //This is used in the rendering loop here, I believe

    OrthographicCamera camera;

    public MainMenuScreen(final FlowerPrototype gam) { //no create() here, so constructor is used
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); //Ah, orthographic views. All hail 2d.
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "This is Dan's test text! ", 100, 150);
        game.font.draw(game.batch, "Touch anywhere to light up the night", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }
    //For now, these guys don't actually have to do anything in this specific class
    public void dispose()
    {

    }
    public void show()
    {

    }
    public void hide()
    {

    }
    public void pause()
    {

    }
    public void resume()
    {

    }
    public void resize(int width, int height)
    {

    }
}