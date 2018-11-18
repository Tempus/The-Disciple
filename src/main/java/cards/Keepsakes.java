package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import java.lang.Math;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;


public class Keepsakes extends MetricsCard {
	public static final String ID = "Keepsakes";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 4;
	private static final int HITS = 2;
	private static final int HITS_UPGRADE = 1;

	public Keepsakes() {
		super(ID, NAME, "chrono_images/cards/Keepsakes.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = HITS;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int block = m.currentBlock;

		// SANE MULTI-HITS? OR MAYBE COPY PASTA WAS RIGHT ALL ALONG
		for (int hit = 0; hit < this.magicNumber; hit++) {
			// Apply the effect
			AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.NONE;
			if (hit == this.magicNumber-1) { effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT; }

        	AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-TICK"));
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), effect));
		}

		// Retain cards based on unblocked hits
		// Check for unblocked hits
		int unblockedHits = this.magicNumber - (int)Math.floor(m.currentBlock / this.damage);

		if (unblockedHits > 0 && p.hand.group.size() > 0) {
		    // AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(p, unblockedHits));
		    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainOncePower(unblockedHits), unblockedHits));
  		}	
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(HITS_UPGRADE);
		}
	}
}