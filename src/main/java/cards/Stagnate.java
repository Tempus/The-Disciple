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
import com.megacrit.cardcrawl.powers.DrawDownPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.SleepPower;


public class Stagnate extends MetricsCard {
	public static final String ID = "Stagnate";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 3;
	private static final int CARD_DRAW_DOWN = 1;
	private static final int SLEEP_TURNS = 3;

	public Stagnate() {
		super(ID, NAME, "images/cards/Stagnate.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);

		this.baseMagicNumber = SLEEP_TURNS;
		this.magicNumber = this.baseMagicNumber;

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new DrawDownPower(p, this.magicNumber), this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,p,new SleepPower(m, this.magicNumber),this.magicNumber));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Stagnate();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBaseCost(2);
		}
	}
}