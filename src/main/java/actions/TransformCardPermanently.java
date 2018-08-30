package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.cards.CardGroup;

import java.util.ArrayList;
import java.util.Iterator;

import chronomuncher.ChronoMod;
import chronomuncher.cards.AbstractSwitchCard;

public class TransformCardPermanently extends AbstractGameAction {

	private AbstractPlayer p;
	private boolean upgraded;
	private AbstractCard transformToCard;
  	private AbstractCard transformee;
  	private boolean toHand;

	public TransformCardPermanently(AbstractPlayer p, AbstractCard transformee, AbstractCard transformToCard, boolean toHand) {
		this.p = p;
		this.transformToCard = transformToCard;
		this.transformee = transformee;
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
		this.toHand  = toHand;
	}

	public TransformCardPermanently(AbstractPlayer p, AbstractCard transformee, AbstractCard transformToCard) {
		this(p, transformee, transformToCard, false); }

	public void update() {

		// Transformed cards transform into upgrade versions of themselves.
	    UnlockTracker.markCardAsSeen(this.transformToCard.cardID);
	    if ((this.transformee.upgraded) && (this.transformToCard.canUpgrade())) {
	     	this.transformToCard.upgrade();
	    }

		// Make the new card temporarily in the battle, and discard it
		if (this.toHand) {
 			AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(this.transformToCard)); }
		else {
  			AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(this.transformToCard, 1));
  		}

		this.transformee.exhaust = true;
		if (this.transformee.purgeOnUse) {
			this.transformToCard.purgeOnUse = true; }
		this.transformee.purgeOnUse = true;

  		// And add it to the deck
		AbstractDungeon.player.masterDeck.addToBottom(this.transformToCard);

  		// Remove the original card from the deck
		this.removeFromCardGroup(AbstractDungeon.player.masterDeck, (AbstractSwitchCard)this.transformee, this.transformee.upgraded);

		this.isDone = true;
	}

	public boolean removeFromCardGroup(CardGroup group, AbstractSwitchCard card, boolean upgrade) {

	    for (Iterator<AbstractCard> i = group.group.iterator(); i.hasNext();)
	    {
	      AbstractCard e = (AbstractCard)i.next();
		  if (e instanceof AbstractSwitchCard) {
	      	AbstractSwitchCard f = (AbstractSwitchCard)e;
		    if (f.switchCardUniqueID == card.switchCardUniqueID)
		    {
		      i.remove();
		      return true;
		    }
		  }
	    }
	    return false;
   	}

	// public boolean findInCardGroup(CardGroup group, String cardID, boolean upgrade) {

	//     for (Iterator<AbstractCard> i = group.group.iterator(); i.hasNext();)
	//     {
	//       AbstractCard e = (AbstractCard)i.next();
	//       if (e.cardID.equals(cardID) && e.upgraded == upgrade)
	//       {
	//         return true;
	//       }
	//     }
	//     return false;
 //   	}
}