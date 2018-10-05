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
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import chronomuncher.powers.HastePower;
import chronomuncher.powers.SleepPower;
import chronomuncher.powers.StunPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.IntentTransformAction;

public class Sospirando extends MetricsCard {
	public static final String ID = "Sospirando";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 2;

	private static final int DMG = 12;
	private static final int UPGRADE_PLUS_DMG = 5;

	private static final int BLOCK = 10;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Sospirando() {
		super(ID, NAME, "images/cards/Sospirando.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ENEMY);

		// this.baseDamage = DMG;
		
		// this.baseBlock = BLOCK;

		this.baseMagicNumber = MAGIC;
		this.magicNumber = UPGRADE_PLUS_MAGIC;

		this.exhaust = true;
    	this.tags.add(Enum.TEMPO_CARD);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		if (m.type == AbstractMonster.EnemyType.BOSS && !this.upgraded) {
			AbstractDungeon.effectList.add(new PowerBuffEffect(m.hb.cX - m.animX, m.hb.cY + m.hb.height / 2.0F - 48.0F, "Immune"));
			return;
		}

		// if (!this.upgraded) {
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(m, p, new StunPower(m, this.magicNumber), this.magicNumber));
		// } else {
		//     for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
		// 		if (!mo.isDead && !mo.escaped && mo != null) {
		// 			AbstractDungeon.actionManager.addToBottom(
		// 				new ApplyPowerAction(mo, p, new StunPower(mo, this.magicNumber), this.magicNumber)); } }
		// }
	}

	@Override
	public AbstractCard makeCopy() {
		return new Sospirando();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
   		   	// this.exhaust = false;
		}
	}
}