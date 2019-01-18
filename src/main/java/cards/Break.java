package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class Break extends MetricsCard {
	public static final String ID = "Break";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_PLUS_BLOCK = 3;
	private static final int NEXT_BLOCK_AMT = 8;
	private static final int NEXT_UPGRADE_PLUS_BLOCK = 3;

	public Break() {
		super(ID, NAME, "chrono_images/cards/Break.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ALL);

		this.baseBlock = BLOCK_AMT;
		this.baseMagicNumber = NEXT_BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDead && !mo.escaped && !mo.isDying && !mo.halfDead) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, mo, this.magicNumber));
			}
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_PLUS_BLOCK);
			upgradeMagicNumber(NEXT_UPGRADE_PLUS_BLOCK);
		}
	}
}