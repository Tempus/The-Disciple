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


import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.DelayedAttackPower;


public class ShortSighted extends MetricsCard {
	public static final String ID = "ShortSighted";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int HEAL = 6;
	private static final int HEAL_UPGRADE = 4;

	public ShortSighted() {
		super(ID, NAME, "chrono_images/cards/ShortSighted.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = HEAL;
		this.magicNumber = this.baseMagicNumber;

		this.baseDamage = HEAL * 2;
    	this.tags.add(AbstractCard.CardTags.HEALING);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
 	    // AbstractDungeon.actionManager.addToBottom(new ExhaustAction(p, p, 1, true));

		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(p, p, new DelayedAttackPower(p, 4, this.magicNumber * 2, true), 5));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(HEAL_UPGRADE);
			upgradeDamage(HEAL_UPGRADE * 2);
		}
	}
}