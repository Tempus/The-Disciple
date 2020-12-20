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

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;
import chronomuncher.cards.AbstractSelfSwitchCard;
import chronomuncher.cards.SwitchGoo;

public class PlayEchoCardAction extends AbstractGameAction {

	private AbstractPlayer p;
	private AbstractCard card;

	public PlayEchoCardAction(AbstractCard card, AbstractCreature target) {
		this.amount = amount;
	    this.duration = Settings.ACTION_DUR_FAST;
	    this.actionType = AbstractGameAction.ActionType.WAIT;
	    this.source = AbstractDungeon.player;
	    this.target = target;
	    this.card = card;
	}

	public void update() {
	    ChronoMod.log("Doubling: " + card.cardID);
	    this.card.flash();
	    AbstractMonster m = null;
	    if (this.target != null) {
	      m = (AbstractMonster)this.target;
	    }
	    if (m == null) {
	      m = AbstractDungeon.getMonsters().getRandomMonster(true);
	    }
	    AbstractCard tmp = card.makeStatEquivalentCopy();
	    AbstractDungeon.player.limbo.addToBottom(tmp);
	    tmp.current_x = card.current_x;
	    tmp.current_y = card.current_y;
	    tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
	    tmp.target_y = (Settings.HEIGHT / 2.0F);
	    tmp.freeToPlayOnce = true;
	    tmp.purgeOnUse = true;
	    if (m != null) {
	      tmp.calculateCardDamage(m);
	    }
	    if (tmp instanceof AbstractSelfSwitchCard) {
	    	AbstractSelfSwitchCard s = (AbstractSelfSwitchCard)tmp;
	    	s.switchTo(s.switchID);
	    	if (tmp instanceof SwitchGoo) {
	    		s.switchTo(s.switchID);
	    	}
	    }
	    AbstractDungeon.player.useCard(tmp, m, card.energyOnUse);

	    this.isDone = true;
	}
}