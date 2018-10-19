package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class WastingAway extends MetricsCard {
	public static final String ID = "WastingAway";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 40;
	private static final int DECREASE = 6;
	private static final int DECREASE_UPGRADE = -3;

	public WastingAway() {
		super(ID, NAME, "chrono_images/cards/WastingAway.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG - this.misc;
		this.baseMagicNumber = DECREASE;
		this.magicNumber = this.baseMagicNumber;
		this.misc = 0;

		this.baseDamage -= this.misc;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    	
    	// AbstractDungeon.actionManager.addToBottom(new IncreaseMiscAction(this.cardID, this.misc, this.magicNumber));
	}

	@Override
	public void applyPowers()
	{
		this.baseDamage = ATTACK_DMG - this.misc;
	    super.applyPowers();
	    initializeDescription();
    }

	@Override
	public AbstractCard makeCopy() {
		return new WastingAway();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(DECREASE_UPGRADE);
		}
	}
}