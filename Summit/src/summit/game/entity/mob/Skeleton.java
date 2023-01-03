/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity.mob;

import summit.game.GameUpdateEvent;
import summit.game.ai.HostileMobAI;
import summit.game.entity.Entity;
import summit.game.item.ArrowItem;
import summit.game.item.BoneItem;
import summit.game.item.Bow;
import summit.game.item.GoldCoin;
import summit.game.item.Sword;
import summit.gfx.Sprite;

public class Skeleton extends HumanoidEntity{

    public Skeleton(float x, float y) {
        super(x, y);
        super.setSprite(Sprite.SKELETON_FACE_BACK);
        super.setAI(new HostileMobAI(this));
        super.setAttackCooldownMS(1000);
        super.setMaxHealth(7);
        super.setHealth(getMaxHealth());

        super.setSpriteStates(Sprite.SKELETON_SUBMERGED, 
                                Sprite.SKELETON_FACE_BACK, 
                                Sprite.SKELETON_NEUTRAL);

        super.setEquipped(new Bow(this));

        super.addItems(new BoneItem(this), (int)(Math.random()*7));
        super.addItems(new GoldCoin(this), (int)(Math.random()*7));

        //drop these items once destroyed
        super.addItems(new ArrowItem(this), 100);

        super.set(hostile, true);
    }

    @Override
    public void damage(GameUpdateEvent e, Entity hitBy){
        if(getHealth() - hitBy.getAttackDamage() <= 0){
            //clear arrows
            getItems().remove(Sprite.ARROW_ITEM);

            //add random amount of arrow
            addItems(new ArrowItem(this), (int)(Math.random()*5));
        }
        super.damage(e, hitBy);
    }
}
