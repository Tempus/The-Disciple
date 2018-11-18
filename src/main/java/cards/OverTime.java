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


public class OverTime extends MetricsCard {
	public static final String ID = "OverTime";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;
	private static final int ATTACK_PER_CARD = 4;
	private static final int UPGRADE_PLUS_DMG = 3;

	public OverTime() {
		super(ID, NAME, "chrono_images/cards/OverTime.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = 0;
		this.baseMagicNumber = ATTACK_PER_CARD;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (!(this.baseDamage == 0)){
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		}
	}

	@Override
	public void atTurnStart() {
		this.baseDamage = AbstractDungeon.player.exhaustPile.size() * this.magicNumber;
	}

	@Override
	public void triggerOnOtherCardPlayed(AbstractCard c) {
		this.baseDamage = AbstractDungeon.player.exhaustPile.size() * this.magicNumber;
	}

	@Override
	public void applyPowers() {
		this.baseDamage = AbstractDungeon.player.exhaustPile.size() * this.magicNumber;
		super.applyPowers();
		initializeDescription();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_PLUS_DMG);
		}
	}
}