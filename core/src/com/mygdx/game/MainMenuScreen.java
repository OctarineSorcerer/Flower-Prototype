package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sun.javafx.geom.Point2D;

import java.util.*;

public class MainMenuScreen implements Screen {
    class PathAndTints {
        public String path;
        public ArrayList<Color> tints = new ArrayList<Color>();
    }
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

    private TextButton buttonFlower = new TextButton("Flower", skin),
            buttonExit = new TextButton("Exit", skin);
    private Slider slider = new Slider(3, 20, 1, false, skin);
    private Label titleLabel = new Label("Flower Prototype!", skin);

    Color[] permittedColours = new Color[] {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.MAROON,
            Color.NAVY, Color.ORANGE, Color.PINK, Color.PURPLE, Color.RED,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    Map<String, ArrayList<Color>> petals = new HashMap<String, ArrayList<Color>>();
    Image headImage, headColourImage; //this is so only 1 can be selected
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
                ChoosePetalColours();
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
        /*petalColours.add(Color.RED);*/
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

    public void ChoosePetalColours() {
        stage.clear();
        table.clear();
        table.add(new Label("Pick your petals and colours!", skin)).pad(10).fillX().center().row();

        ArrayList<Image> images = GetImages("textures/petals/monochrome");
        for(final Image image : images) {
            petals.put(image.getName(), new ArrayList<Color>());
            Table subTable = new Table();
            table.add(image).pad(10).center();
            for(Color colour : permittedColours) {
                final Image colourImage = new Image(new Texture("textures/ColourSquare.png"));
                colourImage.setColor(colour.r, colour.g, colour.b, 0.5f);
                colourImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y)
                    {
                        //Add/remove colour to appropriate list
                        Color colour = colourImage.getColor();
                        String imageName = image.getName();

                        if(petals.containsKey(imageName)) {
                            ArrayList<Color> colourList = petals.get(imageName);
                            if(colourList.contains(colour)) {
                                colourList.remove(colour);
                                colourImage.setColor(colour.r, colour.g, colour.b, 0.5f);
                            }
                            else {
                                colourList.add(colour);
                                colourImage.setColor(colour.r, colour.g, colour.b, 1.0f);
                            }
                        }
                    }
                });
                subTable.add(colourImage).padRight(10);
            }
            table.add(new ScrollPane(subTable)).padBottom(5).row();
        }
        TextButton chooseHead = new TextButton("Next!", skin);
        chooseHead.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ChooseHead();
            }
        });
        slider.setValue(10);
        final Label progressLabel = new Label(Float.toString(slider.getValue()), skin);
        slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                progressLabel.setText(Float.toString(slider.getValue()));
                }
            });
        table.add(slider).colspan(2).fillX().pad(5);
        table.add(progressLabel).left().padRight(10).row();
        table.add(chooseHead).colspan(2).center();
        table.setFillParent(true);
        stage.addActor(table);
    }
    public void ChooseHead() {
        stage.clear();
        table.clear();
        table.add(new Label("Pick the middle of your flower!", skin)).pad(10).fillX().center().row();

        ArrayList<Image> headImages = GetImages("textures/heads/monochrome");
        Table headTable = new Table();
        Table colourTable = new Table();
        for(final Image image : headImages) {
            image.setColor(1f, 1f, 1f, 0.5f);
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(headImage != image) {
                        if(headImage != null) headImage.setColor(1f, 1f, 1f, 0.5f);
                        image.setColor(1f, 1f, 1f, 1f);
                        headImage = image;
                    }
                }
            });
            headTable.add(image).pad(5).center();
        }
        for(final Color colour : permittedColours) {
            final Image colourImage = new Image(new Texture("textures/ColourSquare.png"));
            colourImage.setColor(colour.r, colour.g, colour.b, 0.5f);
            colourImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (headColourImage != colourImage) {
                        colourImage.setColor(colour.r, colour.g, colour.b, 1f);
                        if(headColourImage != null) {
                            Color oldColour = headColourImage.getColor();
                            headColourImage.setColor(oldColour.r, oldColour.g, oldColour.b, 0.5f);
                        }
                        headColourImage = colourImage;
                        headColour = colour;
                    }
                }
            });
            colourTable.add(colourImage).pad(5).center();
        }
        table.add(new ScrollPane(headTable)).row();
        table.add(new ScrollPane(colourTable)).row();
        TextButton buttonGo = new TextButton("Go!", skin);
        buttonGo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Random rand = new Random();
                SaveInfo info = new SaveInfo();
                SaveInfo.HeadSave head = info.new HeadSave(headColour, headImage.getName());
                SaveInfo.StemSave stem = info.new StemSave(new Random().nextLong(), Color.GREEN, 20,
                new Point2D(FlowerPrototype.WIDTH/2, 20));
                SaveInfo.GrowthInfo growthInfo = info.new GrowthInfo(0, 6, 4);
                ArrayList<SaveInfo.PetalGroupSave> petalGroups = new ArrayList<SaveInfo.PetalGroupSave>();

                for (String key : petals.keySet())
                {
                    ArrayList<Color> colours = petals.get(key);
                    for(Color colour : colours) {
                        petalGroups.add(info.new PetalGroupSave(colour, key, 1, 0));
                    }
                }
                info = new SaveInfo(head, stem, growthInfo, petalGroups);
                info.petalIndices.clear();
                for(int i = 0; i < slider.getValue(); i++) {
                    info.petalIndices.add(0);
                }
                game.info = info;
                ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        });
        table.add(buttonGo).center();
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