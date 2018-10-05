package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import chronomuncher.ChronoMod;
import chronomuncher.actions.PlayEchoCardAction;
import chronomuncher.cards.ResonantCall;

public class EchonomicsPower
  extends AbstractPower
{
  public static final String POWER_ID = "Echonomics";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Echonomics");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public boolean upgraded = false;
  
  public EchonomicsPower(int amount, boolean upgraded)
  {
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = AbstractDungeon.player;
    this.amount = amount;
    this.upgraded = upgraded;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/EchoLate.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/EchoLateSmall.png"), 0, 0, 32, 32);
  }
  
  public void updateDescription()
  {
    if (this.amount == 1) {
      this.description = DESCRIPTIONS[0];
    } else {
      this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
  }

  public void atEndOfTurn(boolean isPlayer)
  {
    if (isPlayer) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "Echonomics"));
    }
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.cardID == "Echonomics") {
      AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "#yEchonomics can't duplicate itself.", true));
      return;
    }

    if (card instanceof ResonantCall) {
      if (((ResonantCall)card).mimic.cardID == "Echonomics") {
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "#yEchonomics can't duplicate itself.", true));
        return;
      }
    }

    AbstractCard c = card.makeStatEquivalentCopy();
    if (!this.upgraded) {
      c.modifyCostForTurn(-9);
    } else {
      c.modifyCostForCombat(-9);
    }
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));

    this.amount -= 1;
    if (this.amount == 0) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "Echonomics"));
    }
  }
}
