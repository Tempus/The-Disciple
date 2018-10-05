package chronomuncher.powers;
import chronomuncher.ChronoMod;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.lang.Math;

public class HastePower extends AbstractPower
{
  public static final String POWER_ID = "Haste";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Haste");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  
  public HastePower(AbstractCreature owner, int amount)
  {
    this.name = NAME;
    this.ID = "Haste";
    this.owner = owner;
    this.amount = amount;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/Haste.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/HasteSmall.png"), 0, 0, 32, 32);
    this.type = AbstractPower.PowerType.BUFF;
  }
  
  @Override
  public void atEndOfRound()
  {
    this.amount = 0;
    updateDescription();
  }
  
  @Override
  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + FontHelper.colorString(this.owner.name, "y") + DESCRIPTIONS[1]);
    if (this.amount != 0) {
      this.description = (this.description + DESCRIPTIONS[2] + this.amount * 10 + DESCRIPTIONS[3]);
    }
  }
  
  @Override
  public void onAfterUseCard(AbstractCard card, UseCardAction action)
  {
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new HastePower(this.owner, 1), 1));
  }
  
  @Override
  public float modifyBlock(float blockAmount)
  {
    return (blockAmount * (1.0F + this.amount * 0.1F));
  }
}
