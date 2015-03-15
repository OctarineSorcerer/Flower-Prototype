package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sun.javafx.geom.Point2D;

import javax.swing.event.ChangeListener;
import java.util.*;

public class MainMenuScreen implements Screen {
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

    private TextButton buttonFlower = new TextButton("Flower", skin),
            buttonExit = new TextButton("Exit", skin);
    private Slider slider = new Slider(3, 20, 1, false, skin);
    private Label titleLabel = new Label("Flower Prototype!", skin);

    ArrayList<String> petalPaths = new ArrayList<String>();
    ArrayList<Color> petalColours = new ArrayList<Color>();
    Image headImage; //this is so only 1 can be selected
    Color headColour, stemColour;
    String stemThickness;

    final FlowerPrototype game; //Pass along!

    OrthographicCamera camera;

    public MainMenuScreen(final FlowerPrototype gam) { //no create() here, so constructor is used
        game = gam;

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
    private ArrayList<Image> GetImages(String path) {
        ArrayList<FileHandle> files = GetMonochromes(path);
        ArrayList<Image> images = new ArrayList<Image>();
        for(FileHandle entry : files) {
            final Image image = new Image(new Texture(entry));
            image.setName(entry.name());
            images.add(image);
        }
        return images;
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.1059f, 0.1059f, 0.1059f, 1); //kinda a dark grey color
        Gdx.gl.glClearColor(0, 0.6f, 1f, 1f); //blue
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
        petalColours.add(Color.RED);
        PlayExitOptions();
        Gdx.input.setInputProcessor(stage);
    }

    private void PlayExitOptions() {
        stage.clear();
        table.clear();

        //For table: The elements are displayed in the order added.
        //The first appear on top, the last at the bottom.
        table.add(titleLabel).padBottom(40).row();
        table.add(buttonFlower).size(150,60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);
    }

    private void NewFlowerCreation() {
        stage.clear();
        table.clear();
        Table petalScroll = new Table();
        Table headScroll = new Table();

        //In C#, I would likely send a lambda as an anonymous function. However, I cannot do this
        //before Java 8, and while I have it, I don't think Android is so happy with 8 yet.
        ArrayList<Image> images = GetImages("textures/petals/monochrome");
        for(final Image image : images) {
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(petalPaths.contains(image.getName())) petalPaths.remove(image.getName());
                    else petalPaths.add(image.getName());

                    if(image.getColor().toIntBits() == 0xffffffff) {
                        image.setColor(Color.RED);
                    }
                    else image.setColor(Color.WHITE);
                }
            });
            petalScroll.add(image).pad(5);
        } petalScroll.row();

        images = GetImages("textures/heads/monochrome");
        for(final Image image : images) {
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    String thisName = image.getName();
                    if(headImage == null) {
                        image.setColor(Color.RED);
                        headImage = image;
                    }
                    else {
                        if (thisName != headImage.getName()) {
                            headImage.setColor(Color.WHITE);
                            image.setColor(Color.RED);
                            headImage = image;
                        } else image.setColor(Color.WHITE);
                    }
                }
            });
            headScroll.add(image).pad(5);
        } headScroll.row();

        table.add(new Label("Select petals!", skin)).fillX().center().row();
        table.add(new ScrollPane(petalScroll)).row();
        table.add(new Label("Select your flower head!", skin)).fillX().center().row();
        table.add(new ScrollPane(headScroll)).row();
        table.add(new Label("And the number of petals!", skin)).fillX().center().row();
        final Label progressLabel = new Label(Float.toString(slider.getValue()), skin);
        slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                progressLabel.setText(Float.toString(slider.getValue()));
            }
        });
        table.add(slider).colspan(2).center().fillX().pad(5);
        table.add(progressLabel).row();
        TextButton buttonGo = new TextButton("Go", skin);
        buttonGo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Random rand = new Random();
                SaveInfo info = new SaveInfo();
                SaveInfo.HeadSave head = info.new HeadSave(Color.BLUE, headImage.getName());
                SaveInfo.StemSave stem = info.new StemSave(new Random().nextLong(), Color.GREEN, 20,
                        new Point2D(FlowerPrototype.WIDTH/2, 20));
                SaveInfo.GrowthInfo growthInfo = info.new GrowthInfo(0, 6, 4);
                ArrayList<SaveInfo.PetalGroupSave> petals = new ArrayList<SaveInfo.PetalGroupSave>();
                for(String path : petalPaths) {
                    petals.add(info.new PetalGroupSave(petalColours.get(rand.nextInt(petalColours.size())),
                    path, 1, 0));
                }
                info = new SaveInfo(head, stem, growthInfo, petals);
                info.petalIndices.clear();
                for(int i = 0; i < slider.getValue(); i++) {
                    info.petalIndices.add(0);
                }
                game.info = info;
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });
        table.add(buttonGo).colspan(3).fillX().center();

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