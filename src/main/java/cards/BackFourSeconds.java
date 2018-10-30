package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.GameActionManager;


import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class BackFourSeconds extends MetricsCard {
	public static final String ID = "BackFourSeconds";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 3;

	public BackFourSeconds() {
		super(ID, NAME, "chrono_images/cards/BackFourSeconds.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, AbstractDungeon.actionManager.playerHpLastTurn-p.currentHealth));
	}

	// public void onMoveToDiscard() {
	// 	super.onMoveToDiscard();
	// 	this.costForTurn = this.cost;
	// 	this.isCostModifiedForTurn = false;
	// }

	@Override
	public void atTurnStart() {
		// if (this.upgraded) { 
			this.retain = true;
			 // }
	}

	@Override
	public AbstractCard makeCopy() {
		return new BackFourSeconds();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			// this.retain = true;
			upgradeBaseCost(2);
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}