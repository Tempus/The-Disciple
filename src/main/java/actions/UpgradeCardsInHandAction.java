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
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import java.util.ArrayList;

public class UpgradeCardsInHandAction
  extends AbstractGameAction
{
  private AbstractPlayer p;
  private ArrayList<AbstractCard> cannotUpgrade = new ArrayList();
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
      for (AbstractCard c : this.p.hand.getCardsOfType(this.cardType).group) {
        if (!c.canUpgrade()) {
          this.cannotUpgrade.add(c);
        }
      }
      if (this.cannotUpgrade.size() == this.p.hand.getCardsOfType(this.cardType).group.size())
      {
        this.isDone = true;
        return;
      }
      if (this.p.hand.getCardsOfType(this.cardType).group.size() - this.cannotUpgrade.size() == 1) {
        for (AbstractCard c : this.p.hand.getCardsOfType(this.cardType).group) {
          if (c.canUpgrade())
          {
            c.upgrade();
            this.isDone = true;
            return;
          }
        }
      }
      this.p.hand.getCardsOfType(this.cardType).group.removeAll(this.cannotUpgrade);
      if (this.p.hand.getCardsOfType(this.cardType).group.size() > 1)
      {
        AbstractDungeon.handCardSelectScreen.open("Upgrade", 1, false, false, false, true);
        tickDuration();
        return;
      }
      if (this.p.hand.getCardsOfType(this.cardType).group.size() == 1)
      {
        this.p.hand.getCardsOfType(this.cardType).getTopCard().upgrade();
        returnCards();
        this.isDone = true;
      }
    }
    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
    {
      for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
      {
        c.upgrade();
        this.p.hand.addToTop(c);
      }
      returnCards();
      AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
      AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
      this.isDone = true;
    }
    tickDuration();
  }
  
  private void returnCards()
  {
    for (AbstractCard c : this.cannotUpgrade) {
      this.p.hand.addToTop(c);
    }
    this.p.hand.refreshHandLayout();
  }
}
