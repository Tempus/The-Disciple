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

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class PreemptiveStrike extends MetricsCard {
	public static final String ID = "PreemptiveStrike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int ATTACK_DMG = 18;
	private static final int ATTACK_DMG_UP = 7;

	public PreemptiveStrike() {
		super(ID, NAME, "images/cards/PreemptiveStrike.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if ((m.intent == AbstractMonster.Intent.BUFF) || (m.intent == AbstractMonster.Intent.DEFEND_BUFF) || (m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
			(m.intent == AbstractMonster.Intent.DEBUFF) || (m.intent == AbstractMonster.Intent.DEFEND_DEBUFF) || (m.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
			(m.intent == AbstractMonster.Intent.STRONG_DEBUFF))) {
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
		}
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
	}

	@Override
	public void atTurnStart() {
		if (this.upgraded) {
			this.retain = true;
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new PreemptiveStrike();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.retain = true;
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}