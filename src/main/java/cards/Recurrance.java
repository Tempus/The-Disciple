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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;

import java.lang.Math;

public class Recurrance extends MetricsCard {
	public static final String ID = "Recurrance";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 2;
	private static final int HITS = 2;
	private static final int HITS_UPGRADE = 1;

	public Recurrance() {
		super(ID, NAME, "images/cards/Recurrance.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = HITS;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		// SANE MULTI-HITS? OR MAYBE COPY PASTA WAS RIGHT ALL ALONG
		for (int hit = 0; hit < this.magicNumber; hit++) {
			// Apply the effect
			AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.NONE;
			if (hit == this.magicNumber-1) { effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT; }
			else { effect = AbstractGameAction.AttackEffect.NONE; }

			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), effect));
		}

		int unblockedHits = this.magicNumber - (int)Math.floor(m.currentBlock / this.damage);

		if (unblockedHits < 1) { return; }

		// HITS WERE UNBLOCKED, SO ADD SHIT TO STACKS
	    for (AbstractPower pow : m.powers) {
			if (pow.type == AbstractPower.PowerType.DEBUFF) {
				if (pow.canGoNegative == true) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, -this.magicNumber, true));
				} else {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, this.magicNumber, true));
				}
			}
	    }
	}

	@Override
	public AbstractCard makeCopy() {
		return new Recurrance();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(HITS_UPGRADE);
		}
	}
}