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

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class PrimeTime extends MetricsCard {
	public static final String ID = "PrimeTime";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 9;
	private static final int UPGRADE_PLUS_DMG = 4;

	public Boolean wasRetained = false;

	public PrimeTime() {
		super(ID, NAME, "chrono_images/cards/PrimeTime.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (this.wasRetained) {
		    this.damage = this.damage * 2;
		    this.isDamageModified = true;
		}
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		PrimeTime ret = (PrimeTime)super.makeStatEquivalentCopy();
		ret.wasRetained = this.wasRetained;
		return ret;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		if (this.wasRetained) {
		    this.damage = this.damage * 2;
		    this.isDamageModified = true;
		}
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.wasRetained = false;
	}

	public void update() {
		super.update();
		if (this.retain) { this.wasRetained = true; }
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
		}
	}
}