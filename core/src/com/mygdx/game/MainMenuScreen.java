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
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.SaveItems.*;
import com.sun.javafx.geom.Point2D;

import java.util.*;

public class MainMenuScreen implements Screen {
    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
    private class PetalSlider {
        public int imageIndex;
        public Slider slider = new Slider(0, 10, 1, false, skin);
        public PetalSlider(int imageIndex) {
            this.imageIndex = imageIndex;
            slider.setValue(3);
            sliders.add(this);
        }
    }

    private Stage stage = new Stage();
    private Table table = new Table();

    private TextButton buttonFlower = new TextButton("Flowers", skin),
            buttonExit = new TextButton("Exit", skin);
    private ArrayList<PetalSlider> sliders = new ArrayList<PetalSlider>();
    private ArrayList<Image> petalImages = new ArrayList<Image>();
    private Label titleLabel = new Label("Flower Prototype!", skin);

    Color[] permittedColours = new Color[] {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.MAROON,
            Color.NAVY, Color.ORANGE, Color.PINK, Color.PURPLE, Color.RED,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    Map<Integer, ArrayList<Color>> petals = new HashMap<Integer, ArrayList<Color>>();
    ArrayList<Integer> petalIndices = new ArrayList<Integer>();

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
                ChooseProfiles();
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

    private ArrayList<String> GetSaves() {
        ArrayList<String> output = new ArrayList<String>();
        FileHandle saveDir = Gdx.files.local("bin/");
        for (FileHandle entry: saveDir.list()) {
            if (entry.name().endsWith(".json")) {
                output.add(entry.name());
            }
        }
        return output;
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

    /**
     * Gets the index of the first positive slider after (and including) the index given
     * @param index
     * @return
     */
    public int GetChosenSliderAfter(int index) {
        int firstChosen = -1;
        for(int i = index; i < sliders.size() && firstChosen == -1; i++) {
            PetalSlider petalSlider = sliders.get(i);
            if(petalSlider.slider.getValue() > 0) {
                firstChosen = i;
            }
        }
        return firstChosen;
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

    public void ChooseProfiles() {
        stage.clear();
        table.clear();
        Table subTable = new Table();
        for(final String fileName : GetSaves()) {
            TextButton saveButton = new TextButton(fileName.substring(0, fileName.length() - 5), skin);
            saveButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    game.info = SaveInfo.LoadSave(fileName);
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                }
            });
            subTable.add(saveButton).pad(10).fillX().center().row();
        }
        TextButton createButton = new TextButton("Create new flower", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ChoosePetalSprites();
            }
        });
        subTable.add(createButton).fillX().center().row();
        ScrollPane scrollPane = new ScrollPane(subTable);
        table.add(scrollPane);

        table.setFillParent(true);
        stage.addActor(table);
    }

    public void ChoosePetalSprites() {
        stage.clear();
        table.clear();
        table.add(new Label("Pick your petals and colours!", skin)).colspan(2).pad(10).fillX().center().row();

        Table scrollTable = new Table();
        petalImages = GetImages("textures/petals/monochrome");
        for(int i = 0; i < petalImages.size(); i++) {
            final Image image = petalImages.get(i);
            //petals.put(i, new ArrayList<Color>());
            scrollTable.add(image).pad(10).left().expandX().row();
            final PetalSlider petalSlider = new PetalSlider(i);
            final Label petalCount = new Label(Float.toString(petalSlider.slider.getValue()), skin);
            petalSlider.slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    petalCount.setText(Float.toString(petalSlider.slider.getValue()));
                }
            });
            petalSlider.slider.addListener(new InputListener() {
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    event.stop();
                    return false;
                }
            });
            //.addListener(stopTouchDown);
            //sliders.add(petalSlider);
            scrollTable.add(petalSlider.slider).fillX().pad(5);
            scrollTable.add(petalCount).left().padRight(10).row();
        }
        TextButton chooseHead = new TextButton("Next!", skin);
        chooseHead.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                for(PetalSlider slider : sliders) {
                    if(slider.slider.getValue() > 0) { //Ensures only chosen colours are in petalgroups
                        petals.put(slider.imageIndex, new ArrayList<Color>());
                    }
                }
                int firstChosen = GetChosenSliderAfter(0);
                ChooseColours(firstChosen);
            }
        });

        table.add(new ScrollPane(scrollTable)).colspan(2).fillX().center().row();
        table.add(chooseHead).center();
        table.setFillParent(true);
        stage.addActor(table);
    }
    public void ChooseColours(final int petalIndex) {
        table.clear();

        Image petalImage = petalImages.get(petalIndex);
        table.add(petalImage).center().padBottom(20).row();

        Table colourTable = new Table();
        for(Color colour : permittedColours) {
            final Image colourImage = new Image(new Texture("textures/ColourSquare.png"));
            colourImage.setColor(colour.r, colour.g, colour.b, 0.5f);
            colourImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    //Add/remove colour to appropriate list
                    Color colour = colourImage.getColor();

                    if(petals.containsKey(petalIndex)) {
                        ArrayList<Color> colourList = petals.get(petalIndex);
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
            colourTable.add(colourImage).padRight(10);
        }
        table.add(new ScrollPane(colourTable)).padBottom(5).row();
        TextButton next = new TextButton("Next!", skin);
        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if(petalIndex < petals.size() - 1) {
                    ChooseColours(GetChosenSliderAfter(petalIndex + 1));
                }
                else {
                    ChooseHead();
                }
            }
        });
        table.add(next).center();
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
                HeadSave head = new HeadSave(headColour, headImage.getName());
                StemSave stem = new StemSave(new Random().nextLong(), Color.GREEN, 20,
                new Point2D(FlowerPrototype.WIDTH/2, 20));
                GrowthInfo growthInfo = new GrowthInfo(0, 6, 4);
                ArrayList<PetalGroupSave> petalGroups = new ArrayList<PetalGroupSave>();
                Random rand = new Random();

                for (Integer key : petals.keySet())
                {
                    ArrayList<Color> colours = petals.get(key);
                    ArrayList<Integer> groupIndices = new ArrayList<Integer>();
                    //int colourAmount = colours.size();
                    for(Color colour : colours) {
                        groupIndices.add(petalGroups.size() - 1);
                        petalGroups.add(new PetalGroupSave(colour, petalImages.get(key).getName(), 1, 0));
                    }
                    int petalAmount = (int)(sliders.get(key).slider.getValue()); //Ye gods this is really cobbled together. Damn being ill
                    for(int i = 0; i < petalAmount; i++) {
                        petalIndices.add(groupIndices.get(rand.nextInt(groupIndices.size())));
                    }
                }
                int petalCount = 0;

                ArrayList<Integer> indices = Flower.GetIndexMix(petalGroups.size() - 1, petalCount);
                SaveInfo info = new SaveInfo(head, stem, growthInfo, petalGroups, indices);
                info.Name = "Test Save";
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