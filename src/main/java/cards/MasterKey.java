package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import chronomuncher.actions.*;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.FocusPower;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.orbs.*;
import chronomuncher.actions.PlayFreePowersAction;

public class MasterKey extends MetricsCard {
	public static final String ID = "MasterKey";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = 1;

	public MasterKey() {
		super(ID, NAME, "images/cards/MasterKey.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new PlayFreePowersAction(this.upgraded));
   	}

   	public void reactivateAllOrbEffects(AbstractPlayer p) {
		for (AbstractOrb unlocked : AbstractDungeon.actionManager.orbsChanneledThisCombat) {

			switch (unlocked.ID) {

				case "Carbon":
				case "Flame":
				case "Lightning":
				case "Tornado":
				case "IceCream":
				case "Medicine":
				case "Plans":
				case "Thread":
				    unlocked.onStartOfTurn();
					break;

				case "Orichalcum":
				case "Mercury":
					unlocked.onEndOfTurn();
					break;

				case "Hand":
					for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
	    				if (c.type == AbstractCard.CardType.POWER) {
							UnlockedHand hand = (UnlockedHand)unlocked;
							hand.reduceCardCost();
						}
					}
					break;

				case "Blood":
				    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(p, new BloodVial()));
				    AbstractDungeon.actionManager.addToTop(new HealAction(p, p, 2));
					break;

				case "Calipers":
					// Nothing happens... right? Maybe we should make a custom Calipers power to go here.
					break;

				case "Nitrogen":
    				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, unlocked.passiveAmount), unlocked.passiveAmount));    
					break;

				case "Urn":
					for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
	    				if (c.type == AbstractCard.CardType.POWER) {
						    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new BirdFacedUrn()));
						    AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, unlocked.passiveAmount));
						}
					}
					break;
			}
		}
   	}

	@Override
	public AbstractCard makeCopy() {
		return new MasterKey();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}