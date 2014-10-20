package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.sun.javafx.geom.Point2D;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Yay, game screen! Where it all goes DOWN
 */
public class GameScreen implements Screen, InputProcessor{
    final FlowerPrototype game;

    OrthographicCamera camera;
    Texture testTex;
    Rectangle testRect;
    Flower.Petal testPetal;
    Flower testFlower;
    public static DebugUtils.CrossManager crossManager = new DebugUtils.CrossManager(true);

    class TouchInfo {
        public float touchX = 0;
        public float touchY = 0;
        public boolean touched = false;
    }
    private Map<Integer,TouchInfo> touches = new HashMap<Integer,TouchInfo>();

    public GameScreen(final FlowerPrototype gam) {
        this.game = gam; //This is for rendering, right?
        Gdx.input.setInputProcessor(this);
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

        for(int i = 0; i < 5; i++){
            touches.put(i, new TouchInfo());
        }
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

        for(Flower.PetalFlyweight petalType : testFlower.petals)
        {
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
        //if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            //bucket.x -= 200 * Gdx.graphics.getDeltaTime(); //timespan between last and this frame in deltaseconds
        //if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            //bucket.x += 200 * Gdx.graphics.getDeltaTime();
    }

    @Override
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

    //region Inputs (touch, keys, etc)
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.RIGHT:
                camera.translate(1, 0);
                break;
            case Keys.LEFT:
                camera.translate(-1, 0);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = screenX;
            touches.get(pointer).touchY = screenX;
            touches.get(pointer).touched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < 5){
            touches.get(pointer).touchX = 0;
            touches.get(pointer).touchY = 0;
            touches.get(pointer).touched = false;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
    //endregion

}
