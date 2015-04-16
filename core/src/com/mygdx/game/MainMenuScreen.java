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
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

    private TextButton buttonFlower = new TextButton("Flowers", skin),
            buttonExit = new TextButton("Exit", skin);
    private Slider slider = new Slider(3, 20, 1, false, skin);
    private Label titleLabel = new Label("Flower Prototype!", skin);
    private TextField saveNameBox = new TextField("My Flower Name", skin);

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
        table.add(buttonFlower).size(150, 60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);
    }

    public void ChooseProfiles() {
        stage.clear();
        table.clear();
        final Table subTable = new Table();
        for(final String fileName : GetSaves()) {
            final TextButton saveButton = new TextButton(fileName.substring(0, fileName.length() - 5), skin);
            saveButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.info = SaveInfo.LoadSave(fileName);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                }
            });

            Image cross = new Image(new Texture("textures/Cross.png"));
            final ImageButton deleteButton = new ImageButton(skin);
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(deleteButton.getStyle());
            style.imageUp = cross.getDrawable();
            style.imageDown = cross.getDrawable();
            deleteButton.setStyle(style);
            deleteButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Dialog deleteBox = new Dialog("Are you sure?", skin) {
                        protected void result(Object object) {
                            if (Boolean.parseBoolean(object.toString())) { //ie yes
                                Gdx.files.local("/bin/" + fileName).delete();
                                subTable.removeActor(saveButton);
                                subTable.removeActor(deleteButton);
                            }
                        }
                    }
                    .text("Are you sure you'd like to delete "
                            + fileName.substring(0, fileName.length() - 5) + "?")
                    .button("Yes", true)
                    .button("No", false);
                    deleteBox.show(stage);
                }
            });
            subTable.add(saveButton).fillX().center().pad(5);
            subTable.add(deleteButton).center().pad(5).row();
        }
        TextButton createButton = new TextButton("Create new flower", skin);
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ChoosePetalColours();
            }
        });
        subTable.add(createButton).fillX().center().pad(10).row();
        ScrollPane scrollPane = new ScrollPane(subTable);
        table.add(scrollPane);

        table.setFillParent(true);
        stage.addActor(table);
    }

    public void ChoosePetalColours() {
        stage.clear();
        table.clear();
        Table subTable = new Table();

        ArrayList<Image> images = DebugUtils.GetImages("textures/petals/monochrome");
        for(final Image image : images) {
            petals.put(image.getName(), new ArrayList<Color>());
            Table colourTable = new Table();
            subTable.add(image).size(image.getWidth(), image.getHeight()).pad(10).center();
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
                colourTable.add(colourImage).padRight(10);
            }
            ScrollPane colourScroll = new ScrollPane(colourTable);
            colourScroll.setScrollingDisabled(false, true);
            subTable.add(colourScroll).padBottom(5).row();
        }
        TextButton chooseHead = new TextButton("Next!", skin);
        chooseHead.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                int totalColours = 0;
                for(ArrayList<Color> colours : petals.values()) {
                    totalColours += colours.size();
                }
                if(totalColours != 0) {
                    ChooseHead();
                }
                else {
                    Dialog rejectionDialog = new Dialog("No petals selected", skin);
                    rejectionDialog.text("Please select some petal colours");
                    rejectionDialog.button("OK");
                    rejectionDialog.show(stage);
                }
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
        ScrollPane wholePane = new ScrollPane(subTable);
        wholePane.setScrollingDisabled(true, false);
        wholePane.setCancelTouchFocus(false); //THIS combination of two lines was almost demonic to find

        table.add(new Label("What's your flower name?", skin)).center().row();
        table.add(new Label("Pick your petals and colours!", skin)).pad(10).fillX().center().row();
        table.add(saveNameBox).center().row();
        table.add(wholePane).row();
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

        ArrayList<Image> headImages = DebugUtils.GetImages("textures/heads/monochrome");
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
                if(headColour != null && (headImage != null && headImage.getName() != null)) {
                    HeadSave head = new HeadSave(headColour, headImage.getName());
                    StemSave stem = new StemSave(new Random().nextLong(), Color.GREEN, 20,
                            new Point2D(FlowerPrototype.WIDTH / 2, 20));
                    GrowthInfo growthInfo = new GrowthInfo(0, 0, 6, 2f, 0f);
                    ArrayList<PetalGroupSave> petalGroups = new ArrayList<PetalGroupSave>();

                    for (String key : petals.keySet()) {
                        ArrayList<Color> colours = petals.get(key);
                        for (Color colour : colours) {
                            petalGroups.add(new PetalGroupSave(colour, key, 1, 0));
                        }
                    }
                    int petalCount = (int) (slider.getValue());
                    ArrayList<Integer> indices = Flower.GetIndexMix(petalGroups.size() - 1, petalCount);
                    String name = saveNameBox.getText();
                    SaveInfo info = new SaveInfo(name, head, stem, growthInfo, petalGroups, indices);
                    game.info = info;
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                }
                else {
                    Dialog rejectionDialog = new Dialog("No head selected", skin);
                    rejectionDialog.text("Please select a head and colour");
                    rejectionDialog.button("OK");
                    rejectionDialog.show(stage);
                }
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