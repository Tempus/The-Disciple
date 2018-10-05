package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import chronomuncher.ChronoMod;

public class PlayExhaustedCardAction extends AbstractGameAction {

	private AbstractPlayer p;

	public PlayExhaustedCardAction(AbstractCreature target, int amount) {
		this.amount = amount;
	    this.duration = Settings.ACTION_DUR_FAST;
	    this.actionType = AbstractGameAction.ActionType.WAIT;
	    this.source = AbstractDungeon.player;
	    this.target = target;
	}

	public void update() {

		if (this.amount > AbstractDungeon.player.exhaustPile.size()) {
			this.amount = AbstractDungeon.player.exhaustPile.size();
		}

		for (int i = 0; i < this.amount ; i++ ) {
			
			if (AbstractDungeon.player.exhaustPile.isEmpty())
			{
				this.isDone = true;
				return;
			}
			else
			{
				AbstractCard card = AbstractDungeon.player.exhaustPile.getRandomCard(true);

	    		AbstractCard tmp = card.makeStatEquivalentCopy();
				tmp.freeToPlayOnce = true;
	 		    tmp.purgeOnUse = true;
				tmp.target_x = (Settings.WIDTH / 2.0F - (200.0f * i * Settings.scale));
				tmp.target_y = (Settings.HEIGHT / 2.0F);
				tmp.current_x = tmp.target_x;
				tmp.current_y = tmp.target_y;

				AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);

			    if (m != null) {
			      tmp.calculateCardDamage(m);
			    }
			    AbstractDungeon.player.useCard(tmp, m, card.energyOnUse);
			}
		}
		
		this.isDone = true;	
	}
}