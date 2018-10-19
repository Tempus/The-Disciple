package chronomuncher.character;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import basemod.abstracts.CustomPlayer;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.relics.Metronome;
import chronomuncher.orbs.ReplicaOrb;
import chronomuncher.vfx.GoldEnergyFlameEffect;
import chronomuncher.cards.*;
import chronomuncher.character.EnergyOrbGold;

public class Chronomuncher extends CustomPlayer {

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 65;
    public static final int MAX_HP = 65;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;
    public static final int ORB_SLOTS = 0;

    public static final String DISCIPLE_SHOULDER_2 = "chrono_images/char/shoulder2.png"; // campfire pose
    public static final String DISCIPLE_SHOULDER_1 = "chrono_images/char/shoulder.png"; // another campfire pose
    public static final String DISCIPLE_CORPSE = "chrono_images/char/corpse.png"; // dead corpse
    public static final String DISCIPLE_SKELETON_ATLAS = "chrono_images/char/skeleton.atlas"; // spine animation atlas
    public static final String DISCIPLE_SKELETON_JSON = "chrono_images/char/skeleton.json"; // spine animation json

    private double counter = 0.0f;
    private Texture tex = new Texture("chrono_images/char/temp.png");

    public static final String DEFAULT_ORB_VFX = "chrono_images/char/orb/neworb/vfx.png";

    public Chronomuncher(String name) {
        super(name, Enum.CHRONO_CLASS, null, "chrono_images/char/orb/neworb/vfx.png", (String)null, (String)null);

        ChronoMod.log("Creating our character?");

        initializeClass(null, DISCIPLE_SHOULDER_2, DISCIPLE_SHOULDER_1, DISCIPLE_CORPSE,
            getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        this.atlas = new TextureAtlas();
        tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.energyOrb = new EnergyOrbGold();

        // loadAnimation(DISCIPLE_SKELETON_ATLAS, DISCIPLE_SKELETON_JSON, 1.0F); 
 
        // AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        // e.setTime(e.getEndTime() * MathUtils.random());
    }

    // Begin week 45 compliance
    @Override
    public String getTitle(PlayerClass playerClass) { return this.getLocalizedCharacterName(); }

    @Override
    public AbstractCard.CardColor getCardColor() { return Enum.CHRONO_GOLD; }

    @Override
    public Color getCardTrailColor() { return ChronoMod.CHRONO_GOLD; }

    @Override
    public Color getSlashAttackColor() { return ChronoMod.CHRONO_GOLD; }

    @Override
    public Color getCardRenderColor() { return ChronoMod.CHRONO_GOLD; }

    @Override
    public String getVampireText() { return 
        com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[0];
    }

    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect()
    {
        return new AbstractGameAction.AttackEffect[] { AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.BLUNT_HEAVY };
    }
    public String getSpireHeartText()
    {
        return "It seems like your time has run out... once again.";
    }

    public TextureAtlas.AtlasRegion getOrb()
    {
        return new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/cardui/description_bronze_orb.png"), 0, 0, 24, 24);
    }
  
    @Override
    public AbstractCard getStartCardForEvent() { return new SecondHand(); }

    @Override
    public int getAscensionMaxHPLoss() { return 3; }

    @Override
    public BitmapFont getEnergyNumFont() { return FontHelper.energyNumFontBlue; }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("SLIME_ATTACK_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() { return "SLIME_ATTACK_2"; }

    @Override
    public String getLocalizedCharacterName() { return "The Disciple"; }

    @Override
    public AbstractPlayer newInstance() { 
        return new Chronomuncher(this.name);
    }
    // End week 45 compliance

    @Override
    protected void initializeStarterDeck() {
        if (ModHelper.isModEnabled("Insanity"))
        {
          for (int i = 0; i < 50; i++) {

            ArrayList<String> tmp = new ArrayList();
          
            for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
              if (((AbstractCard)c.getValue()).color == Enum.CHRONO_GOLD) {
                tmp.add(c.getKey());
              }
            }

            this.masterDeck.addToTop(
              (AbstractCard)CardLibrary.cards.get(tmp.get(AbstractDungeon.cardRandomRng.random(0, tmp.size() - 1))).makeCopy());
          }
        }
        else if (ModHelper.isModEnabled("Shiny"))
        {
          CardGroup everyRareCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
          
          for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            if ((((AbstractCard)c.getValue()).color == Enum.CHRONO_GOLD) && (((AbstractCard)c.getValue()).rarity == AbstractCard.CardRarity.RARE)) {
              everyRareCard.addToBottom(((AbstractCard)c.getValue()).makeCopy());
            }
          }

          for (AbstractCard c : everyRareCard.group)
          {
            this.masterDeck.addToTop(c);
            UnlockTracker.markCardAsSeen(c.cardID);
          }
        }
        else if (ModHelper.isModEnabled("Draft") || ModHelper.isModEnabled("SealedDeck")) {
            return;
        }
        else if (ModHelper.isModEnabled("Chimera"))
        {
          this.masterDeck.addToTop(new Bash());
          this.masterDeck.addToTop(new Survivor());
          this.masterDeck.addToTop(new Zap());
          this.masterDeck.addToTop(new PatternShift());
          this.masterDeck.addToTop(new Strike_Red());
          this.masterDeck.addToTop(new Strike_Green());
          this.masterDeck.addToTop(new Strike_Blue());
          this.masterDeck.addToTop(new Strike_Bronze());
          this.masterDeck.addToTop(new Defend_Red());
          this.masterDeck.addToTop(new Defend_Green());
          this.masterDeck.addToTop(new Defend_Blue());
          this.masterDeck.addToTop(new Defend_Bronze());
        }
        else {
          for (String s : this.getStartingDeck()) {
            this.masterDeck.addToTop(CardLibrary.getCard(Enum.CHRONO_CLASS, s).makeCopy());
          }
        }
    }

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
    }

    @Override
    public void updateOrb(int energy) {
        this.energyOrb.updateOrb(energy);
    }

    public ArrayList<String> getStartingDeck() {
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

    public ArrayList<String> getStartingRelics() { // starting relics - also simple
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Chronometer");
        UnlockTracker.markRelicAsSeen("Chronometer");
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo("The Disciple", 
            "This character is finally ready to play. It's about time.",
            STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE,
            this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);

        c.retain = false;
    }
}