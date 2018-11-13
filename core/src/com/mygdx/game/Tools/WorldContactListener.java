package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.HellHound;
import com.mygdx.game.Sprites.Inimigo;
import com.mygdx.game.Sprites.ObjetoTileInterativo;
import com.mygdx.game.Sprites.Viking;
import com.mygdx.game.VikingWorld;

public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case VikingWorld.ENEMY_BIT | VikingWorld.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == VikingWorld.ENEMY_BIT) {
                    ((Inimigo) fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Inimigo) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case VikingWorld.VIKING_BIT | VikingWorld.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == VikingWorld.VIKING_BIT)
                    ((Viking) fixA.getUserData()).atingido();
                else
                    ((Viking) fixB.getUserData()).atingido();
            break;
            case VikingWorld.VIKING_BIT | VikingWorld.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == VikingWorld.VIKING_BIT)
                    ((Viking) fixA.getUserData()).atingido();
                else
                    ((Viking) fixB.getUserData()).atingido();
            break;
            case VikingWorld.VIKING_BIT | VikingWorld.FINAL_BIT:
                if (fixA.getFilterData().categoryBits == VikingWorld.VIKING_BIT)
                    ((Viking) fixA.getUserData()).ganhou();
                else
                    ((Viking) fixB.getUserData()).ganhou();
                break;


        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
