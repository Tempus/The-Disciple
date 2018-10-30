package chronomuncher.powers;
import chronomuncher.ChronoMod;
import chronomuncher.cards.Ward;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
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

public class EchowardPower extends AbstractPower
{
  public static final String POWER_ID = "Echoward";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Echoward");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

  private boolean upgraded = false;

  public EchowardPower(AbstractCreature owner, boolean upgraded)
  {
    this.name = NAME;
    this.ID = "Echoward";
    this.owner = owner;
    this.amount = 1;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/Echoward.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/EchowardSmall.png"), 0, 0, 32, 32);
    this.type = AbstractPower.PowerType.BUFF;
    this.isTurnBased = true;
    this.upgraded = upgraded;
    this.description = DESCRIPTIONS[0];
  }
  
  @Override
  public void onPlayCard(AbstractCard card, AbstractMonster m)
  {
    if ((card.cardID == "Ward") && (card.exhaustOnFire == true)) { return; }

    for (int i = 0; i < this.amount; i++) {
      this.playWard(card);              
    }
  }
  
  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
  }

  public void playWard(AbstractCard card) {
    flash();
    AbstractCard tmp = new Ward();
    if (this.upgraded){ tmp.upgrade(); }
    AbstractDungeon.player.limbo.addToBottom(tmp);
    tmp.current_x = card.current_x;
    tmp.current_y = card.current_y;
    tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
    tmp.target_y = (Settings.HEIGHT / 2.0F);
    tmp.freeToPlayOnce = true;
    tmp.exhaustOnFire = true;
    tmp.purgeOnUse = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, true));
  }
}

// Should not echo after playing itself - OnAfterUseCard triggers after the action is executed.
// Should echo after playing Wards from hand, but not from echo'd wards.