package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.VikingWorld;
import com.mygdx.game.Screens.PlayScreen;

public class MainMenu implements Screen {
    private VikingWorld game;

    private Viewport viewport;
    private Stage stage;

    public MainMenu (VikingWorld game)
    {
        this.game = game;
        viewport = new FitViewport(VikingWorld.V_WEIDTH, VikingWorld.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((VikingWorld) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.debugTable();
        table.setFillParent(true);

        Label gameOverLabel = new Label("Viking World", font);
        Label play1 = new Label("Pressione de 1 a 3 para escolher a fase", font);


        table.add(gameOverLabel).expandX();
        table.row();
        table.add(play1).expandX().padTop(10f);

        stage.addActor(table);

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.graphics.getGL20();
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            game.setScreen(new PlayScreen(game));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            game.setScreen(new PlayScreen(game));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            game.setScreen(new PlayScreen(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
