package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.cards.AbstractSelfSwitchCard;
import chronomuncher.actions.SwitchAction;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class SwitchExoVibe extends AbstractSelfSwitchCard {

	// switchList.add(new switchCard(String CardID, String switchID, Integer cost, Integer damage, Integer damageUp, Integer block, Integer blockUp, Integer magicNum, Integer magicNumUp, 
	// 					          CardType type, CardTarget target, boolean isMultiTarget, boolean isInnate, boolean exhaust, boolean isEthereal));

	public List<switchCard> switchListInherit = Arrays.asList(
		new AbstractSelfSwitchCard.switchCard("Exocoating", "CoatedVibrissa", 1, 0, 0, 0, 0, 3, 1, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false, false, false, false),

		new AbstractSelfSwitchCard.switchCard("CoatedVibrissa", "Exocoating", 1, 0, 0, 10, 3, 0, 0, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false, false, false, false) );


	public SwitchExoVibe(String switchID) {
		super("ExoVibe", "None", null, 0, "None", AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.NONE, SwitchExoVibe.class);

		if (switchID == null) {
			switchID = switchListInherit.get(new Random().nextInt(switchListInherit.size())).cardID;
		}

		this.switchList = switchListInherit;
		this.switchTo(switchID);
	}

	public SwitchExoVibe() { this(null); }

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		switch (this.currentID) {
			case "Exocoating":
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber, true, AbstractGameAction.AttackEffect.SHIELD));
				break;
			case "CoatedVibrissa":
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
				break;
		}

		AbstractDungeon.actionManager.addToBottom(new SwitchAction(this));
	}
}
