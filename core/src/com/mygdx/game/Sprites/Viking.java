package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.VikingWorld;

public class Viking extends Sprite {
    //POSSÍVEIS ESTADOS PARA O VIKING
    public enum Estado{CAINDO, PULANDO, PARADO, CORRENDO, MORTO, WIN};
    public Estado estadoAtual;
    public Estado estadoAnterior;

    private float temporizadorEst;
    private boolean correndoDireita;
    private boolean vikingIsDead;
    private boolean vikingWin;

    //ATRIBUTOS
    public World world;
    public Body b2body;

    //VARIAVEIS DE TEXTURAS PARA VIKING PARADO
    private TextureRegion vikingParado;
    private TextureRegion vikingMorto;
    //VARIAVEIS PARA ANIMAÇÃO DO VIKING
    private Animation vikingCorrendo;
    private Animation vikingPulo;



    public Viking(PlayScreen screen){

        super(screen.getAtlas().findRegion("VikingMovimento1"));
        this.world = screen.getWorld();

        //DEFINE O PRIMEIRO ESTADO COMO PARADO
        estadoAtual = Estado.PARADO;
        estadoAnterior = Estado.PARADO;
        //SETAR TEMPORIZADOR DE ESTADO EM ZERO
        temporizadorEst = 0;
        //DEFINE QUE O VIKING ESTÁ CORRENDO PARA A DIREITA
        correndoDireita = true;

        //ARRAY DE FRAMES DA ANIMAÇÃO DO VIKING
        Array<TextureRegion> frames = new Array<TextureRegion>();

        //ANIMAÇÃO DO VIKING CORRENDO.
        for(int i=1; i<3; i++){
            frames.add(new TextureRegion(getTexture(),i*70, 0,70,70));
        }
        vikingCorrendo = new Animation(0.1f, frames);
        frames.clear();

        //ANIMAÇÃO VIKING PULANDO
        for(int i=3; i<5; i++){
            frames.add(new TextureRegion(getTexture(),i*70, 0,70,70));
        }
        vikingPulo = new Animation(0.1f, frames);
        frames.clear();

        //ANIMAÇÃO VIKING PARADO
        vikingParado = new TextureRegion(getTexture(),0,0,70,70);
        defineViking();
        setBounds(0,0,17/VikingWorld.PPM,17/VikingWorld.PPM);
        setRegion(vikingParado);

        //ANIMAÇÃO VIKING MORTO
        vikingMorto = new TextureRegion(getTexture(),4*70, 0,70,70);


    }

    public void update(float dt){
        //ATUALIZA A ANIMAÇÃO
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        //PEGA DO MÉTODO GETESTADO O ESTADO QUE O VIKING SE ENCONTRA
        estadoAtual = getEstado();
        TextureRegion regiao;

        //VERIFICA EM QUAL ESTADO O PERSONAGEM ESTÁ E DEFINE QUAL ANIMAÇÃO VAI SER MOSTRADA
        switch (estadoAtual){
            case MORTO:
                regiao = vikingMorto;
                break;
            case PULANDO:
                regiao = (TextureRegion) vikingPulo.getKeyFrame(temporizadorEst/3);
                break;
            case CORRENDO:
                regiao = (TextureRegion) vikingCorrendo.getKeyFrame(temporizadorEst, true);
                break;
            case CAINDO:
            case PARADO:
            default:
                regiao = vikingParado;
                break;
        }

        //VERIFICA SE O PERSONAGEM CORRE PARA A ESQUERDA OU PARA A DIREITA
        if ((b2body.getLinearVelocity().x < 0 || !correndoDireita)&& !regiao.isFlipX()){
            regiao.flip(true, false);
            correndoDireita = false;
        }
        if ((b2body.getLinearVelocity().x > 0 || correndoDireita)&& regiao.isFlipX()){
            regiao.flip(true, false);
            correndoDireita = true;
        }

        temporizadorEst = estadoAtual == estadoAnterior ? temporizadorEst + dt : 0;
        estadoAnterior = estadoAtual;
        return regiao;
    }

    public Estado getEstado(){
        //VERIFICA O ESTADO QUE O VIKING ESTÁ
        if (vikingWin){
            return Estado.WIN; //PASSOU DE FASE
        }
        else if(vikingIsDead){
            return Estado.MORTO;
        }
        else if(b2body.getLinearVelocity().y > 0){
            return Estado.PULANDO;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return Estado.CAINDO;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return Estado.CORRENDO;
        }
        else
            return Estado.PARADO;
    }

    //METODO USADO EXCLUSIVAMENTE PARA SER CHAMADO NAS SITUAÇÕES QUE O VIKING MORRER. EX: COLISÃO COM BADBLOCKS, CAIR EM BURACOS
    public void atingido(){
        vikingIsDead = true;
        Filter filtro = new Filter();
        filtro.maskBits = VikingWorld.NOTHING_BIT;
        for(Fixture fixture : b2body.getFixtureList()){
            fixture.setFilterData(filtro);
        }
    }

    //METODO CHAMADO SEMPRE QUE O PERSONAGEM PASSAR DE FASE
    public void ganhou(){
        vikingWin = true;
    }

    public float getTemporizadorEst(){
        return temporizadorEst;
    }

    public void defineViking(){
        BodyDef bdef = new BodyDef();
        //DEFINE EM QUAL POSIÇÃO DO GAME O CORPO VAI SER DESENHADO
        bdef.position.set(200 / VikingWorld.PPM, 35 / VikingWorld.PPM);
        //DEFINE COMO UM CORPO DINÂMICO.
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //CRIA UM CORPO CIRCULAR
        CircleShape shape = new CircleShape();
        //DEFINE O RAIO DO CORPO CIRCULAR
        shape.setRadius(6 / VikingWorld.PPM);

        fdef.shape = shape;

        //DEFINE O TIPO CATEGORIA DO CORPO E COM O QUE O MESMO PODE COLIDIR
        fdef.filter.categoryBits = VikingWorld.VIKING_BIT;
        fdef.filter.maskBits = VikingWorld.ENEMY_BIT | VikingWorld.GROUND_BIT | VikingWorld.OBJECT_BIT | VikingWorld.FINAL_BIT;

        b2body.createFixture(fdef).setUserData(this);
    }
}
