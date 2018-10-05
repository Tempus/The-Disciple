package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.TransformCardPermanently;
import chronomuncher.cards.WatchCommand;
import chronomuncher.powers.MalleablePlayerPower;
import chronomuncher.cards.AbstractSwitchCard;

public class NaturalGuard extends AbstractSwitchCard {
	public static final String ID = "NaturalGuard";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	


	private static final int COST = 2;
	private static final int BLOCK_AMT = 20;
	private static final int UPGRADE_PLUS_BLOCK = 10;

	public NaturalGuard() {
		super(ID, NAME, "chrono_images/cards/NaturalGuard.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF, WatchCommand.class);

		this.baseMagicNumber = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MalleablePlayerPower(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.SHIELD));

		AbstractDungeon.actionManager.addToBottom(new TransformCardPermanently(p, this, new WatchCommand()));
	}

	@Override
	public AbstractCard makeCopy() {
		return new NaturalGuard();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}
}