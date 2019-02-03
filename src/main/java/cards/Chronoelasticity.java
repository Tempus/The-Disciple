package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.HastePower;

public class Chronoelasticity extends MetricsCard {
	public static final String ID = "Chronoelasticity";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = -1;
	private static final int SLOW = 5;
	private static final int SLOW_UPGRADE = 3;

	public Chronoelasticity() {
		super(ID, NAME, "chrono_images/cards/Chronoelasticity.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = SLOW;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    
	   	int effect = EnergyPanel.totalCount;
	    if (p.hasRelic("Chemical X"))
	    {
	      effect += 2;
	      p.getRelic("Chemical X").flash();
	    }
	    if (effect > 0)
	    {
        	AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-ELASTIC"));
	      for (int i = 0; i < effect; i++)
	      {
		    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
		      if (!mo.isDeadOrEscaped()) {
		        AbstractDungeon.actionManager.addToBottom(
		          new ApplyPowerAction(mo, p, new SlowPower(mo, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
		      }
		    }
	          	AbstractDungeon.actionManager.addToBottom(
	            	new ApplyPowerAction(p, p, new HastePower(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
	      }
	      if (!this.freeToPlayOnce) {
	        p.energy.use(EnergyPanel.totalCount);
	      }
	    }
   	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(SLOW_UPGRADE);
		}
	}
}