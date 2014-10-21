package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Yay, game screen! Where it all goes DOWN
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    final FlowerPrototype game;

    OrthographicCamera camera;
    Texture testTex;
    Rectangle testRect;
    Flower.Petal testPetal;
    Flower testFlower;
    public static DebugUtils.CrossManager crossManager = new DebugUtils.CrossManager(true);

    public GameScreen(final FlowerPrototype gam) {
        this.game = gam; //This is for rendering, right?
        //Gdx.input.setInputProcessor(this);
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

        Flower.Head testHead = new Flower.Head(0, Color.RED, new Point2D(200, 200));
        testFlower = new Flower(testPetal, testHead, null, 13, Flower.PetalStyle.Touching);

        Point2D headCenter = testFlower.head.GetCenter();
        crossManager.AddCross(headCenter, Float.toString(headCenter.x) + ", " + Float.toString(headCenter.y), Color.ORANGE);

        DecimalFormat dF = new DecimalFormat(); dF.setMaximumFractionDigits(2);
        for(Flower.PetalFlyweight petalType : testFlower.petals)
        {
            for(int i = 0; i < petalType.locations.size(); i++)
            {
                crossManager.AddCross(petalType.locations.get(i), dF.format(petalType.rotations.get(i)), Color.MAGENTA);
            }
        } //Crosses for debug purposes

        /*for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }*/
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clear dat screen

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        //Begin a batch and draw stuff
        game.batch.begin();
        //game.font.draw(game.batch, "Game screen test text!", testRect.x, testRect.y + testTex.getHeight() + 40);
        //testPetal.sprite.draw(game.batch);
        //game.batch.draw(testTex, testRect.x, testRect.y);

        for (Flower.PetalFlyweight petalType : testFlower.petals) {
            petalType.DrawCentered(game.batch);
        }
        testFlower.head.sprite.draw(game.batch);
        crossManager.DrawCrosses(game.batch);

        game.batch.end();

        //Process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //Translates from screen co-ord to world co-ord
            //Do with touchPos as you will
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            camera.translate(-200 * Gdx.graphics.getDeltaTime(), 0); //timespan between last and this frame in deltaseconds
            camera.update();
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            camera.translate(200 * Gdx.graphics.getDeltaTime(), 0);
            camera.update();
        if (Gdx.input.isKeyPressed(Keys.W))
            camera.translate(0, 200 * Gdx.graphics.getDeltaTime());
            camera.update();
        if (Gdx.input.isKeyPressed(Keys.S))
            camera.translate(0, -200 * Gdx.graphics.getDeltaTime());
            camera.update();
    }
    public void dispose() { //dispose of all textures and such here

    }
    @Override
    public void show() { //Do stuff when screen is shown (ie play music)

    }
    @Override
    public void hide() {

    }
    @Override
    public void pause() {

    }
    @Override
    public void resume() {

    }
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    //region Input Events
    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    //endregion
}
