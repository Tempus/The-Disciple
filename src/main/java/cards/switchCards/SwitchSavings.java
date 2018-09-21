package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.cards.AbstractSelfSwitchCard;
import chronomuncher.actions.SwitchAction;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class SwitchSavings extends AbstractSelfSwitchCard {

	// switchList.add(new switchCard(String CardID, String switchID, Integer cost, Integer damage, Integer damageUp, Integer block, Integer blockUp, Integer magicNum, Integer magicNumUp, 
	// 					          CardType type, CardTarget target, boolean isMultiTarget, boolean isInnate, boolean exhaust, boolean isEthereal));

	public List<switchCard> switchListInherit = Arrays.asList(
		new AbstractSelfSwitchCard.switchCard("SpringForward", "Fallback", 1, 0, 0, 0, 0, 2, 0, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false, true, false, false),

		new AbstractSelfSwitchCard.switchCard("Fallback", "SpringForward", 0, 0, 0, 0, 0, 2, 1, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false, false, true, false) );


	public SwitchSavings(String switchID) {
		super("DaylightSavings", "None", null, 0, "None", AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE, SwitchSavings.class);

		if (switchID == null) {
			switchID = switchListInherit.get(new Random().nextInt(switchListInherit.size())).cardID;
		}
		
		this.switchList = switchListInherit;
		if (this.currentID != null) {
			this.switchTo(this.currentID);
		} else {
			this.switchTo(switchID);
		}
	}

	public SwitchSavings() { this(null); }

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		switch (this.currentID) {
			case "SpringForward":
			    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber, false)); 
				if (this.upgraded) { AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(p, 1)); }
				break;
			case "Fallback":
				AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
				break;
		}

		AbstractDungeon.actionManager.addToBottom(new SwitchAction(this));
	}
}
