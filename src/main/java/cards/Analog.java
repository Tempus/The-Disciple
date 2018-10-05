package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;


public class Analog extends MetricsCard {
	public static final String ID = "Analog";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;
	private static final int POWER = 1;

	public Analog() {
		super(ID, NAME, "images/cards/Analog.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ALL_ENEMY);

		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;

		this.retain = true;
	}

	@Override
	public void atTurnStart() {
		this.retain = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
	   
	   		if (!mo.isDead && !mo.escaped) {

		   		// Strength
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new StrengthPower(p,this.magicNumber),this.magicNumber,true));
		    	}

		    	// Dexterity
		    	if ((mo.intent == AbstractMonster.Intent.DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new DexterityPower(p,this.magicNumber),this.magicNumber,true));
		    	}
		    			
		    	// Artifact
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.STRONG_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEBUFF)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new ArtifactPower(p,this.magicNumber),this.magicNumber,true));
		    	}

		    	// Retain Card
		    	if ((mo.intent == AbstractMonster.Intent.ESCAPE) ||
		    		(mo.intent == AbstractMonster.Intent.MAGIC) ||
		    		(mo.intent == AbstractMonster.Intent.SLEEP) ||
		    		(mo.intent == AbstractMonster.Intent.STUN) ||
		    		(mo.intent == AbstractMonster.Intent.UNKNOWN)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new RetainOncePower(this.magicNumber),this.magicNumber,true));
		    	}

	    	}
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Analog();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(1);
		}
	}
}