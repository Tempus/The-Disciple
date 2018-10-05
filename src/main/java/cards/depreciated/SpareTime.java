package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


// ALTERNATE NAME - Fast Forward (I think Fast forward is more apt but already have art for Spare Time)
public class SpareTime extends MetricsCard {
	public static final String ID = "SpareTime";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = -1;
	private static final int DRAW_UPGRADE = 1;

	public SpareTime() {
		super(ID, NAME, "chrono_images/cards/SpareTime.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);

		this.baseMagicNumber = DRAW_UPGRADE;
		this.magicNumber = this.baseMagicNumber;

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    
	   	int effect = EnergyPanel.totalCount;
	    if (p.hasRelic("Chemical X"))
	    {
	      effect += 2;
	      p.getRelic("Chemical X").flash();
	    }
	    if (this.upgraded) {
	      effect += this.magicNumber;
	    }
	    if (effect > 0)
	    {
	      for (int i = 0; i < effect; i++)
	      {
			AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(m, false));
	      }
	      if (!this.freeToPlayOnce) {
	        p.energy.use(EnergyPanel.totalCount);
	      }
	    }
   	}

	@Override
	public AbstractCard makeCopy() {
		return new SpareTime();
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