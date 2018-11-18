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
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;

import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;

import chronomuncher.powers.WakeUpCallPower;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class WakeUpCall extends AbstractSelfRetainingCard {
	public static final String ID = "WakeUpCall";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int ATTACK_DMG = 44;
	private static final int ATTACK_DMG_UP = 16;

	public WakeUpCall() {
		super(ID, NAME, "chrono_images/cards/WakeUpCall.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = ATTACK_DMG;
		this.magicNumber = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if ((m.intent == AbstractMonster.Intent.SLEEP) || (m.intent == AbstractMonster.Intent.STUN)) {
	        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(m.hb.cX, m.hb.cY)));
			AbstractDungeon.actionManager.addToBottom(new SFXAction("BELL", 1.0F));
		    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL)));
		}	else {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WakeUpCallPower(m, this.magicNumber), 1));
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(ATTACK_DMG_UP);
			upgradeMagicNumber(ATTACK_DMG_UP);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}