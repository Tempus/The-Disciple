package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.GameActionManager;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.TransformCardPermanently;
import chronomuncher.cards.ShortSighted;

public class Foolish extends MetricsCard {
	public static final String ID = "Foolish";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;

	public Foolish() {
		super(ID, NAME, "chrono_images/cards/Foolish.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int diff = 0;

		if (!this.upgraded){
			diff = p.maxHealth/2-p.currentHealth;
		} else {
			diff = ((p.maxHealth/3)*2)-p.currentHealth;
		}

		if (diff > 0) {
			AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, diff));}
		else {
			AbstractDungeon.actionManager.addToBottom(new LoseHPAction(p, p, diff));}

		AbstractDungeon.actionManager.addToBottom(new TransformCardPermanently(p, this, new ShortSighted()));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Foolish();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}