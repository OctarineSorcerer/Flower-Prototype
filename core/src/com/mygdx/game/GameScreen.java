package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Yay, game screen! Where it all goes DOWN
 */
public class GameScreen implements Screen{
    final FlowerPrototype game;

    OrthographicCamera camera;
    Texture testTex;
    Rectangle testRect;
    //Textures, sounds, rectangles etc go here, and game logic stuff

    public GameScreen(final FlowerPrototype gam)
    {
        this.game = gam; //This is for rendering, right?

        //Load images
        testTex = new Texture(Gdx.files.internal("dorf.jpg"));

        //Load sounds


        //Camera and spritebatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FlowerPrototype.WIDTH, FlowerPrototype.HEIGHT); //width*height of the camera

        //any other creation stuff
        testRect = new Rectangle();
        testRect.x = FlowerPrototype.WIDTH/2 - (testTex.getWidth())/2;
        testRect.y = 20;
    }

    @Override
    public void render (float delta)
    {
        Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clear dat screen

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        //Begin a batch and draw stuff
        game.batch.begin();
        game.font.draw(game.batch, "Game screen test text!", testRect.x, testRect.y + testTex.getHeight() + 40);
        game.batch.draw(testTex, testRect.x, testRect.y);
        game.batch.end();

        //Process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //Translates from screen co-ord to world co-ord
            //Do with touchPos as you will
        }
        //if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            //bucket.x -= 200 * Gdx.graphics.getDeltaTime(); //timespan between last and this frame in deltaseconds
        //if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            //bucket.x += 200 * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void dispose() //dispose of all textures and such here
    {

    }
    @Override
    public void show() //Do stuff when screen is shown (ie play music)
    {

    }
    @Override
    public void hide()
    {

    }
    @Override
    public void pause()
    {

    }
    @Override
    public void resume()
    {

    }
    @Override
    public void resize(int width, int height)
    {

    }
}
