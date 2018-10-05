package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class TimelySolution extends MetricsCard {
	public static final String ID = "TimelySolution";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;

	public TimelySolution() {
		super(ID, NAME, "chrono_images/cards/TimelySolution.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ALL_ENEMY);

		this.baseMagicNumber = 2;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void atTurnStart() {
		this.retain = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
	    	if ((mo != null) && (mo.hasPower("Minion"))) {
				AbstractDungeon.actionManager.addToBottom(
					new DamageAction(mo, new DamageInfo(mo, 999, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
	    	}
		}

		if (this.upgraded) { AbstractDungeon.actionManager.addToBottom(
			new DrawCardAction(p, this.magicNumber, false)); 
		}
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
	}

	@Override
	public AbstractCard makeCopy() {
		return new TimelySolution();
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