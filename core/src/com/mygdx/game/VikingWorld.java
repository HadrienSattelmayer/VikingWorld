package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Screens.MainMenu;
import com.mygdx.game.Screens.PlayScreen;

public class VikingWorld extends Game {
	public static final int V_WEIDTH = 400;  //ALTURA E LARGURA VIRTUAL
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100; // PIXELS POR METRO

	//COLISÕES
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short VIKING_BIT = 2;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short FINAL_BIT = 128;


	public SpriteBatch batch;


	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();

	}
}
