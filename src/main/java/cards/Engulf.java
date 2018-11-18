package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.vfx.SlimeSplashEffect;


public class Engulf extends MetricsCard {
	public static final String ID = "Engulf";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 3;
	private static final int UPGRADE_PLUS_DMG = 1;
	private static final int SLOWSTACKS = 5;
	private static final int SLOWSTACKS_UP = 3;

	public Engulf() {
		super(ID, NAME, "chrono_images/cards/Engulf.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = SLOWSTACKS;
		this.magicNumber = SLOWSTACKS;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));

		AbstractDungeon.actionManager.addToBottom(
			new SFXAction("SLIME_ATTACK_2", 0.4F));

		AbstractDungeon.actionManager.addToBottom(
			new VFXAction(new SlimeSplashEffect(m.drawX, m.drawY + m.hb_h/2.0F)));

		AbstractDungeon.actionManager.addToBottom(
			new ApplyPowerAction(m, p, new SlowPower(m, this.magicNumber-1), this.magicNumber-1, true, AbstractGameAction.AttackEffect.NONE));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeMagicNumber(SLOWSTACKS_UP);
		}
	}
}