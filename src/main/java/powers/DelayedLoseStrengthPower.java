package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class DelayedLoseStrengthPower extends AbstractPower
{
  public static final String POWER_ID = "DelayedLoseStrength";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DelayedLoseStrength");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  
  public DelayedLoseStrengthPower(AbstractCreature owner, int strength)
  {
    this.name = NAME;
    this.ID = ("DelayedLoseStrength");
    this.owner = owner;
    this.amount = strength;
    updateDescription();
    this.type = AbstractPower.PowerType.DEBUFF;

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedLoseStrength.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedLoseStrengthSmall.png"), 0, 0, 32, 32);
  }
  
  public void atEndOfTurn(boolean isPlayer)
  {
    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      flash();
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
    }
  }
  
  public void updateDescription()
  {
    this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.amount) });
  }
}
