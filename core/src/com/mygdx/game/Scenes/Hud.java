package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Sprites.Viking;
import com.mygdx.game.VikingWorld;

// CLASSE RESPONSÁVEL PELA PARTE DO SCORE, PONTUAÇÃO, QUANTIDADE DE VIDAS DO PERSONAGEM, E PELO LUGAR DO MAPA QUE ESTÁ
public class Hud implements Disposable{

    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private boolean timeUp;
    private float timeCount;
    private static int score;

    //ATRIBUTOS PARA MOSTRAR NO GAME O TEMPO, PONTUAÇÃO, N° DO MAPA.
    Label countdownnLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label vikingLabel;

    public Hud(SpriteBatch sb){
        //VARIÁVEIS DE RASTREAMENTO
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        //DEFINIR TAMANHO PADRÃO E TIPO DE VISUALIZAÇÃO(FitViewport) DA TELA DO JOGO DESSA CLASSE
        viewport = new FitViewport(VikingWorld.V_WEIDTH, VikingWorld.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //CRIAR UMA TABELA
        Table table = new Table();
        //COLOCAR A PRIMEIRA LINHA DA TABELA NO TOPO DA TELA
        table.top();
        //TAMANHO DA TABELA IGUAL AO TAMANHO DA TELA
        table.setFillParent(true);

        //DEFINIR AS PROPRIEDADES DE FONTE, TAMANHO E QTD DE CARACTERES DE CADA LABEL
        countdownnLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        vikingLabel = new Label("JAVA´R REIDT", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //ADICIONAR AS LABELS NAS LINHAS E COLUNAS QUE DESEJAMOS DA TABELA CRIADA
        table.add(vikingLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownnLabel).expandX();

        stage.addActor(table);

    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownnLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            addScore(1);
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));

    }

    @Override
    public void dispose() {

        stage.dispose();
    }

    public boolean isTimeUp() {
        return timeUp;
    }


}

