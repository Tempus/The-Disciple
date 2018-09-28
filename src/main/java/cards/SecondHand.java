package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;

import basemod.helpers.BaseModTags;
import basemod.helpers.CardTags;

public class SecondHand extends MetricsCard {
	public static final String ID = "SecondHand";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int RETAIN = 1;
	private static final int RETAIN_UPGRADE = 1;
	private static final int BLOCK = 5;
	private static final int BLOCK_UPGRADE = 2;

	public SecondHand() {
		super(ID, NAME, "images/cards/SecondHand.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = RETAIN;
		this.magicNumber = this.baseMagicNumber;
		this.baseBlock = BLOCK;
		this.block = BLOCK;

		CardTags.addTags(this, BaseModTags.GREMLIN_MATCH);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hand.group.size() > 0) {
			// AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(p, this.magicNumber));
		}
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RetainOncePower(this.magicNumber), this.magicNumber));

		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));		
	}

	@Override
	public AbstractCard makeCopy() {
		return new SecondHand();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBlock(BLOCK_UPGRADE);
			upgradeMagicNumber(RETAIN_UPGRADE);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}