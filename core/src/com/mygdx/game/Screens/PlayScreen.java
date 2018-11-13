package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.HellHound;
import com.mygdx.game.Sprites.Inimigo;
import com.mygdx.game.Sprites.Viking;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.VikingWorld;
import com.mygdx.game.Tools.WorldContactListener;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class PlayScreen implements Screen {
    //ATRIBUTOS DE REFERÊNCIA DO GAME, USADO PARA DEFINIR TELAS E PERSONAGENS
    private VikingWorld game;
    private TextureAtlas atlas;

    //ATRIBUTOS BÁSICOS DA PLAYSCREEN
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //ATRIBUTOS TILED MAP, USADOS PARA DEFINIR MAPAS
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //ATRIBUTOS BOX2D
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //SPRITES
    private Viking player; //CRIAR UM OBJETO DA CLASSE VIKING

    public PlayScreen(VikingWorld game){
        //PASSAR O PACOTE CRIADO PELO "TEXTUREPACKER" PARA A CLASSE RESPONSÁVEL POR IDENTIFICAR AS TEXTURAS
        atlas = new TextureAtlas("viking.pack");

        this.game = game;

        //CRIADO CAMERA NA PLAYSCREEN PARA SEGUIR O VIKING POR ONDE PASSAR NO MAPA
        gamecam = new OrthographicCamera();

        //CRIADO UM FITVIEWPORT PARA MANTER A IMAGEM A MESMA SE COLOCAR TELA CHEIA
        gamePort = new FitViewport(VikingWorld.V_WEIDTH/ VikingWorld.PPM, VikingWorld.V_HEIGHT/ VikingWorld.PPM, gamecam);

        //INSTANCIAR A CLASSE "Hud" ONDE ESTÁ DEFINIDO SCORE/ TIMERS/ LEVEL
        hud = new Hud(game.batch);

        //INSTANCIAR A CLASSE ONDE SE CARREGA AS FASES DO JOGO
        mapLoader = new TmxMapLoader();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            map = mapLoader.load("mapa1.tmx");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            map = mapLoader.load("mapa2.tmx");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            map = mapLoader.load("mapa3.tmx");
        }
        renderer = new OrthogonalTiledMapRenderer(map, 1 / VikingWorld.PPM);

        //SETAR POSIÇÃO DA GAMECAM PARA QUE ELA COMECE AO INÍCIO DO MAPA
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        //CRIAR O MUNDO E COLOCAR GRAVIDADE NOS EIXOS X E Y
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //CRIAR VIKING NO MUNDO
        player = new Viking(this);

        //ADICIONAR OUVINTE NO MUNDO
        world.setContactListener(new WorldContactListener());


    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void  update(float dt){
        handleImput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);


        for(Inimigo inimigo : creator.getHh()){
            inimigo.update(dt);
        }

        //ATUALIZA SCORE, TIME
        hud.update(dt);

        //VERIFICA SE O VIKING ESTÁ COM STATUS MORTO E SE ESTIVER A CAMERA PARA ONDE O VIKING ESTÁ
        if (player.estadoAtual != Viking.Estado.MORTO) {
            gamecam.position.x = player.b2body.getPosition().x;
        }
        //FAZ COM QUE A CAMERA DO JOGO SIGA O VIKING
        gamecam.update();
        renderer.setView(gamecam);

        // SE O VIKING ESTIVER ABAIXO DA ALTURA VIRTUAL (CAIR NO BURACO), VIKING MORRE.
        if (player.getY() < Gdx.graphics.getMonitor().virtualY){
            player.atingido();
        }
    }

    //DEFINIR O QUE ACONTECE QUANDO AS TECLAS: LADO ESQUERDO, LADO DIREITO E PARA CIMA SÃO PRESSIONADAS
    public void handleImput(float dt){

        if (player.estadoAtual != Viking.Estado.MORTO) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        //LIMPAR A TELA
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //RENDERIZAR O MAPA
        renderer.render();
        b2dr.render(world, gamecam.combined);

        //DESENHA NO JOGO A TEXTURA OBTIDA PELO PACOTE E IDENTIFICADA PELA CLASSE
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        //DESENHA O VIKING
        player.draw(game.batch);

        //DESENHA OS CÃES PELO MAPA
        for(Inimigo inimigo : creator.getHh()){
            inimigo.draw(game.batch);
        }

        game.batch.end();

        //DESENHAR OS SCORES NO CABEÇALHO DO JOGO
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //SE O JOGO ESTIVER EM GAME OVER, CHAMAR A TELA GAMEOVER
        if (gameOver()){
            game.setScreen(new GameOver(game));
            dispose();
        }
        //SE O VIKING PASSOU DE FASE, CHAMAR TELA DE PARABENS
        if (menu()){
            game.setScreen(new Parabens(game));
            dispose();
        }
    }

    //VERIFICA QUANDO O JOGO ESTÁ EM GAME OVER
    public boolean gameOver(){
        if(player.estadoAtual == Viking.Estado.MORTO && player.getTemporizadorEst() < 3){
            return true;
        }
        return false;
    }
    //VERIFICA QUANDO VIKING PASSOU DE FASE
    public boolean menu(){
        if (player.estadoAtual == Viking.Estado.WIN){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //ATUALIZA AS MEDIDAS DA JANELA
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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

        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();


    }
}
