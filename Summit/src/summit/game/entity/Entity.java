/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.game.entity;

import summit.game.GameUpdateEvent;
import summit.game.animation.ParticleAnimation;
import summit.game.entity.projectile.Projectile;
import summit.game.gamemap.GameMap;
import summit.game.item.AppleItem;
import summit.game.item.ArrowItem;
import summit.game.item.BoneItem;
import summit.game.item.GoldCoin;
import summit.game.item.Item;
import summit.game.item.ItemStorage;
import summit.game.item.SnowballItem;
import summit.game.item.StickItem;
import summit.game.item.WeaponItem;
import summit.gfx.ColorFilter;
import summit.gfx.Light;
import summit.gfx.OrderPaintEvent;
import summit.gfx.PaintEvent;
import summit.gfx.RenderLayers;
import summit.util.Direction;
import summit.util.GameObject;
import summit.util.GameScheduler;
import summit.util.GraphicsScheduler;
import summit.util.ScheduledEvent;

/**
 *
 * 
 * The abstract class Entities extends GameObject and is the base class for all
 * entities in the game such as player characters and non-player characters.
 * 
 * It contains properties and methods for handling movement, collisions,
 * inventory, and combat.
 * 
 * @author Aadi J, Aditya B, Sanjay R, Aadithya R. S.
 */
public abstract class Entity extends GameObject {

    private ItemStorage items;

    // currently equipped weapon
    private WeaponItem equipped;

    // the map this entity belongs to
    private String curMap = " ";

    private float dx, dy;

    // knockback lasts for 2 seconds
    private Knockback kb;

    // last x and last y; used to check if entity is moving
    private float lx, ly;

    // facing of the entity
    private Direction facing;

    private Light shadow;

    // private float attackDamage;
    private int damageCooldownMS;

    // private float attackRange;
    private int attackCooldownMS;

    // coefficient
    private float damageResistance;

    private float maxHealth;
    private float health;

    /**
     * 
     * Constructs an Entities object with specified x, y, width, and height.
     * 
     * @param x      the x-coordinate of the entity
     * @param y      the y-coordinate of the entity
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public Entity(float x, float y, float width, float height) {
        super(x, y, width, height);
        super.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER);
        super.setMoveable(true);

        this.maxHealth = 1;
        this.health = maxHealth;
        // this.attackDamage = 0;
        // this.attackRange = 2;
        this.damageResistance = 0;

        this.lx = x;
        this.ly = y;

        this.shadow = new Light(x, y, 1f, -50, -50, -50);

        this.items = new ItemStorage(this);

        items.addItems(new ArrowItem(this), 0);
        items.addItems(new SnowballItem(this), 0);
        items.addItems(new AppleItem(this), 0);
        items.addItems(new StickItem(this), 0);
        items.addItems(new BoneItem(this), 0);
        items.addItems(new GoldCoin(this), 0);

        // items.put(Sprite.ARROW_ITEM, new Stack<Item>());
        // items.put(Sprite.SNOWBALL, new Stack<Item>());
        // items.put(Sprite.APPLE_ITEM, new Stack<Item>());
        // items.put(Sprite.STICK_ITEM, new Stack<Item>());
        // items.put(Sprite.BONE_ITEM, new Stack<Item>());
        // items.put(Sprite.GOLD_COIN, new Stack<Item>());

        this.add(attackCooldown);
        this.add(damageCooldown);
        this.add(destroyed);
        this.add(invulnerable);
        this.add(fireResistant);
        this.add(inWater);
        this.add(onFire);
        this.add(moving);
        this.add(projectileDamage);
        this.add(pickupItems);

        this.set(projectileDamage, true);
    }

    /**
     * 
     * Overrides the setRenderLayer method of the parent class to set the render
     * layer for the shadow and equipped weapon
     * 
     * @param ope the OrderPaintEvent object
     */
    @Override
    public void setRenderLayer(OrderPaintEvent ope) {
        super.setRenderLayer(ope);
        shadow.setRenderLayer(ope);

        if (equipped != null)
            equipped.setRenderLayer(ope);
    }

    /**
     * 
     * Updates the entity's state on each game update
     * 
     * @param e the GameUpdateEvent object
     */
    @Override
    public void update(GameUpdateEvent e) throws Exception {
        curMap = e.getMap().getName();

        if (health <= 0) {
            set(destroyed, true);
            return;
        }

        if (kb != null) {
            kb.update(e);
            if (kb.finished())
                this.kb = null;
        }
        updateMoving();
    }

    /**
     * 
     * The method collide is called when this entity collides with another entity.
     * This method checks if the other entity is on fire and sets the onFire flag to
     * true if it is.
     * 
     * @param ev GameUpdateEvent object
     * @param e  The other entity involved in the collision
     */
    @Override
    public void collide(GameUpdateEvent ev, Entity e) {
        if (e.is(onFire))
            set(onFire, true);
    }

    /**
     * 
     * The method damage is called when this entity takes damage from another
     * entity.
     * 
     * This method reduces the health of the entity, sets the knockback effect,
     * triggers a flash animation,
     * 
     * sets the damageCooldown flag, and checks if the entity's health has reached
     * zero and if so, sets the destroyed flag and
     * 
     * calls the pickupItems method if the other entity has the pickupItems flag
     * set.
     * 
     * @param e     GameUpdateEvent object
     * 
     * @param hitBy The other entity involved in the damage
     */
    public void damage(GameUpdateEvent e, Entity hitBy) {
        if (hitBy.is(attackCooldown) || this.is(damageCooldown))
            return;
        if (hitBy instanceof Projectile && !is(projectileDamage))
            return;

        health -= hitBy.getAttackDamage();

        this.setKnockback(10, 500, hitBy);

        // flash animation-----------------------------------------
        GraphicsScheduler.registerEvent(new ScheduledEvent(125, 8) {
            private boolean flash = false;

            @Override
            public void run() {
                if (!flash) {
                    setColorFilter(new ColorFilter(150, 0, 0));
                    flash = true;
                } else {
                    setColorFilter(ColorFilter.NOFILTER);
                    flash = false;
                }
            }
        });

        // ---------------------------------------------------------

        set(damageCooldown, true);

        if (health <= 0) {
            set(destroyed, true);

            if (hitBy.is(pickupItems)) {
                hitBy.pickupItems(this.getItems());
            }
        }
    }

    /**
     * 
     * The method reinit is called to reset the entity's state.
     * This method calls the reinit method of the parent class, the item storage,
     * and the equipped weapon.
     */
    @Override
    public void reinit() {
        super.reinit();
        items.reinit();
        if (equipped != null)
            equipped.reinit();
    }

    /**
     * 
     * The method gameClick is called when the entity is clicked in-game.
     * This method currently has no implementation.
     * 
     * @param e GameUpdateEvent object
     */
    @Override
    public void gameClick(GameUpdateEvent e) {
    }

    /**
     * 
     * The method attack is called when the entity attacks another entity.
     * 
     * This method checks if the attackCooldown flag is set, if so it does not
     * attack.
     * 
     * If the entity has an equipped weapon, it uses the weapon, otherwise it deals
     * direct damage.
     * 
     * @param targetX the x-coordinate of the target
     * 
     * @param targetY the y-coordinate of the target
     * 
     * @param ev      GameUpdateEvent object
     */
    public void attack(float targetX, float targetY, GameUpdateEvent ev) {
        if (is(attackCooldown))
            return;

        boolean hit = false;

        if (equipped != null)
            // spawn projectile towards the entity
            // or melee sweep damage towards the entity
            hit = equipped.useWeapon(targetX, targetY, ev);
        else {
            // direct damage
            // e.damage(ev, this);
        }

        if (hit && !is(attackCooldown))
            set(attackCooldown, true);
    }

    /**
     * 
     * The method destroy is called when the entity is destroyed.
     * This method creates a particle animation at the location of the entity with
     * the color of the entity.
     * 
     * @param e GameUpdateEvent object
     */
    public void destroy(GameUpdateEvent e) {
        e.getMap().addAnimation(
                new ParticleAnimation(getX(), getY(),
                        300,
                        20,
                        getColor()));
    }

    /**
     * 
     * The method clip is deprecated and is used if the entity is stuck in a wall.
     * This method sets the position of the entity to the rounded x and y coordinate
     * of the entity.
     * 
     * @param map The current game map
     */
    @Deprecated
    public void clip(GameMap map) {
        setPos(Math.round(getX()), Math.round(getY()));
    }

    /**
     * 
     * The method moveTo is used to move the entity to a new position on the game
     * map.
     * 
     * This method checks if the entity is moveable, and if so, it checks for any
     * obstacles in the new position and returns false if there is any.
     * 
     * @param map  The current game map
     * 
     * @param newX The new x-coordinate of the entity
     * 
     * @param newY The new y-coordinate of the entity
     */
    public boolean moveTo(GameMap map, float newX, float newY) {
        if (!isMoveable())
            return false;

        // leniancy
        float settle = 1 / 16f;

        for (float x = newX - getWidth() / 2f + settle; x <= newX + getWidth() / 2f
                - settle; x += (settle != 0 ? settle : 1 / 16f)) {
            for (float y = newY - getHeight() / 2f + settle; y <= newY + getHeight() / 2f
                    - settle; y += (settle != 0 ? settle : 1 / 16f)) {
                if (map.getTileAt(x, y) == null ||
                        map.getTileAt(x, y).isBoundary()) {

                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 
     * The method lineOfSight is used to check if there is a clear line of sight
     * between this entity and another entity on the game map.
     * This method checks for obstacles between the two entities and returns false
     * if there is any.
     * 
     * @param e   The other entity
     * @param map The current game map
     */
    public boolean lineOfSight(Entity e, GameMap map) {
        // vertically aligned
        if (e.getX() == getX()) {
            float start = Math.min(getY(), e.getY());
            float end = Math.max(getY(), e.getY());

            for (float y = start; y <= end; y++) {
                if (map.getTileAt(getX(), y).isBoundary()) {
                    return false;
                }
            }
            return true;
        }

        float m = (e.getY() - getY()) / (e.getX() - getX());
        float b = -1 * ((float) m * getX() - getY());

        float start = Math.min(getX(), e.getX());
        float end = Math.max(getX(), e.getX());

        for (float inc_x = start; inc_x < end; inc_x += (1f / map.getWidth())) {
            if (map.getTileAt(inc_x, (inc_x * m + b)).isBoundary()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * The updateMoving method checks if the entity's x and y position has changed
     * from the last frame, and sets the moving flag accordingly.
     * 
     * It also sets the facing direction of the entity based on the change in x and
     * y position.
     */
    private void updateMoving() {
        if (lx != getX() || ly != getY()) {
            this.set(moving, true);
        } else {
            this.set(moving, false);
        }

        // fix facing
        if (getX() > lx) {
            facing = Direction.EAST;
        }
        if (getX() < lx) {
            facing = Direction.WEST;
        }
        if (getY() > ly) {
            facing = Direction.NORTH;
        }
        if (getY() < ly) {
            facing = Direction.SOUTH;
        }

        lx = getX();
        ly = getY();
    }

    /**
     * 
     * Adds the specified number of items to the entity's inventory.
     * 
     * @param item the item to be added
     * @param num  the number of items to be added
     */
    public void addItems(Item item, int num) {
        items.addItems(item, num);
    }

    /**
     * 
     * Allows the entity to pick up items from another ItemStorage object.
     * 
     * @param items2 the ItemStorage object that the entity is picking items up from
     */
    public void pickupItems(ItemStorage items2) {
        items.pickupItems(items2);
    }

    /**
     * 
     * Allows the entity to use an item from its inventory.
     * 
     * @param item the item to be used
     */
    public void useItem(String item) {
        items.useItem(item);
    }

    /**
     * 
     * Returns the number of a specific item in the entity's inventory.
     * 
     * @param item the item to count
     * @return the number of the specified item in the inventory
     */
    public int countItems(String item) {
        return items.countItem(item);
    }

    // ---------------------------------------------------------
    // getters and setters
    // ---------------------------------------------------------

    /**
     * 
     * Overrides the setX method in the parent class to also update the shadow's x
     * position and call the updateMoving method.
     * 
     * @param x the new x position of the entity
     */
    @Override
    public void setX(float x) {
        super.setX(x);
        if (shadow != null) {
            shadow.setX(x);
        }
        updateMoving();
    }

    /**
     * 
     * Overrides the setY method in the parent class to also update the shadow's y
     * position and call the updateMoving method.
     * 
     * @param y the new y position of the entity
     */
    @Override
    public void setY(float y) {
        super.setY(y);
        if (shadow != null) {
            shadow.setY(y);
        }
        updateMoving();
    }

    /**
     * 
     * Overrides the set method in the parent class to also handle attackCooldown
     * and damageCooldown properties.
     * If attackCooldown or damageCooldown is set to true, a ScheduledEvent is
     * registered to set the property back to false after a certain amount of time.
     * 
     * @param property the property to set
     * @param value    the value to set the property to
     */
    @Override
    public void set(String property, boolean value) {
        super.set(property, value);

        switch (property) {
            case attackCooldown -> {
                if (is(attackCooldown)) {
                    GameScheduler.registerEvent(new ScheduledEvent(attackCooldownMS, 1) {
                        @Override
                        public void run() {
                            set(attackCooldown, false);
                        }
                    });
                }

                break;
            }

            case damageCooldown -> {
                if (is(damageCooldown)) {
                    GameScheduler.registerEvent(new ScheduledEvent(damageCooldownMS, 1) {
                        @Override
                        public void run() {
                            set(damageCooldown, false);
                        }
                    });
                }

                break;
            }
        }
    }

    /**
     * 
     * Returns the entity's current dx value, which represents its horizontal
     * velocity.
     * If the entity is in water, the dx value is divided by 2 to simulate the
     * effect of water resistance.
     * 
     * @return the entity's current dx value
     */
    public float getDx() {
        return this.dx / (is(inWater) ? 2 : 1);
    }

    /**
     * 
     * Sets the entity's dx value, which represents its horizontal velocity.
     * 
     * @param dx the new dx value for the entity
     */
    public void setDx(float dx) {
        this.dx = dx;
    }

    /**
     * 
     * Returns the entity's current dy value, which represents its vertical
     * velocity.
     * If the entity is in water, the dy value is divided by 2 to simulate the
     * effect of water resistance.
     * 
     * @return the entity's current dy value
     */
    public float getDy() {
        return this.dy / (is(inWater) ? 2 : 1);
    }

    /**
     * 
     * Sets the entity's dy value, which represents its vertical velocity.
     * 
     * @param dy the new dy value for the entity
     */
    public void setDy(float dy) {
        this.dy = dy;
    }

    /**
     * 
     * Returns the entity's current health value.
     * 
     * @return the entity's current health value
     */
    public float getHealth() {
        return this.health;
    }

    /**
     * 
     * Sets the entity's health value.
     * 
     * @param health the new health value for the entity
     */
    public void setHealth(float health) {
        this.health = health;
    }

    /**
     * 
     * Modifies the entity's health value by a certain amount.
     * 
     * @param c the amount to change the health value by
     */
    public void modHealth(float c) {
        this.health += c;
    }

    /**
     * 
     * Returns the entity's maximum health value.
     * 
     * @return the entity's maximum health value
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * 
     * Sets the entity's maximum health value.
     * 
     * @param maxHealth the new maximum health value for the entity
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * 
     * Returns the entity's attack damage value.
     * If the entity has an equipped weapon, the attack damage is equal to the
     * weapon's net damage value.
     * Otherwise, the attack damage is a default value of 0.5.
     * 
     * @return the entity's attack damage value
     */
    public float getAttackDamage() {
        if (equipped != null)
            return this.equipped.getNetDamage();
        return 0.5f;
    }

    /**
     * 
     * Sets a knockback effect for the entity.
     * 
     * @param k          the knockback strength
     * @param durationMS the duration of the knockback effect in milliseconds
     * @param hitBy      the entity that caused the knockback
     */
    public void setKnockback(float k, int durationMS, Entity hitBy) {
        this.kb = new Knockback(k, this, hitBy, durationMS);
    }

    /**
     * 
     * Returns the entity's damage resistance value.
     * 
     * @return the entity's damage resistance value
     */
    public float getDamageResistance() {
        return this.damageResistance;
    }

    /**
     * 
     * Returns the shadow associated with this entity.
     * The shadow is a Light object that is used to simulate the effect of the
     * entity casting a shadow.
     * 
     * @return Light object representing the shadow
     */
    public Light getShadow() {
        return this.shadow;
    }

    /**
     * 
     * Sets the shadow for this entity.
     * The shadow is a Light object that is used to simulate the effect of the
     * entity casting a shadow.
     * 
     * @param shadow the Light object that should be set as the shadow for this
     *               entity
     */
    public void setShadow(Light shadow) {
        shadow.setRenderLayer(RenderLayers.STRUCTURE_ENTITY_LAYER - 1);
        this.shadow = shadow;
    }

    /**
     * 
     * Sets the damage resistance of this entity.
     * Damage resistance represents the amount of damage this entity can resist
     * before taking damage.
     * 
     * @param damageResistance the value to set the damage resistance to
     */
    public void setDamageResistance(float damageResistance) {
        this.damageResistance = damageResistance;
    }

    /**
     * 
     * Gets the facing direction of this entity.
     * The facing direction determines which direction the entity is facing.
     * 
     * @return the facing direction of this entity
     */
    public Direction getFacing() {
        return this.facing;
    }

    /**
     * 
     * Sets the facing direction of this entity.
     * The facing direction determines which direction the entity is facing.
     * 
     * @param facing the direction to set the facing direction to
     */
    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    
    /** 
     * @return String
     */
    public String getCurMap() {
        return this.curMap;
    }

    
    /** 
     * @return float
     */
    public float getAttackRange() {
        if (equipped != null)
            return this.equipped.getAttackRange();
        return 0.5f;
    }

    /**
     * 
     * Gets the attack cooldown of this entity.
     * The attack cooldown is used to determine how long the entity has to wait
     * before being able to attack again.
     * 
     * @return the attack cooldown of this entity, in milliseconds
     */
    public int getAttackCooldownMS() {
        return this.attackCooldownMS;
    }

    /**
     * 
     * Sets the duration of the attack cooldown in milliseconds.
     * 
     * @param attackCooldownMS the duration of the attack cooldown in milliseconds
     */
    public void setAttackCooldownMS(int attackCooldownMS) {
        this.attackCooldownMS = attackCooldownMS;
    }

    /**
     * 
     * Returns the duration of the damage cooldown in milliseconds.
     * 
     * @return the duration of the damage cooldown in milliseconds
     */
    public int getDamageCooldownMS() {
        return this.damageCooldownMS;
    }

    /**
     * 
     * Sets the duration of the damage cooldown in milliseconds.
     * 
     * @param hitCooldownMS the duration of the damage cooldown in milliseconds
     */
    public void setDamageCooldownMS(int hitCooldownMS) {
        this.damageCooldownMS = hitCooldownMS;
    }

    /**
     * 
     * Gets the weapon that is currently equipped by this entity.
     * The equipped weapon is the weapon that this entity is currently using to
     * attack.
     * 
     * @return the weapon that is currently equipped by this entity
     */
    public WeaponItem getEquipped() {
        return this.equipped;
    }

    /**
     * 
     * Sets the weapon that is currently equipped by this entity.
     * The equipped weapon is the weapon that this entity is currently using to
     * attack.
     * 
     * @param equipped the weapon to set as the currently equipped weapon
     */
    public void setEquipped(WeaponItem equipped) {
        this.equipped = equipped;
    }

    /**
     * 
     * Gets the item storage of this entity.
     * The item storage is used to store items that this entity is carrying.
     * 
     * @return the item storage of this entity
     */
    public ItemStorage getItems() {
        return this.items;
    }

    // ----------- game tag / property keys ------------------------------

    public static final String attackCooldown = "hitCooldown";
    public static final String damageCooldown = "damageCooldown";
    public static final String destroyed = "destroyed";
    public static final String invulnerable = "invulnerable";
    public static final String fireResistant = "fireResistant";
    public static final String inWater = "inWater";
    public static final String onFire = "onFire";
    public static final String moving = "moving";
    public static final String projectileDamage = "projectileDamage";
    public static final String pickupItems = "pickupItems";

    // -------------------------------------------------------------------
}