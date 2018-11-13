package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.VikingWorld;

public class HellHound extends Inimigo {
    private float stateTime;
    private Animation movimentoAnimacao;
    private Array<TextureRegion> frames;

    public HellHound(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i=0; i<5; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("hh"), i * 67, 0, 67, 30));
        }
        movimentoAnimacao = new Animation(0.4f, frames);

        stateTime = 0;
        setBounds(getX(), getY(), 30/ VikingWorld.PPM,27/VikingWorld.PPM);
    }

    @Override
    protected void defineInimigo() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / VikingWorld.PPM);
        fdef.filter.categoryBits = VikingWorld.ENEMY_BIT;
        fdef.filter.maskBits = VikingWorld.VIKING_BIT | VikingWorld.GROUND_BIT | VikingWorld.OBJECT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        b2body.setLinearVelocity(velocity);
        setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y - getHeight() / 2)+ 4 / VikingWorld.PPM);
        setRegion((TextureRegion) movimentoAnimacao.getKeyFrame(stateTime, true));

    }
}
