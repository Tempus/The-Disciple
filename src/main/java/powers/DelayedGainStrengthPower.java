package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class DelayedGainStrengthPower extends AbstractPower
{
  public static final String POWER_ID = "DelayedGainStrength";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DelayedGainStrength");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  private int strength;
  private static int bombIdOffset;
  
  public DelayedGainStrengthPower(AbstractCreature owner, int turns, int strength)
  {
    this.name = NAME;
    this.ID = ("DelayedGainStrength" + bombIdOffset);
    bombIdOffset += 1;
    this.owner = owner;
    this.amount = turns;
    this.strength = strength;
    updateDescription();

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedGainStrength.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedGainStrengthSmall.png"), 0, 0, 32, 32);
  }
  
  public void atEndOfTurn(boolean isPlayer)
  {
    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
      if (this.amount <= 1) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.strength), this.strength));
      }
    }
  }
  
  public void updateDescription()
  {
    if (this.amount == 1) {
      this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.strength) });
    } else {
      this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount), Integer.valueOf(this.strength) });
    }
  }
}
