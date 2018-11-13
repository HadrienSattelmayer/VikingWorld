package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.HellHound;
import com.mygdx.game.Sprites.Viking;
import com.mygdx.game.VikingWorld;

public class B2WorldCreator {


    private Array<HellHound> hh;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //criar colisão com os badblocks
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / VikingWorld.PPM, (rectangle.getY() + rectangle.getWidth() / 2) / VikingWorld.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rectangle.getWidth() / 2 / VikingWorld.PPM, rectangle.getHeight() / 2 / VikingWorld.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = VikingWorld.OBJECT_BIT;
            fdef.filter.maskBits = VikingWorld.VIKING_BIT | VikingWorld.ENEMY_BIT;
            body.createFixture(fdef);
        }

        //criar colisão com o chão
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / VikingWorld.PPM, (rectangle.getY() + rectangle.getWidth() / 2) / VikingWorld.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rectangle.getWidth() / 2 / VikingWorld.PPM, rectangle.getHeight() / 2 / VikingWorld.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = VikingWorld.GROUND_BIT;
            fdef.filter.maskBits = VikingWorld.ENEMY_BIT | VikingWorld.VIKING_BIT;
            body.createFixture(fdef);
        }

        hh = new Array<HellHound>();
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            hh.add(new HellHound(screen, rect.getX() / VikingWorld.PPM, rect.getY() / VikingWorld.PPM));
        }
        //DETERMINAR O FINAL DO PERCURSO
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / VikingWorld.PPM, (rectangle.getY() + rectangle.getWidth() / 2) / VikingWorld.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rectangle.getWidth() / 2 / VikingWorld.PPM, rectangle.getHeight() / 2 / VikingWorld.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = VikingWorld.FINAL_BIT;
            fdef.filter.maskBits = VikingWorld.VIKING_BIT;
            body.createFixture(fdef);
        }
    }
    public Array<HellHound> getHh() {
        return hh;
    }
}
