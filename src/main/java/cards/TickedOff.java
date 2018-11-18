package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.HastePower;


public class TickedOff extends AbstractSelfRetainingCard {
	public static final String ID = "TickedOff";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;
	private static final int BLOCK = 8;
	private static final int BLOCK_UPGRADE = 5;

	public TickedOff() {
		super(ID, NAME, "chrono_images/cards/TickedOff.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseBlock = BLOCK;
		this.retains = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HastePower(p, 0), 0));
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBlock(BLOCK_UPGRADE);
		}
	}
}