package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import chronomuncher.actions.*;

import chronomuncher.cards.MetricsCard;
import basemod.helpers.TooltipInfo;
import java.util.ArrayList;
import java.util.List;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.orbs.*;

public class LockedMercury extends MetricsCard {
	public static final String ID = "LockedMercury";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = 1;
	public ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();

	public LockedMercury() {
		super(ID, NAME, "images/cards/LockedMercury.png", COST, DESCRIPTION, AbstractCard.CardType.POWER,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
    	this.tags.add(Enum.REPLICA_CARD);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedMercury(this.upgraded)));
   	}

	@Override
	public AbstractCard makeCopy() {
		return new LockedMercury();
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		this.tips.clear();
		
		if (!this.upgraded) {
			this.tips.add(new TooltipInfo("Mercury", "Deals #g3 damage to ALL enemies at the start of your turn. NL #pShatters #pin #b4 #pturns."));
		} else {
			this.tips.add(new TooltipInfo("Mercury+", "Deals #g5 damage to ALL enemies at the start of your turn. NL #pShatters #pin #b4 #pturns."));
		}

	    return this.tips;
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