package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.orbs.ReplicaOrb;
import chronomuncher.patches.Enum;
import chronomuncher.cards.AbstractSelfSwitchCard;
import chronomuncher.actions.SwitchAction;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

public class SwitchSharpShooter extends AbstractSelfSwitchCard {

	// switchList.add(new switchCard(String CardID, String switchID, Integer cost, Integer damage, Integer damageUp, Integer block, Integer blockUp, Integer magicNum, Integer magicNumUp, 
	// 					          CardType type, CardTarget target, boolean isMultiTarget, boolean isInnate, boolean exhaust, boolean isEthereal));

	public List<switchCard> switchListInherit = Arrays.asList(
		new AbstractSelfSwitchCard.switchCard("FastForward", "ClockandLoad", 2, 0, 0, 0, 0, 1, 1, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.ENEMY, false, false, false, false),

		new AbstractSelfSwitchCard.switchCard("ClockandLoad", "FastForward", 1, 0, 0, 0, 0, 1, 1, 
						          		AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false, false, false, false) );


	public SwitchSharpShooter (String switchID) {
		super("SharpShooter", "None", null, 0, "None", AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.NONE, SwitchSharpShooter.class);

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

	public SwitchSharpShooter () { this(null); }

	@Override
	public void atTurnStart() {
		if (this.upgraded && this.currentID == "FastForward") {
			this.retain = true; }
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.purgeOnUse) { this.switchTo(switchID); }

		AbstractDungeon.actionManager.addToBottom(new SwitchAction(this));
		ReplicaOrb r;

		switch (this.currentID) {
			case "FastForward":
		        for (AbstractOrb o : p.orbs) {
		            if (o instanceof ReplicaOrb) {
		            	r = (ReplicaOrb)o;
		            	for (int i = 0; i < this.magicNumber; i++ ) {
		            		if (r.timer > 0) {
								AbstractDungeon.actionManager.addToBottom(new WaitAction(0.33F));
								AbstractDungeon.actionManager.addToBottom(new SFXAction("RELIC_DROP_MAGICAL", 0.75F));
								AbstractDungeon.actionManager.addToBottom(new PlayTopCardAction(m, false));
						        r.activateEffect();
						        r.decrementTimer();
							}		            		
		            	}
		        	}
		        }
				break;
			case "ClockandLoad":
		        for (AbstractOrb o : p.orbs) {
		            if (o instanceof ReplicaOrb) {
		            	r = (ReplicaOrb)o;
						AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_PLASMA_CHANNEL", 0.75F));
				        r.timer += this.magicNumber;
		        	}
		        }
				break;
		}
	}
}