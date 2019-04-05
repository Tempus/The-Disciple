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
		super(ID, NAME, "chrono_images/cards/OracleForm.png", COST, DESCRIPTION, AbstractCard.CardType.POWER,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = 5;
		this.magicNumber = this.baseMagicNumber;
		this.updateBodyText(false);

    	this.tags.add(BaseModCardTags.FORM);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower("Augury")) { 
			int drawIncrease = 2;
			if (this.upgraded) {
				drawIncrease = 3;
			}
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(p, p, new AuguryPower(this.upgraded), drawIncrease));
		} else {
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(p, p, new AuguryPower(this.upgraded), 0));
		}
	}

  	public void triggerWhenDrawn() { this.updateBodyText(false); }
	public void onPlayCard(AbstractCard c, AbstractMonster m) { 
		if (c.cardID == "OracleForm") {
			this.updateBodyText(true); 
		} else {
			this.updateBodyText(false); 
		}
	}
	public void atTurnStart() { this.updateBodyText(false); }

	public void updateBodyText(boolean now) {
		if (AbstractDungeon.player != null) { 
			this.magicNumber = AbstractDungeon.player.gameHandSize;
			if (this.upgraded) { 
				this.magicNumber++; 
			}

			if (AbstractDungeon.player.hasPower("Augury") || now) { 
				this.baseMagicNumber = 2;
				this.magicNumber = 2;
				if (this.upgraded) { 
					this.baseMagicNumber++;
					this.magicNumber++;
				}

	      		this.rawDescription = "Choose an additional !M! cards.";
	   		   	initializeDescription();
			}
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(1);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	updateBodyText(false);
		}
	}
}