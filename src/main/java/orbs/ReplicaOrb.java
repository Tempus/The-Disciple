package chronomuncher.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect.OrbFlareColor;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.FontHelper;
import java.util.ArrayList;
import java.lang.Math;

import chronomuncher.ChronoMod;
import chronomuncher.actions.ShatterAction;
import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;


public abstract class ReplicaOrb extends AbstractOrb {
  private float vfxTimer = 1.0F;
  private float vfxIntervalMin = 0.2F;
  private float vfxIntervalMax = 0.7F;
  private static final float ORB_WAVY_DIST = 0.04F;
  private static final float PI_4 = 12.566371F;

  private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString("ReplicaOrb");
  public static final String DESCRIPTION[] = orbString.DESCRIPTION;

  public AbstractCard lockedCard;
  protected boolean showChannelValue = true;

  public int baseTimer = 0;
  public int timer = 0;
  public int timeElapsed = 0;
  public boolean upgraded = false;
  public boolean showPassive = true;
  public String originalRelic = "";
  public boolean shattering = false;
 
  public String[] descriptions; 
 
  public ReplicaOrb(String ID, boolean upgraded, int passive, int passiveUp, int timer, int timerUp, AbstractCard locked, String originalRelic) {
    this.ID = ID;
    this.img = ImageMaster.loadImage("chrono_images/orbs/" + ID + ".png");
    this.upgraded = upgraded;

    this.baseTimer = timer;
    if (upgraded) { this.baseTimer = timerUp; }
    this.timer = this.baseTimer;
    this.timeElapsed = 0;

    this.basePassiveAmount = passive;
    if (this.upgraded) { this.basePassiveAmount = passiveUp; }
    this.passiveAmount = this.basePassiveAmount;

    this.lockedCard = locked;

    this.channelAnimTimer = 0.5F;
    this.showPassive = showPassive;

    this.originalRelic = originalRelic;

    this.descriptions = CardCrawlGame.languagePack.getOrbString(this.ID).DESCRIPTION;
    this.name = CardCrawlGame.languagePack.getOrbString(this.ID).NAME;
    if (this.upgraded) { this.name = this.name + "+"; }

    updateDescription();
  }

  public void onStartOfTurn()
  {
    this.decrementTimer();
  }

  public void decrementTimer() {
    this.timer--;
    this.timeElapsed++;
    updateDescription();
    if (this.timer <= 0 && this.shattering == false) {
      this.shattering = true;
      AbstractDungeon.actionManager.addToBottom(new ShatterAction(this));
    }
  }

  public void onCardUse(AbstractCard c) {}

  public void onCardDraw(AbstractCard c) {}

  public void onShatter() {}

  public void atTurnStartPostDraw() {}

  public void onVictory() {}

  @Override
  public void onEvoke()
  {
    if (this.lockedCard == null) { return; }
    AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(lockedCard, 1));
  }

  @Override
  public void triggerEvokeAnimation()
  {
    CardCrawlGame.sound.play("DUNGEON_TRANSITION", 0.1F);
    AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.cX, this.cY));
  }

  @Override
  public void updateDescription()
  {
    applyFocus();

    String turn = DESCRIPTION[0];
    if (this.timer == 1) { turn = DESCRIPTION[1]; }

    if (!this.upgraded) {
      this.description = (this.descriptions[0] + DESCRIPTION[2] + this.timer + turn);
    } else {
      this.description = (this.descriptions[1] + DESCRIPTION[2] + this.timer + turn);
    }
  }
    
  @Override
  public void applyFocus()
  {
    AbstractPower power = AbstractDungeon.player.getPower("Focus");
    if (power == null) { return; }

    this.timer = Math.max(0, this.baseTimer + power.amount - this.timeElapsed);
  }

  public void activateEffect() {
    float speedTime = 0.6F / AbstractDungeon.player.orbs.size();
    if (Settings.FAST_MODE) {
      speedTime = 0.0F;
    }
    AbstractDungeon.effectList.add(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA));    
    // AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA), speedTime));
  }

  @Override
  public void playChannelSFX()
  {
    CardCrawlGame.sound.play("AUTOMATON_ORB_SPAWN", 0.1F);
  }

  @Override
  public void render(SpriteBatch sb)
  {
    sb.setColor(this.c);
    sb.draw(img, this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
    
    renderText(sb);
    this.hb.render(sb);
  }
  
  @Override
  public void updateAnimation()
  {
    super.updateAnimation();
    this.angle += Gdx.graphics.getDeltaTime() * 45.0F;
    
    this.vfxTimer -= Gdx.graphics.getDeltaTime();
    if (this.vfxTimer < 0.0F)
    {
      AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(this.cX, this.cY));
      AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(this.cX, this.cY));
      this.vfxTimer = MathUtils.random(this.vfxIntervalMin, this.vfxIntervalMax);
    }
  }

  @Override
  protected void renderText(SpriteBatch sb)
  {
    if (this.showChannelValue && !this.showEvokeValue && this.showPassive) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, 
          Integer.toString(this.timer), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.PURPLE.cpy(), this.fontScale);
    }

    // Render an upgraded green + on the other side, for upgraded replicas only.
    if (this.upgraded) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, 
          "+", this.cX - NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET, Color.GREEN.cpy(), this.fontScale);
    }
  }
}
