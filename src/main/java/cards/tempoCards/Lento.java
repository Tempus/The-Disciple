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

public class Lento extends MetricsCard {
	public static final String ID = "Lento";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;

	private static final int DMG = 12;
	private static final int UPGRADE_PLUS_DMG = 5;

	private static final int BLOCK = 16;
	private static final int UPGRADE_PLUS_BLOCK = 7;

	private static final int MAGIC = 2;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Lento() {
		super(ID, NAME, "chrono_images/cards/Lento.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.SELF);

		// this.baseDamage = DMG;
		
		this.baseBlock = BLOCK;

		this.baseMagicNumber = MAGIC;
		this.magicNumber = UPGRADE_PLUS_MAGIC;
    	this.tags.add(Enum.TEMPO_CARD);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new GainBlockAction(p, p, this.block));


	}

	@Override
	public AbstractCard makeCopy() {
		return new Lento();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			// upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeBlock(UPGRADE_PLUS_BLOCK);
			upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
		}
	}
}