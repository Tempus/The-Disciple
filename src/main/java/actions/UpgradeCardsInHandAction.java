package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import java.util.ArrayList;

public class UpgradeCardsInHandAction
  extends AbstractGameAction
{
  private AbstractPlayer p;
  private ArrayList<AbstractCard> cannotUpgrade = new ArrayList();
  private CardGroup canUpgrade = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
  private boolean upgraded = false;
  public AbstractCard.CardType cardType;

  public UpgradeCardsInHandAction(AbstractCard.CardType cardType, boolean entireHand)
  {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.p = AbstractDungeon.player;
    this.duration = Settings.ACTION_DUR_FAST;
    this.upgraded = entireHand;
    this.cardType = cardType;
  }
  
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_FAST)
    {
      // Upgraded Logic
      if (this.upgraded)
      {
        for (AbstractCard c : this.p.hand.getCardsOfType(this.cardType).group) {
          if (c.canUpgrade())
          {
            c.upgrade();
            c.superFlash();
          }
        }
        this.isDone = true;
        return;
      }

      // Unupgraded Logic
      // Find all upgradeable cards
      for (AbstractCard c : this.p.hand.group) {
        if (c.canUpgrade() && c.type == this.cardType) {
          this.canUpgrade.addToBottom(c);
        } 
      }
      // No cards,so just go back
      if (this.canUpgrade.size() == 0)
      {
        this.isDone = true;
        return;
      }
      // Only one card, so no need to choose.
      else if (this.canUpgrade.size() == 1) {
        for (AbstractCard c : this.canUpgrade.group) {
          c.upgrade();
          this.isDone = true;
          return;
        }
      }
      // Choose a card from your hand
      else
      {
        for (AbstractCard c : this.p.hand.group) {
          c.setAngle(0);

          // Reset attributes without reseting cost. Reseting cost would also make sense, but then it doesn't change back when going into your hand again.
          c.block = c.baseBlock;
          c.isBlockModified = false;
          c.damage = c.baseDamage;
          c.isDamageModified = false;
          c.magicNumber = c.baseMagicNumber;
          c.isMagicNumberModified = false;
        }
        AbstractDungeon.gridSelectScreen.open(this.canUpgrade, 1, "Upgrade", true);
        tickDuration();
        return;
      }
    }

    if (AbstractDungeon.gridSelectScreen.upgradePreviewCard != null) {
      AbstractDungeon.gridSelectScreen.upgradePreviewCard.applyPowers();
    }
    // Return if we've selected some cards
    if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
    {
      for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
      {
        c.upgrade();
      }
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      this.p.hand.refreshHandLayout();
      this.isDone = true;
    }
    tickDuration();
  }
}
