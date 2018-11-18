package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.DelayedAttackPower;

public class BiteCommand extends MetricsCard {
	public static final String ID = "BiteCommand";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
    private static final int TURNS = 2;
  	private static final int ATTACK_DMG = 16;
  	private static final int UPGRADE_DMG = 5;
	private static final int UPGRADE_AMT = -1;

	public BiteCommand() {
		super(ID, NAME, "chrono_images/cards/BiteCommand.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = TURNS;
		this.magicNumber = TURNS;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(m, p, new DelayedAttackPower(m, this.magicNumber, this.damage), this.magicNumber));
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_AMT);
			upgradeDamage(UPGRADE_DMG);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
   		}
	}
}