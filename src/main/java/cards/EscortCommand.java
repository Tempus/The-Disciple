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


public class EscortCommand extends MetricsCard {
	public static final String ID = "EscortCommand";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int BLOCK_AMT = 3;
	private static final int UPGRADE_PLUS_BLOCK = 2;
	private static final int NEXT_BLOCK_AMT = 8;
	private static final int NEXT_UPGRADE_PLUS_BLOCK = 3;

	public EscortCommand() {
		super(ID, NAME, "chrono_images/cards/EscortCommand.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseBlock = BLOCK_AMT;
		this.baseMagicNumber = NEXT_BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

		this.baseBlock = this.baseMagicNumber;
		super.applyPowers();
		this.calculateCardDamage(null);

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, this.block), this.block, true, AbstractGameAction.AttackEffect.SHIELD));
		this.baseBlock = BLOCK_AMT;
		if (this.upgraded) { this.baseBlock += UPGRADE_PLUS_BLOCK; }
	}

	@Override
	public void applyPowers() {
		// Next turn's block
		this.baseBlock = this.baseMagicNumber;
		super.applyPowers();
		this.calculateCardDamage(null);
		if (this.isBlockModified) { this.isMagicNumberModified = true; }
		this.magicNumber = this.block;
		this.baseBlock = BLOCK_AMT;
		if (this.upgraded) { this.baseBlock += UPGRADE_PLUS_BLOCK; }

		// This turn's block
		this.block = this.baseBlock;
		super.applyPowers();
		this.calculateCardDamage(null);
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