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
  
  public RetainOncePower(int amount)
  {
    this.name = NAME;
    this.ID = ("RetainOnce");
    this.owner = AbstractDungeon.player;
    this.amount = amount;
    this.region128 = new TextureAtlas.AtlasRegion(new Texture("images/powers/RetainOnce.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(new Texture("images/powers/RetainOnceSmall.png"), 0, 0, 32, 32);
    updateDescription();
  }

  @Override
  public void atStartOfTurn() 
  {
    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
  }

  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    if ((isPlayer) && (!AbstractDungeon.player.hand.isEmpty()) && (!AbstractDungeon.player.hasRelic("Runic Pyramid")) && 
      (!AbstractDungeon.player.hasPower("Equilibrium"))) {
      AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(this.owner, this.amount));
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
