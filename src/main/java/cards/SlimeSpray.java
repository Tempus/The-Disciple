package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;

import chronomuncher.vfx.SlimeSplashEffect;

import com.megacrit.cardcrawl.actions.utility.SFXAction;
import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class SlimeSpray extends AbstractSelfRetainingCard {
	public static final String ID = "SlimeSpray";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;
	private static final int ATTACK_DMG = 7;
	private static final int UPGRADE_PLUS_DMG = 4;

	public SlimeSpray() {
		super(ID, NAME, "chrono_images/cards/SlimeSpray.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ALL_ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.isMultiDamage = true;
		this.retains = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    AbstractDungeon.actionManager.addToBottom(
	    		new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
 
 		AbstractDungeon.actionManager.addToBottom(
			new SFXAction("MONSTER_SLIME_ATTACK", 0.4F));

	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDead && !mo.escaped) {
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(mo, p, new SlowPower(mo, 0), 0, true, AbstractGameAction.AttackEffect.NONE));

				AbstractDungeon.actionManager.addToBottom(
					new VFXAction(new SlimeSplashEffect(mo.drawX, mo.drawY + mo.hb_h/2.0F)));
			}
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
		}
	}
}