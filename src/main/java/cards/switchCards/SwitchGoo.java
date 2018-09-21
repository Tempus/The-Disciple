package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.SlowPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.cards.AbstractSelfSwitchCard;
import chronomuncher.actions.SwitchAction;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class SwitchGoo extends AbstractSelfSwitchCard {

	// switchList.add(new switchCard(String CardID, String switchID, Integer cost, Integer damage, Integer damageUp, Integer block, Integer blockUp, Integer magicNum, Integer magicNumUp, 
	// 					          CardType type, CardTarget target, boolean isMultiTarget, boolean isInnate, boolean exhaust, boolean isEthereal));

	public List<switchCard> switchListInherit = Arrays.asList(
		new AbstractSelfSwitchCard.switchCard("AcidicGoo", "ViscousGoo", 0, 3, 2, 0, 0, 2, 1, 
						          		AbstractCard.CardType.ATTACK, AbstractCard.CardTarget.ENEMY, false, false, false, false),

		new AbstractSelfSwitchCard.switchCard("ViscousGoo", "ThickGoo", 0, 3, 2, 0, 0, 2, 1, 
						          		AbstractCard.CardType.ATTACK, AbstractCard.CardTarget.ENEMY, false, false, false, false),

		new AbstractSelfSwitchCard.switchCard("ThickGoo", "AcidicGoo", 0, 6, 3, 0, 0, 1, 0, 
						          		AbstractCard.CardType.ATTACK, AbstractCard.CardTarget.ENEMY, false, false, false, false) );


	public SwitchGoo(String switchID) {
		super("Goo", "None", null, 0, "None", AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE, SwitchGoo.class);

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

	public SwitchGoo() { this(null); }

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

		switch (this.currentID) {
			case "AcidicGoo":
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
				break;
			case "ViscousGoo":
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
				break;
			case "ThickGoo":
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(m, p, new SlowPower(m, 0), 0, true, AbstractGameAction.AttackEffect.NONE));
				break;
		}

		AbstractDungeon.actionManager.addToBottom(new SwitchAction(this));
	}
}
