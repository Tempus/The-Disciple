package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class BackFourSeconds extends AbstractSelfRetainingCard {
	public static final String ID = "BackFourSeconds";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	public int HP_PERCENT = 50;
	public int HP_PERCENT_UPGRADE = 25;
	private static final int COST = 2;

	public BackFourSeconds() {
		super(ID, NAME, "chrono_images/cards/BackFourSeconds.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.magicNumber = this.baseMagicNumber = HP_PERCENT;
		this.exhaust = true;
		this.retains = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-TICKINGDIRTY"));
		AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, (int)((AbstractDungeon.actionManager.playerHpLastTurn-p.currentHealth)*(this.magicNumber/100.0F))));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			// upgradeBaseCost(2);
			upgradeName();
			upgradeMagicNumber(HP_PERCENT_UPGRADE);
      		// this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}