package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/menuSkin.json"),
            new TextureAtlas(Gdx.files.internal("skins/menuSkin.pack")));

    private TextButton buttonPlay = new TextButton("Play", skin),
            buttonExit = new TextButton("Exit", skin);
    private Label titleLabel = new Label("Flower Prototype!", skin);

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
        buttonPlay.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
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
        //For table: The elements are displayed in the order added.
        //The first appear on top, the last at the bottom.
        table.add(titleLabel).padBottom(40).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
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