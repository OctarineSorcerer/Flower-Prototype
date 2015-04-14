package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.FlowerItems.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.SaveItems.SaveInfo;
import com.mygdx.game.Tools.ITool;
import com.mygdx.game.Tools.Shovel;
import com.mygdx.game.Tools.WateringCan;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Yay, game screen! Where it all goes DOWN
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    final FlowerPrototype game;
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Stage stage = new Stage();
    private Table table = new Table();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    ArrayList<ITool> tools = new ArrayList<ITool>();
    private ITool currentTool;

    Random rand = new Random();
    ExtendedCamera camera;
    int hCameraSpeed = 200, vCameraSpeed = 200;
    Ground ground;
    public static Flower testFlower; //needs to be accessed elsewhere
    public static DebugUtils.CrossManager crossManager = new DebugUtils.CrossManager(true);

    public GameScreen(final FlowerPrototype gam) {
        this.game = gam; //This is for rendering, right?
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new GestureDetector(this));
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);

        AddUI();
        tools.add(new Shovel());
        tools.add(new WateringCan());
        currentTool = tools.get(0);

        //Camera and spritebatch
        camera = new ExtendedCamera(0, FlowerPrototype.HEIGHT/2, null, null);
        camera.setToOrtho(false, FlowerPrototype.WIDTH, FlowerPrototype.HEIGHT); //width*height of the camera
        //any other creation stuff
        ground = new Ground(new Texture(Gdx.files.internal("textures/Ground.png")));

        testFlower = game.info.ConstructFlower();
        testFlower.stem.curveInfo.GetCurvesOnScreen(0, FlowerPrototype.HEIGHT/2, testFlower.rootLoc);

        //crossManager.AddCross(headCenter, Float.toString(headCenter.x) + ", " + Float.toString(headCenter.y), Color.ORANGE);
        DecimalFormat dF = new DecimalFormat();
        dF.setMaximumFractionDigits(2);
        rand.nextLong();
    }

    public void AddUI() {
        ArrayList<Image> toolImages = DebugUtils.GetImages("textures/tools");
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setMaxCheckCount(1);
        for(int i = 0; i < toolImages.size(); i++) {
            Image toolImage = toolImages.get(i);
            final ImageButton imageButton = new ImageButton(skin);
            ImageButtonStyle style = new ImageButtonStyle(imageButton.getStyle());
            style.imageUp = toolImage.getDrawable();
            style.imageDown = toolImage.getDrawable();
            style.checked = skin.getDrawable("default-round-down");
            imageButton.setStyle(style);
            if(i==0) imageButton.setChecked(true);

            final int index = i;
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    currentTool = tools.get(index);
                    return false;
                }
            });

            buttonGroup.add(imageButton);
            table.add(imageButton);
        }
        table.left().bottom();
        table.setFillParent(true);
        stage.addActor(table);

        Table otherSide = new Table().right().bottom();
        Image exitImage = new Image(new Texture(Gdx.files.internal("textures/Exit.png")));
        ImageButton exitButton = new ImageButton(skin);
        ImageButtonStyle style = new ImageButtonStyle(exitButton.getStyle());
        style.imageUp = exitImage.getDrawable();
        style.imageDown = style.imageUp;
        style.checked = skin.getDrawable("default-round-down");
        exitButton.setStyle(style);
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.info.LoadFromFlower(testFlower);
                game.info.WriteSave();
                dispose();
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                return false;
            }
        });
        otherSide.add(exitButton);
        otherSide.setFillParent(true);
        stage.addActor(otherSide);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //Clear dat screen
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        testFlower.ApplyGrowth();

        //Begin a batch and draw stuff
        testFlower.DrawShapes(shapeRenderer);

        game.batch.begin();
        testFlower.DrawSprites(game.batch);
        ground.Draw(game.batch);
        testFlower.DrawHole(game.batch, delta);

        //Process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos); //Translates from screen co-ord to world co-ord
            currentTool.draw(game.batch, touchPos.x, touchPos.y, delta);
        //Do with touchPos as you will
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Vector2 translated = camera.SafeTranslate(-hCameraSpeed * Gdx.graphics.getDeltaTime(), 0); //timespan between last and this frame in deltaseconds
            ground.IncrementStart(translated.x);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(hCameraSpeed * Gdx.graphics.getDeltaTime(), 0);
            ground.IncrementStart(hCameraSpeed * Gdx.graphics.getDeltaTime());
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            camera.translate(0, hCameraSpeed * Gdx.graphics.getDeltaTime());
            camera.update();
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            camera.SafeTranslate(0, -hCameraSpeed * Gdx.graphics.getDeltaTime()); //timespan between last and this frame in deltaseconds
        }
        if (Gdx.input.isKeyPressed(Keys.G)) {
            testFlower.ApplyGrowth();
        }

        game.batch.end();

        stage.act();
        stage.draw();
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

        FlowerPrototype.WIDTH = width;
        FlowerPrototype.HEIGHT = height;
        ground.CalculateIterations();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        currentTool.apply(x, y);
        /*testFlower.DebugChangeStem();
        testFlower.stem.curveInfo.GetCurvesOnScreen((int)camera.position.y, (int)camera.position.x, testFlower.rootLoc);*/
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
        ground.IncrementStart(-deltaX);
        camera.SafeTranslate(-deltaX, deltaY);
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