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
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import java.util.ArrayList;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class UpgradeCardsInHandAction
  extends AbstractGameAction
{
  private AbstractPlayer p;
  private ArrayList<AbstractCard> cannotUpgrade = new ArrayList();
  private ArrayList<AbstractCard> canUpgrade = new ArrayList();
  private boolean upgraded = false;
  public AbstractCard.CardType cardType;

  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("UpgradeCardsInHandAction");
  public static final String[] TEXT = uiStrings.TEXT;

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
      // Separate upgrade candidates from non-candidates
      for (AbstractCard c : this.p.hand.group) {
        if (c.canUpgrade() && c.type == this.cardType) {
          this.canUpgrade.add(c);
        } 
        else {
          this.cannotUpgrade.add(c);
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
        for (AbstractCard c : this.canUpgrade) {
          c.upgrade();
          this.isDone = true;
          return;
        }
      }
      // Choose a card from your hand
      else {
        AbstractDungeon.player.hand.group.removeAll(this.cannotUpgrade);
        AbstractDungeon.handCardSelectScreen.open(TEXT[0] + TEXT[1], 1, false, false, false, true);
        tickDuration();
        return;
      }
    }


    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
    {
      for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
      {
        c.upgrade();
        c.superFlash();
        AbstractDungeon.player.hand.addToTop(c);
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
      AbstractDungeon.player.hand.addToTop(c);
    }
    AbstractDungeon.player.hand.refreshHandLayout();
  }
}
