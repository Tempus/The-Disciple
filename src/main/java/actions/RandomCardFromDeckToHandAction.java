package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import java.util.ArrayList;

import chronomuncher.ChronoMod;

public class RandomCardFromDeckToHandAction
  extends AbstractGameAction
{
  private AbstractPlayer p;
  public AbstractCard.CardType type;
  
  public RandomCardFromDeckToHandAction(AbstractCard.CardType type)
  {
    this.p = AbstractDungeon.player;
    this.type = type;
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
  }
  
  public void update()
  {
    // Grab the card group
    AbstractCard c = this.p.drawPile.getRandomCard(this.type, true);

    // If there are no cards in deck, or no cards of the type, return
    if (c == null) {
      this.isDone = true;
      return;
    }
    // Otherwise add that card to hand

    // Well, unless the hand is full
    if (this.p.hand.size() == 10)
    {
      this.p.drawPile.moveToDiscardPile(c);
      this.p.createHandIsFullDialog();
    }
    else
    {
      this.p.drawPile.moveToHand(c, this.p.drawPile);
    }

    this.isDone = true;
  }
}
