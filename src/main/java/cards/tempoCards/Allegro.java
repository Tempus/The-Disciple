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
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import chronomuncher.actions.PlayExhaustedCardAction;
import chronomuncher.actions.SmokeBombAction;

import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import chronomuncher.powers.HastePower;
import chronomuncher.powers.SleepPower;
import chronomuncher.powers.StunPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.IntentTransformAction;

public class Allegro extends MetricsCard {
	public static final String ID = "Allegro";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;

	private static final int DMG = 18;
	private static final int UPGRADE_PLUS_DMG = 7;

	private static final int BLOCK = 10;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Allegro() {
		super(ID, NAME, "images/cards/Allegro.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ENEMY);

		this.baseDamage = DMG;
		
		// this.baseBlock = BLOCK;

		this.baseMagicNumber = MAGIC;
		this.magicNumber = UPGRADE_PLUS_MAGIC;
    	this.tags.add(Enum.TEMPO_CARD);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));


	}

	@Override
	public AbstractCard makeCopy() {
		return new Allegro();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeBlock(UPGRADE_PLUS_BLOCK);
			upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
		}
	}
}