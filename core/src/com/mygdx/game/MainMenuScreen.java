package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;
import java.util.List;

public class MainMenuScreen implements Screen {
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

    private TextButton buttonPlay = new TextButton("Play", skin),
            buttonFlower = new TextButton("Flower", skin),
            buttonExit = new TextButton("Exit", skin);
    private Label titleLabel = new Label("Flower Prototype!", skin, "MotorStyle");

    final FlowerPrototype game; //Pass along!

    OrthographicCamera camera;

    public MainMenuScreen(final FlowerPrototype gam) { //no create() here, so constructor is used
        game = gam;

        buttonPlay.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });
        buttonFlower.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                NewFlowerCreation();
            }
        });
        buttonExit.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
            }
        });

        camera = new OrthographicCamera();
        camera.setToOrtho(false, FlowerPrototype.WIDTH, FlowerPrototype.HEIGHT); //Ah, orthographic views. All hail 2d.
    }

    private ArrayList<FileHandle> GetMonochromes(String path) {
        ArrayList<FileHandle> output = new ArrayList<FileHandle>();
        FileHandle dirHandle;
        dirHandle = Gdx.files.internal(path);
        for (FileHandle entry: dirHandle.list()) {
            if (entry.name().endsWith(".png")) {
                output.add(entry);
            }
        }
        return output;
    }
    private void AddPicsToTable(String path) {
        ArrayList<FileHandle> files = GetMonochromes(path);
        for(FileHandle entry : files) {
            Image image = new Image(new Texture(entry));
            image.setName(entry.path());
            table.add(image).pad(5).padBottom(15);
        }
        table.row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act();
        stage.draw();
    }
    @Override
    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }
    @Override
    public void show()
    {
        PlayExitOptions();
        Gdx.input.setInputProcessor(stage);
    }

    private void PlayExitOptions() {
        stage.clear();
        table.clear();

        //For table: The elements are displayed in the order added.
        //The first appear on top, the last at the bottom.
        table.add(titleLabel).padBottom(40).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonFlower).size(150,60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);
    }

    private void NewFlowerCreation() {
        stage.clear();
        table.clear();

        AddPicsToTable("textures/petals/monochrome");
        AddPicsToTable("textures/heads/monochrome");

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void hide()
    {
        dispose();
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