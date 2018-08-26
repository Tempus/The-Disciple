package chronomuncher.character;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.DailyMods;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.random.Random;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationState.TrackEntry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;

import com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect;
import com.megacrit.cardcrawl.vfx.GhostlyFireEffect;
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;
import com.megacrit.cardcrawl.vfx.StaffFireEffect;
import com.megacrit.cardcrawl.vfx.TorchHeadFireEffect;

import basemod.abstracts.CustomPlayer;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.orbs.ReplicaOrb;
import chronomuncher.vfx.GoldEnergyFlameEffect;

public class Chronomuncher extends CustomPlayer {

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 65;
    public static final int MAX_HP = 65;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;
    public static final int ORB_SLOTS = 0;

    public static final String DISCIPLE_SHOULDER_2 = "images/char/shoulder2.png"; // campfire pose
    public static final String DISCIPLE_SHOULDER_1 = "images/char/shoulder.png"; // another campfire pose
    public static final String DISCIPLE_CORPSE = "images/char/corpse.png"; // dead corpse
    public static final String DISCIPLE_SKELETON_ATLAS = "images/char/skeleton.atlas"; // spine animation atlas
    public static final String DISCIPLE_SKELETON_JSON = "images/char/skeleton.json"; // spine animation json

    public float angle1 = 0F;
    public float angle2 = 0F;
    public float angle3 = 0F;
    public float angle4 = 0F;
    public float angle5 = 0F;
    public float angle6 = 0F;
    public float angle7 = 0F;

    private float w = 0F;
    private float h = 0F;
    private final float orbScale = 1.15F * Settings.scale;

    private double counter = 0.0f;
    private Texture tex = new Texture("images/char/temp.png");

    private ArrayList<LogoFlameEffect> flame = new ArrayList();
    private float flameTimer = 0.2F;
    private static final float FLAME_INTERVAL = 0.05F;

    public static final ArrayList<Texture> energyActiveLayers = new ArrayList<Texture>();
    public static final ArrayList<Texture> energyDisabledLayers = new ArrayList<Texture>();

    public static final String DEFAULT_ORB_VFX = "images/char/orb/neworb/vfx.png";

    public Chronomuncher(String name, PlayerClass setClass) {
        super(name, setClass, new String[0], "images/char/orb/neworb/vfx.png", (String)null, (String)null);

        ChronoMod.log("Creating our character?");

        initializeClass(null, DISCIPLE_SHOULDER_2, DISCIPLE_SHOULDER_1, DISCIPLE_CORPSE,
            getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        this.atlas = new TextureAtlas();
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.loadEnergyOrbs();

        // loadAnimation(DISCIPLE_SKELETON_ATLAS, DISCIPLE_SKELETON_JSON, 1.0F); 
 
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    protected void initializeStarterDeck() {

        if (((Boolean)DailyMods.cardMods.get("Insanity")).booleanValue())
        {
          for (int i = 0; i < 50; i++) {

            ArrayList<String> tmp = new ArrayList();
          
            for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
              if (((AbstractCard)c.getValue()).color == Enum.BRONZE) {
                tmp.add(c.getKey());
              }
            }

            this.masterDeck.addToTop(
              (AbstractCard)CardLibrary.cards.get(tmp.get(AbstractDungeon.cardRandomRng.random(0, tmp.size() - 1))).makeCopy());
          }
        }

        else if (((Boolean)DailyMods.cardMods.get("Draft")).booleanValue()) {
          // We do nothing, honest.
        }

        else if (((Boolean)DailyMods.cardMods.get("Shiny")).booleanValue())
        {
          CardGroup everyRareCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
          
          for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if ((((AbstractCard)c.getValue()).color == Enum.BRONZE) && (((AbstractCard)c.getValue()).rarity == AbstractCard.CardRarity.RARE)) {
              everyRareCard.addToBottom(((AbstractCard)c.getValue()).makeCopy());
            }
          }

          for (AbstractCard c : everyRareCard.group)
          {
            this.masterDeck.addToTop(c);
            UnlockTracker.markCardAsSeen(c.cardID);
          }
        }

        else {
          for (String s : this.getStartingDeck()) {
            this.masterDeck.addToTop(CardLibrary.getCard(Enum.CHRONO_CLASS, s).makeCopy());
          }
        }
    }


    public void loadEnergyOrbs() {
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer1.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer2.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer3.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer4.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer5.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer6.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer7.png"));
        this.energyActiveLayers.add(new Texture("images/char/orb/neworb/active/layer8.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer1.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer2.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer3.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer4.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer5.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer6.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer7.png"));
        this.energyDisabledLayers.add(new Texture("images/char/orb/neworb/disabled/layer8.png"));

        for (Texture tex : this.energyActiveLayers) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        for (Texture tex : this.energyDisabledLayers) {
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        // try {
        //     Field tmp;

        //     tmp = this.getClass().getDeclaredField("orbVfx");
        //     tmp.setAccessible(true);
            
        //     tmp.set(this, new Texture("images/char/orb/neworb/vfx.png"));

        // } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
        //     e.printStackTrace();
        // }
    }

    // public Texture getOrbVfxTexture() {
    //     return new Texture("images/char/orb/neworb/vfx.png");
    // }


    @SuppressWarnings("unused")
    public void renderPlayerImage(SpriteBatch sb) {
        if(!(AbstractDungeon.player instanceof Chronomuncher))
            return;
        
        sb.setColor(1, 1, 1, 1);
                
        counter += Gdx.graphics.getDeltaTime();
        
        if(counter > 100.0 && Math.sin(counter) > 0.9999) {
            counter = Math.PI / 2;
        }
        
        float scale = 1.0f * (float)Settings.scale + (float)(0.01f*Math.sin(counter)); 

        // sb.draw(tex, 256, 310, 0, 0, 512f, 512f, scale, scale * 1.05f, 0.0f, 0, 0, 512, 512, false, false);

        sb.draw(tex,

            this.drawX - tex.getWidth() * Settings.scale / 2.0F + this.animX,  // x
            this.drawY,                                                             // y

            tex.getWidth() * scale,                                   // 
            tex.getHeight() * scale * 1.05f, 

            0, 0, 

            tex.getWidth(), 
            tex.getHeight(), 

            this.flipHorizontal, this.flipVertical);
    }

    @Override
    public void update() {
        super.update();
        // this.updateFlame();

        if (EnergyPanel.totalCount > 0){
            this.angle7 += Gdx.graphics.getDeltaTime() * 40.0F;
            this.angle6 += Gdx.graphics.getDeltaTime() * 20.0F;
            this.angle5 += Gdx.graphics.getDeltaTime() * -10.0F;
            this.angle4 += Gdx.graphics.getDeltaTime() * -20.0F;
            this.angle3 += Gdx.graphics.getDeltaTime() * -30.0F;
            this.angle2 += Gdx.graphics.getDeltaTime() * -80.0F;
            this.angle1 += Gdx.graphics.getDeltaTime() * -160.0F;
        } else {
            this.angle7 += Gdx.graphics.getDeltaTime() * 10.0F;
            this.angle6 += Gdx.graphics.getDeltaTime() * 5.0F;
            this.angle5 += Gdx.graphics.getDeltaTime() * -2.5F;
            this.angle4 += Gdx.graphics.getDeltaTime() * -5.0F;
            this.angle3 += Gdx.graphics.getDeltaTime() * -7.5F;
            this.angle2 += Gdx.graphics.getDeltaTime() * -10.0F;
            this.angle1 += Gdx.graphics.getDeltaTime() * -20.0F;
        }
    }

    private void updateFlame()
    {
      this.flameTimer -= Gdx.graphics.getDeltaTime();
      if (this.flameTimer < 0.0F)
      {
        this.flameTimer = 0.05F;
        AbstractDungeon.effectList.add(new GoldEnergyFlameEffect(198.0F, 190.0F, 1.0F, Color.GOLDENROD.cpy())); // this duration
      }
    }

    public static ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Strike_Br");
        retVal.add("Strike_Br");
        retVal.add("Strike_Br");
        retVal.add("Strike_Br");
        retVal.add("Defend_Br");
        retVal.add("Defend_Br");
        retVal.add("Defend_Br");
        retVal.add("Defend_Br");
        retVal.add("SecondHand");
        retVal.add("PatternShift");
        return retVal;
    }

    public static ArrayList<String> getStartingRelics() { // starting relics - also simple
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Chronometer");
        UnlockTracker.markRelicAsSeen("Chronometer");
        return retVal;
    }

    public static CharSelectInfo getLoadout() {
        return new CharSelectInfo("The Disciple", 
            "This character is finally ready to play. It's about time.",
            STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE,
            Enum.CHRONO_CLASS, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void draw(int numCards) {
        super.draw(numCards);

        // Orb callback
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof ReplicaOrb) {
                ReplicaOrb u = (ReplicaOrb)o;
                u.onCardDraw();
            }
        }    
    }

    @Override
    public void applyStartOfTurnPostDrawRelics() {
        super.applyStartOfTurnPostDrawRelics();

        // Orb callback
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof ReplicaOrb) {
                ReplicaOrb u = (ReplicaOrb)o;
                u.atTurnStartPostDraw();
            }
        }    
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);

        c.retain = false;
    }

    public void saneDraw(EnergyPanel panel, SpriteBatch sb, Texture tex, float xOffset, float yOffset, float rotation) {
        this.w = tex.getWidth();
        this.h = tex.getHeight();

        sb.draw(tex, panel.current_x - (this.w/2.0F) + xOffset, panel.current_y - (this.h/2.0F) + yOffset, this.w/2.0F, this.h/2.0F, this.w, this.h, this.orbScale, this.orbScale, rotation, 0, 0, (int)this.w, (int)this.h, false, false);
    }

    public void renderOrb(EnergyPanel panel, SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        
        this.saneDraw(panel, sb, this.energyActiveLayers.get(0), 0F, 0F, this.angle4);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(1), -3.0F, -4.0F, this.angle3);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(2), 0F, -2.0F, this.angle5);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(3), 0F, -2.0F, this.angle1);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(4), -10.0F, 0F, this.angle6);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(5), 0F, -2.0F, this.angle2);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(6), 0F, 0F, 0);
        this.saneDraw(panel, sb, this.energyActiveLayers.get(7), 0F, 0F, 0);
    }

    public void renderDisabledOrb(EnergyPanel panel, SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(0), 0F, 0F, this.angle4);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(1), -3.0F, -4.0F, this.angle3);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(2), 0F, -2.0F, this.angle5);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(3), 0F, -2.0F, this.angle1);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(4), -10.0F, 0F, this.angle6);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(5), 0F, -2.0F, this.angle2);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(6), 0F, 0F, 0);
        this.saneDraw(panel, sb, this.energyDisabledLayers.get(7), 0F, 0F, 0);
    }
}