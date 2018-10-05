package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.AuguryPower;

import basemod.helpers.BaseModCardTags;

public class OracleForm extends MetricsCard {
	public static final String ID = "OracleForm";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 3;

	public OracleForm() {
		super(ID, NAME, "images/cards/OracleForm.png", COST, DESCRIPTION, AbstractCard.CardType.POWER,
				Enum.BRONZE, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		if (AbstractDungeon.player != null) { 
			this.baseMagicNumber = AbstractDungeon.player.gameHandSize-1;
		} else {
			this.baseMagicNumber = 4;
		}
		
		this.magicNumber = this.baseMagicNumber;

    	this.tags.add(BaseModCardTags.FORM);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower("Augury")) { 
			if (this.upgraded) {
				AuguryPower a = (AuguryPower)p.getPower("Augury");
				a.upgraded = true;
			}
		} else {
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(p, p, new AuguryPower(this.upgraded), 0));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new OracleForm();
	}

	@Override
	public void triggerWhenDrawn() {
		this.magicNumber = AbstractDungeon.player.gameHandSize-1;
		if (this.upgraded) { this.magicNumber++; }
	}

	public void atTurnStart() {
		this.magicNumber = AbstractDungeon.player.gameHandSize-1;
		if (this.upgraded) { this.magicNumber++; }
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(1);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}