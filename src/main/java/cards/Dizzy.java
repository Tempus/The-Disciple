package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.ConfuseHandAction;

public class Dizzy extends AbstractSelfRetainingCard {
	public static final String ID = "Dizzy";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = -2;

	public Dizzy() {
		super(ID, NAME, "chrono_images/cards/BlueShift.png", COST, DESCRIPTION, AbstractCard.CardType.CURSE,
				AbstractCard.CardColor.CURSE, AbstractCard.CardRarity.CURSE, AbstractCard.CardTarget.NONE);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m)
	{
	  if (p.hasRelic("Blue Candle")) {
	    useBlueCandle(p);
	  } else {
	    AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
	  }
	}

  	public void triggerWhenDrawn()
  	{
  	  AbstractDungeon.actionManager.addToBottom(new ConfuseHandAction());
  	}

	@Override
	public void upgrade() {}
}