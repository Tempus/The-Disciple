package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class RetainOncePower extends AbstractPower
{
  public static final String POWER_ID = "RetainOnce";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("RetainOnce");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  public int permaRetain = 0;
  
  public RetainOncePower(int amount)
  {
    this.name = NAME;
    this.ID = ("RetainOnce");
    this.owner = AbstractDungeon.player;
    this.amount = amount;
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/RetainOnce.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/RetainOnceSmall.png"), 0, 0, 32, 32);
    updateDescription();
    this.permaRetain = permaRetain;
    this.priority = 4;
  }

  @Override
  public void atStartOfTurn() 
  {
    if (this.owner.hasPower("Retain Cards")) {
      this.owner.getPower("Retain Cards").amount -= this.permaRetain;
    }
    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
  }

  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    if (this.owner.hasPower("Retain Cards")) {
      this.permaRetain = this.amount;
      this.owner.getPower("Retain Cards").amount += this.amount;
      this.amount = 0;
    }
    else if ((!AbstractDungeon.player.hand.isEmpty())) {
      AbstractDungeon.actionManager.addToTop(new RetainCardsAction(this.owner, this.amount));
    }
  }

  @Override
  public void updateDescription()
  {
    if (this.amount == 1) {
      this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    } else {
      this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2]);
    }
  }
}
