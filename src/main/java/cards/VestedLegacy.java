package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.cards.Ward;

public class VestedLegacy extends MetricsCard {
	public static final String ID = "VestedLegacy";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = 1;
	private static final int WARDS = 3;
	private static final int DECK_WARDS = 2;
	private static int powerCount = 0;

	public VestedLegacy() {
		super(ID, NAME, "chrono_images/cards/VestedLegacy.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = WARDS;
		this.magicNumber = WARDS;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Ward card = new Ward();
		if (this.upgraded){ card.upgrade(); }

		for (int i=0; i<this.magicNumber; i++) {
			AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(card, false));			
		}
		AbstractDungeon.actionManager.addToTop(new MakeTempCardInDrawPileAction(card, DECK_WARDS, true, true));			
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