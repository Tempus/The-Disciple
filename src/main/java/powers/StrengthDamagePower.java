package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;
import java.lang.Math;

public class StrengthDamagePower extends AbstractPower
{
  public static final String POWER_ID = "StrengthDamage";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DelayedAttack");
  public static final String NAME = "Strength Damage";
  // public static final String[] DESCRIPTIONS = "Does damage based on power stuff.";
  
  public StrengthDamagePower(AbstractCreature owner)
  {
    this.name = NAME;
    this.ID = "POWER_ID";
    this.owner = owner;
    this.type = AbstractPower.PowerType.BUFF;
    updateDescription();

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedAttack.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedAttackSmall.png"), 0, 0, 32, 32);
  }

  public void atEndOfRound()
  {
    AbstractPower power = this.owner.getPower("Strength");
    if (power == null) { return; }
    int pwr = power.amount;
    if (pwr >= 0) { return; }

    pwr = Math.abs(pwr);

    if (!this.owner.isDead)
    {
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(this.owner.hb.cX, this.owner.hb.cY - 40.0F * Settings.scale, Color.RED.cpy()), 0.3F));
      AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(null, pwr, DamageInfo.DamageType.THORNS), AttackEffect.NONE));

      AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY - 40.0F * Settings.scale, Color.RED.cpy()), 0.3F));
      AbstractDungeon.actionManager.addToBottom(
        new DamageAction(AbstractDungeon.player, new DamageInfo(null, pwr, DamageInfo.DamageType.THORNS), AttackEffect.NONE));
    }
  }

  public void updateDescription()
  {
    this.description = String.format("Will do damage equal to negative strength to both enemy and player at the end of the round.");
  }
}
