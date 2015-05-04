package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.FlowerItems.Flower;
import com.mygdx.game.SaveItems.*;

import java.util.*;

class MainMenuScreen implements Screen {
    private Stage stage = new Stage();
    private Table table = new Table();

    private Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

    private TextButton buttonFlower = new TextButton("Flowers", skin),
            buttonExit = new TextButton("Exit", skin);
    private Slider slider = new Slider(10, 30, 1, false, skin); //Slider for how many petals there should be
    private Label titleLabel = new Label("Flower Prototype!", skin);
    private TextField saveNameBox = new TextField("My Flower Name", skin);

    private Color[] permittedColours = new Color[] {
            Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.MAROON,
            Color.NAVY, Color.ORANGE, Color.PINK, Color.PURPLE, Color.RED,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    private Map<String, ArrayList<Color>> petals = new HashMap<String, ArrayList<Color>>();
    private Image headImage;
    private Image headColourImage; //this is so only 1 can be selected
    private Color headColour;

    private final FlowerPrototype game; //Pass along!

    private OrthographicCamera camera;

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

    /**
     * Fetch savefiles
     * @return Return list of savefile path names
     */
    private ArrayList<String> GetSaves() {
        ArrayList<String> output = new ArrayList<String>();
        FileHandle saveDir = Gdx.files.local("saves/");
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

        stage.act(); //Stage do your thing
        stage.draw(); //Render, o stage
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
        PlayExitOptions(); //Set initial UI
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Add options for exiting, or viewing flowers
     */
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

    private void ChooseProfiles() {
        stage.clear();
        table.clear();
        final Table subTable = new Table();
        for(final String fileName : GetSaves()) {
            final TextButton loadButton = new TextButton(fileName.substring(0, fileName.length() - 5), skin);
            //Button with savefile name (.json extension trimmed)
            loadButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.info = SaveInfo.LoadSave(fileName); //Will load this flower
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                }
            });

            Image cross = new Image(new Texture("textures/Cross.png")); //Delete image
            final ImageButton deleteButton = new ImageButton(skin);
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle(deleteButton.getStyle());
            style.imageUp = cross.getDrawable();
            style.imageDown = cross.getDrawable();
            deleteButton.setStyle(style); //Set delete button style
            deleteButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Dialog deleteBox = new Dialog("Are you sure?", skin) { //Make sure to prompt for delete
                        protected void result(Object object) {
                            if (Boolean.parseBoolean(object.toString())) { //ie yes
                                Gdx.files.local("/saves/" + fileName).delete();
                                subTable.removeActor(loadButton);
                                subTable.removeActor(deleteButton); //remove the buttons from stage
                            }
                        }
                    }
                    .text("Are you sure you'd like to delete "
                            + fileName.substring(0, fileName.length() - 5) + "?") //Set prompt text
                    .button("Yes", true)
                    .button("No", false);
                    deleteBox.show(stage);
                }
            });
            subTable.add(loadButton).fillX().center().pad(5);
            subTable.add(deleteButton).center().pad(5).row(); //Add load/delete buttons
        }
        TextButton createButton = new TextButton("Create new flower", skin); //For creating a new flower
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ChoosePetalColours();
            }
        });
        subTable.add(createButton).fillX().center().pad(10).row();
        ScrollPane scrollPane = new ScrollPane(subTable); //So that the stuff is scrollable if there are many
        table.add(scrollPane);

        table.setFillParent(true);
        stage.addActor(table);
    }

    /**
     * Adds UI for creating a new flower
     */
    private void ChoosePetalColours() {
        stage.clear();
        table.clear();
        Table subTable = new Table(); //Used to scroll petals vertically

        //For each petal, add its image and a row of colours next to it
        ArrayList<Image> images = DebugUtils.GetImages("textures/petals/monochrome");
        for(final Image image : images) {

            petals.put(image.getName(), new ArrayList<Color>());
            Table colourTable = new Table(); //What will be used to scroll the colours horizontally
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
            colourScroll.setScrollingDisabled(false, true); //Makes sure only horizontal scrolling for colours
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
                else { //Makes sure you select some colours
                    Dialog rejectionDialog = new Dialog("No petals selected", skin);
                    rejectionDialog.text("Please select some petal colours");
                    rejectionDialog.button("OK");
                    rejectionDialog.show(stage);
                }
            }
        });
        slider.setValue(15);
        final Label progressLabel = new Label(Float.toString(slider.getValue()), skin);
        slider.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                progressLabel.setText(Float.toString(slider.getValue())); //How many petals you have selected
            }
        });
        ScrollPane wholePane = new ScrollPane(subTable); //Vertical scrollpane
        wholePane.setScrollingDisabled(true, false);
        wholePane.setCancelTouchFocus(false);
        //THIS combination of above two lines was almost demonic to find. Stops some spooky action between the two scrollpanes

        //populates table and stage
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

    /**
     * Adds UI for choosing flower head
     */
    private void ChooseHead() {
        stage.clear();
        table.clear();
        table.add(new Label("Pick the middle of your flower!", skin)).pad(10).fillX().center().row();

        ArrayList<Image> headImages = DebugUtils.GetImages("textures/heads/monochrome");
        Table headTable = new Table();
        Table colourTable = new Table();

        for(final Image image : headImages) { //For each head image
            image.setColor(1f, 1f, 1f, 0.5f);
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    if(headImage != image) {
                        if(headImage != null) headImage.setColor(1f, 1f, 1f, 0.5f); //Wash out what had just been selected
                        image.setColor(1f, 1f, 1f, 1f); //Make current one fully opaque
                        headImage = image; //Set currently selected head image
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
                    if (headColourImage != colourImage) { //set the colour of the currently selected head
                        colourImage.setColor(colour.r, colour.g, colour.b, 1f); //Opaque new
                        if(headColourImage != null) {
                            Color oldColour = headColourImage.getColor();
                            headColourImage.setColor(oldColour.r, oldColour.g, oldColour.b, 0.5f); //Wash out old
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
                            4, new Vector2(FlowerPrototype.WIDTH / 2, 20));
                    GrowthSave growthSave = new GrowthSave(0, 0, 0, 0, 6, 2f, 0f);
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
                    game.info = new SaveInfo(name, head, stem, growthSave, petalGroups, indices);
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