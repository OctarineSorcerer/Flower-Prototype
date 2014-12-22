package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FlowerItems.Flower;
import com.sun.javafx.geom.Point2D;

import com.mygdx.game.FlowerItems.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Yay, game screen! Where it all goes DOWN
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    final FlowerPrototype game;

    OrthographicCamera camera;
    Ground ground;
    Petal testPetal;
    Flower testFlower;
    public static DebugUtils.CrossManager crossManager = new DebugUtils.CrossManager(true);

    public GameScreen(final FlowerPrototype gam) {
        this.game = gam; //This is for rendering, right?
        //Gdx.input.setInputProcessor(this);
        //Load images

        //Load sounds

        //Camera and spritebatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, FlowerPrototype.WIDTH, FlowerPrototype.HEIGHT); //width*height of the camera

        //any other creation stuff
        ground = new Ground(new Texture(Gdx.files.internal("textures/Ground.png")));

        Head testHead = new Head(0, Color.BLUE, new Point2D(200, 200));
        testPetal = new Petal(0, Color.YELLOW);
        testFlower = new Flower(testPetal, testHead, new Stem(), 13, Flower.PetalStyle.Touching,
                new Point2D(FlowerPrototype.WIDTH/2, 20)); //20 for funsies

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
        //Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClearColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clear dat screen

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        //Begin a batch and draw stuff
        game.batch.begin();
        ground.Draw(game.batch);
        testFlower.Draw(game.batch);
        //crossManager.DrawCrosses(game.batch);
        game.batch.end();

        //Process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //Translates from screen co-ord to world co-ord
            //Do with touchPos as you will
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (camera.position.x > FlowerPrototype.WIDTH / 2) {
                camera.translate(-200 * Gdx.graphics.getDeltaTime(), 0); //timespan between last and this frame in deltaseconds
                ground.DecrementStart(200 * Gdx.graphics.getDeltaTime());
            }
            else {
                camera.position.x = FlowerPrototype.WIDTH / 2;
            }
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(200 * Gdx.graphics.getDeltaTime(), 0);
            ground.IncrementStart(200 * Gdx.graphics.getDeltaTime());
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            camera.translate(0, 200 * Gdx.graphics.getDeltaTime());
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            if (camera.position.y > FlowerPrototype.HEIGHT / 2) {
                camera.translate(0, -200 * Gdx.graphics.getDeltaTime());
            }
            else camera.position.y = FlowerPrototype.HEIGHT / 2;
            camera.update();
        }
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
        camera.translate(-deltaX, -deltaY);
        camera.update();
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
