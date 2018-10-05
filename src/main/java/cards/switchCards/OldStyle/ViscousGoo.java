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
import com.megacrit.cardcrawl.powers.WeakPower;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.TransformCardPermanently;
import chronomuncher.cards.ThickGoo;
import chronomuncher.cards.AbstractSwitchCard;


public class ViscousGoo extends AbstractSwitchCard {
	public static final String ID = "ViscousGoo";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	


	private static final int COST = 0;
	private static final int ATTACK_DMG = 3;
	private static final int UPGRADE_PLUS_DMG = 2;
	private static final int EFFECT = 2;
	private static final int EFFECT_UPGRADE = 1;	

	public ViscousGoo() {
		super(ID, NAME, "chrono_images/cards/ViscousGoo.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY, ThickGoo.class);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = EFFECT;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));

		AbstractDungeon.actionManager.addToBottom(
			new TransformCardPermanently(p, this, new ThickGoo()));
	}

	@Override
	public AbstractCard makeCopy() {
		return new ViscousGoo();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeMagicNumber(EFFECT_UPGRADE);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}