package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.cards.CardGroup;

import chronomuncher.ChronoMod;
import chronomuncher.actions.TransformCardPermanently;
import chronomuncher.cards.*;


public class IntentTransformAction extends AbstractGameAction {

	private AbstractPlayer p;
	private AbstractMonster m;
	private boolean upgraded = false;
	private AbstractCard transformToCard;
  	private AbstractCard transformee;

	public IntentTransformAction(AbstractPlayer p, AbstractMonster m, boolean upgraded) {
		this(p,m,null);
		this.upgraded = upgraded;
	}

	public IntentTransformAction(AbstractPlayer p, AbstractMonster m, AbstractCard transformee) {
		this.p = p;
		this.m = m;
		this.transformee = transformee;
	}

	public void update() {
		AbstractCard newCard;

		if (this.m.intent == AbstractMonster.Intent.ATTACK) {
			newCard = new Allegro(); }
		else if (this.m.intent == AbstractMonster.Intent.ATTACK_BUFF) {
			newCard = new Vivace(); }
		else if (this.m.intent == AbstractMonster.Intent.ATTACK_DEBUFF) {
			newCard = new Moderato(); }
		else if (this.m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
			newCard = new Allegretto(); }
		else if (this.m.intent == AbstractMonster.Intent.BUFF) {
			newCard = new Accelerando(); }
		else if (this.m.intent == AbstractMonster.Intent.DEBUFF) {
			newCard = new Rallentando(); }
		else if (this.m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
			newCard = new Ritenuto(); }
		else if (this.m.intent == AbstractMonster.Intent.DEFEND) {
			newCard = new Lento(); }
		else if (this.m.intent == AbstractMonster.Intent.DEFEND_BUFF) {
			newCard = new Adagio(); }
		else if (this.m.intent == AbstractMonster.Intent.DEFEND_DEBUFF) {
			newCard = new Largo(); }
		else if (this.m.intent == AbstractMonster.Intent.ESCAPE) {
			newCard = new Presto(); }
		else if (this.m.intent == AbstractMonster.Intent.MAGIC) {
			newCard = new Maestoso(); }
		else if (this.m.intent == AbstractMonster.Intent.SLEEP) {
			newCard = new Grave(); }
		else if (this.m.intent == AbstractMonster.Intent.STUN) {
			newCard = new Sospirando(); }
		else if (this.m.intent == AbstractMonster.Intent.UNKNOWN) {
			newCard = new Misterioso(); }
		else { 
			this.isDone = true;
			return ; }

		if (this.transformee != null) {
			// Make the new card temporarily in the battle, and discard it
	  		AbstractDungeon.actionManager.addToTop(new TransformCardPermanently(this.p, this.transformee, newCard, false));
	  	} else {
		    UnlockTracker.markCardAsSeen(newCard.cardID);
		    if ((this.upgraded) && (newCard.canUpgrade())) {
		     	newCard.upgrade();
		    }

	  			AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(newCard, 1));

			AbstractDungeon.player.masterDeck.addToBottom(newCard);
	  	}

		this.isDone = true;
	}
}