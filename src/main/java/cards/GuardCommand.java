package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.DelayedBlockPower;


public class GuardCommand extends MetricsCard {
	public static final String ID = "GuardCommand";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int BLOCK_AMT = 16;

	public GuardCommand() {
		super(ID, NAME, "chrono_images/cards/GuardCommand.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);

		this.baseBlock = BLOCK_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(p, p, new DelayedBlockPower(p, 1, this.block), 1, true, AbstractGameAction.AttackEffect.SHIELD));
		} else {
			AbstractDungeon.actionManager.addToBottom(
				new ApplyPowerAction(p, p, new DelayedBlockPower(p, 2, this.block), 2, true, AbstractGameAction.AttackEffect.SHIELD));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new GuardCommand();
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