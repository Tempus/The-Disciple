package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import java.util.ArrayList;
import java.util.Iterator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedHand;
import basemod.BaseMod;
import basemod.interfaces.OnCardUseSubscriber;

public class UnlockedHand extends ReplicaOrb 
{

  public UnlockedHand(boolean upgraded)
  {
    super(  "Hand",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            2,                  // int passiveUp, 
            8,                  // int timer
            8,                  // int timerUp
            new LockedHand(),
            "Mummified Hand"); // AbstractCard locked)
  }
    
  @Override
  public void onCardUse(AbstractCard c)
  { 
    this.activateEffect();

    if (c.type == AbstractCard.CardType.POWER) {
      this.reduceCardCost();
    }
  }

  public void reduceCardCost() {
    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new MummifiedHand()));
    AbstractCard c;

    // Make a list of all cards that cost energy in your hand
    ArrayList<AbstractCard> groupCopy = new ArrayList();
    for (AbstractCard handCard : AbstractDungeon.player.hand.group) {
      if ((handCard.cost > 0) && (handCard.costForTurn > 0)) {
        groupCopy.add(handCard);
      }
    }

    // Remove any cards in your hand that have been put into the queue (since they've already been paid for)
    CardQueueItem i = new CardQueueItem();

    for (Iterator<CardQueueItem> cardQueue = AbstractDungeon.actionManager.cardQueue.iterator(); cardQueue.hasNext(); i = (CardQueueItem)cardQueue.next() )
    {
      if ((i.card != null) && (i.card.cost > 0) && (i.card.costForTurn > 0))
      {
        groupCopy.remove(i.card);
      }
    }
    
    // Pick a card and discount it
    if (!groupCopy.isEmpty()) {
      ChronoMod.log("Card group not empty");
      c = (AbstractCard)groupCopy.get(AbstractDungeon.cardRng.random(groupCopy.size() - 1));
      c.modifyCostForTurn(-this.passiveAmount);
      c.flash();
    } else {
      ChronoMod.log("Card group is empty");
    }
  }

  @Override
  public AbstractOrb makeCopy() { return new UnlockedHand(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary