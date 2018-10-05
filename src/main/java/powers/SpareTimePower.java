package chronomuncher.powers;
import chronomuncher.ChronoMod;
import chronomuncher.cards.Ward;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.Settings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.lang.Math;

public class SpareTimePower extends AbstractPower
{
  public static final String POWER_ID = "SpareTime";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("SpareTime");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  
  private boolean upgraded = false;

  public SpareTimePower(AbstractCreature owner, boolean upgraded)
  {
    this.name = NAME;
    this.ID = "SpareTime";
    this.owner = owner;
    this.amount = 1;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/SpareTime.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/SpareTimeSmall.png"), 0, 0, 32, 32);
    this.type = AbstractPower.PowerType.BUFF;
    this.isTurnBased = true;
    this.upgraded = upgraded;
    this.description = DESCRIPTIONS[0];
  }
  
  @Override
  public void onAfterUseCard(AbstractCard card, UseCardAction action)
  {
    for (int i=0; i < card.costForTurn; i++) {
      for (int j = 0; j < this.amount; j++) {
        this.playWard(card, i+j);              
      }
    }
  }
  
  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
  }

  public void playWard(AbstractCard card, int offset) {
    flash();
    AbstractCard tmp = new Ward();
    if (this.upgraded){ tmp.upgrade(); }
    AbstractDungeon.player.limbo.addToBottom(tmp);
    tmp.current_x = card.current_x;
    tmp.current_y = card.current_y;
    tmp.target_x = (Settings.WIDTH / 2.0F - 150.0F * (offset+1) * Settings.scale);
    tmp.target_y = (Settings.HEIGHT / 2.0F);
    tmp.freeToPlayOnce = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, true));
  }
}