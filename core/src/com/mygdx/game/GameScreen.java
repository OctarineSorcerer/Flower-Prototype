package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.FlowerItems.*;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;

import com.mygdx.game.Tools.ITool;
import com.mygdx.game.Tools.Shovel;
import com.mygdx.game.Tools.WateringCan;

import java.util.ArrayList;
import java.util.Random;

/**
 * Yay, game screen! Where all the things happen
 */
public class GameScreen implements Screen, GestureDetector.GestureListener {
    private final FlowerPrototype game;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Stage stage = new Stage();
    private Table table = new Table();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    private ArrayList<ITool> tools = new ArrayList<ITool>();
    private ITool currentTool;

    private Random rand = new Random();
    private ExtendedCamera camera;
    private int hCameraSpeed = 200; //Horizontal camera speed
    private Ground ground;
    public static Flower testFlower; //needs to be accessed elsewhere

    public GameScreen(final FlowerPrototype gam) {
        this.game = gam;
        InputMultiplexer im = new InputMultiplexer(); //Be able to take multiple types of input
        im.addProcessor(new GestureDetector(this)); //Regular input on the screen
        im.addProcessor(stage); //Input to the UI
        Gdx.input.setInputProcessor(im); //Set the input to be whatever the multiplexer gets

        AddUI(); //Add the UI to the screen
        tools.add(new Shovel());
        tools.add(new WateringCan());
        currentTool = tools.get(0); //Adding those tools

        //Camera and spritebatch
        camera = new ExtendedCamera(0, FlowerPrototype.HEIGHT/2, null, null);
        camera.setToOrtho(false, FlowerPrototype.WIDTH, FlowerPrototype.HEIGHT); //width*height of the camera
        //any other creation stuff
        ground = new Ground(new Texture(Gdx.files.internal("textures/Ground.png")));

        testFlower = game.info.ConstructFlower(); //Load the flower from the save info
        testFlower.stem.curveInfo.GetAllCurves(); //Load initial cirves

        rand.nextLong();
    }

    private void AddUI() {
        ArrayList<Image> toolImages = DebugUtils.GetImages("textures/tools");
        buttonGroup.setMinCheckCount(0);
        buttonGroup.setMaxCheckCount(1); //Only one tool may be checked at any given time
        for(int i = 0; i < toolImages.size(); i++) {
            Image toolImage = toolImages.get(i);
            final ImageButton imageButton = new ImageButton(skin);
            ImageButtonStyle style = new ImageButtonStyle(imageButton.getStyle());
            style.imageUp = toolImage.getDrawable();
            style.imageDown = toolImage.getDrawable();
            style.checked = skin.getDrawable("default-round-down"); //Set the various states of the button style
            imageButton.setStyle(style); //Actually give the button the style
            if(i==0) imageButton.setChecked(true); //Check the first button

            final int index = i;
            imageButton.addListener(new InputListener() {
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    currentTool = tools.get(index); //Set current tool when clicked
                    return false;
                }
            });

            buttonGroup.add(imageButton);
            table.add(imageButton);
        }
        table.left().bottom();
        table.setFillParent(true);
        stage.addActor(table); //Put all the tool images on the bottom left

        //Make an exit button on the bottom right
        Table otherSide = new Table().right().bottom();
        Image exitImage = new Image(new Texture(Gdx.files.internal("textures/Exit.png")));
        ImageButton exitButton = new ImageButton(skin);
        ImageButtonStyle style = new ImageButtonStyle(exitButton.getStyle());
        style.imageUp = exitImage.getDrawable();
        style.imageDown = style.imageUp;
        style.checked = skin.getDrawable("default-round-down");
        exitButton.setStyle(style); //Set the images of the exit button
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.info.LoadFromFlower(testFlower);
                game.info.WriteSave(); //Save the flower
                dispose(); //Dispose of resources
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game)); //Go back to main menu
                return false;
            }
        });
        otherSide.add(exitButton);
        otherSide.setFillParent(true);
        stage.addActor(otherSide); //Add the exit button to the stage
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
        testFlower.ApplyGrowth(); //Grow that flower. Possibly could be set on a timer, but that flower should grow as smoothly as possible

        //Begin a batch and draw everything
        testFlower.DrawShapes(shapeRenderer);

        game.batch.begin();
        testFlower.DrawSprites(game.batch); //Draw the flower sprites
        ground.Draw(game.batch); //Draw the ground
        testFlower.DrawHole(game.batch, delta); //Draw the hole after the ground and flower

        //Process user input - do things about it
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
    public void resize(int width, int height) { //This likely isn't going to happen much, but worth resizing a couple things
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        FlowerPrototype.WIDTH = width;
        FlowerPrototype.HEIGHT = height;
        ground.CalculateIterations();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        currentTool.apply(x, y);
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
    public boolean pan(float x, float y, float deltaX, float deltaY) { //Move the camera about
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