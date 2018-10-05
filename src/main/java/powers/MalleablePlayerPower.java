package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;

public class MalleablePlayerPower
  extends AbstractPower
{
  public static final String POWER_ID = "MalleablePlayer";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Malleable");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  private static final int STARTING_BLOCK = 3;

  public static int baseAmount = 0;
  
  public MalleablePlayerPower(AbstractCreature owner, int amount)
  {
    this.name = NAME;
    this.ID = "Malleable";
    this.owner = owner;
    this.baseAmount = amount;
    this.amount = amount;
    
    updateDescription();
    this.img = ImageMaster.loadImage("chrono_images/powers/Malleable.png");
  }
  
  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + NAME + DESCRIPTIONS[2] + 3 + DESCRIPTIONS[3]);
  }
  
  public void atEndOfTurn(boolean isPlayer)
  {
    this.amount = this.baseAmount;
    updateDescription();
  }
  
  public int onAttacked(DamageInfo info, int damageAmount)
  {
    if ((damageAmount < this.owner.currentHealth) && (damageAmount > 0) && (info.owner != null) && (info.type == DamageInfo.DamageType.NORMAL) && (info.type != DamageInfo.DamageType.HP_LOSS))
    {
      flash();
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, this.amount));
      this.amount += 1;
      updateDescription();
    }
    return damageAmount;
  }
}
