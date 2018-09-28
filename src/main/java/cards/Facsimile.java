package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import chronomuncher.actions.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import com.badlogic.gdx.math.MathUtils;

import chronomuncher.cards.MetricsCard;
import basemod.helpers.TooltipInfo;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;
import chronomuncher.orbs.*;

import javafx.util.Pair;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Facsimile extends MetricsCard {
	public static final String ID = "Facsimile";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 2;
	private static final int RELIC_CAP = 3;
	private static final int RELIC_CAP_UPGRADE = 7;

	public static ArrayList<String> replicaStrings = new ArrayList<String>();
	private final ArrayList<Pair<String, Class>> relicList = new ArrayList<Pair<String, Class>>();

	public Facsimile() {
		super(ID, NAME, "images/cards/Facsimile.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = RELIC_CAP;
		this.magicNumber = this.baseMagicNumber;

		this.exhaust = true;

		relicList.add(new Pair("Blood Vial", 		UnlockedBlood.class));
		relicList.add(new Pair("StoneCalendar", 	UnlockedCalendar.class));
		relicList.add(new Pair("Carbonhydrate", 	UnlockedCarbon.class));
		relicList.add(new Pair("Bottled Flame", 	UnlockedFlame.class));
		relicList.add(new Pair("Bottled Lightning", UnlockedLightning.class));
		relicList.add(new Pair("Bottled Tornado", 	UnlockedTornado.class));
		relicList.add(new Pair("Mummified Hand", 	UnlockedHand.class));
		relicList.add(new Pair("Ice Cream", 		UnlockedIceCream.class));
		relicList.add(new Pair("Medical Kit", 		UnlockedMedicine.class));
		relicList.add(new Pair("Mercury Hourglass", UnlockedMercury.class));
		relicList.add(new Pair("Cryopreserver", 	UnlockedNitrogen.class));
		relicList.add(new Pair("Orichalcum", 		UnlockedOrichalcum.class));
		relicList.add(new Pair("Thread and Needle", UnlockedThread.class));
		relicList.add(new Pair("Bird Faced Urn", 	UnlockedUrn.class));
		relicList.add(new Pair("Chronometer", 		UnlockedPlans.class));
		relicList.add(new Pair("Chronograph", 		UnlockedPlans.class));
		relicList.add(new Pair("War Paint", 		UnlockedWarPaint.class));
		relicList.add(new Pair("Whetstone", 		UnlockedWhetstone.class));

		this.updateRelicDescription();
	}

	@Override
	public void triggerWhenCopied() {
		this.updateRelicDescription();
	}

	@Override
	public void update() {
		super.update();
		this.updateRelicDescription();
	}

   	public void updateRelicDescription() {
   		this.updateRelicList();

   		String plural = (this.replicaStrings.size() > 1) ? "s" : "";
   		String end = " NL Currently " + Integer.toString(this.replicaStrings.size()) + " relic" + plural + " can be replicated.";
   		if (this.replicaStrings.size() == 0) {
   			end = " NL Currrently you can't replicate any of your relics.";
   		}
	    
	    this.rawDescription = DESCRIPTION + end;	    	
      	this.initializeDescription();
   	}

   	public void updateRelicList() {
		this.replicaStrings.clear();

		if (AbstractDungeon.player == null) { return; }

	    for (AbstractRelic relic : AbstractDungeon.player.relics) {
			for (Pair<String, Class> replica : relicList) {
				if (replica.getKey() == relic.relicId) {
					if (replica.getKey() == "Chronometer" || replica.getKey() == "Chronograph") {
						this.replicaStrings.add("Plans from " + relic.name);
					} else {
						this.replicaStrings.add(relic.name);
					}
				}
			}
		}
   	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		this.updateRelicList();

		String relicsListed;
		if (this.replicaStrings.size() == 0) {
			relicsListed = "None of your relics can be replicated.";
		} else {
			relicsListed = String.join(", ", this.replicaStrings);
		}

		String amount = Integer.toString(this.magicNumber);

		ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();
		tips.add(new TooltipInfo("Replicate", "Replicas will be created for " + amount + " of the following relics: " + relicsListed));

	    return tips;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		ArrayList<Class> replicaClasses = new ArrayList<Class>();

	    for (AbstractRelic relic : p.relics) {
			for (Pair<String, Class> replica : relicList) {
				if (replica.getKey() == relic.relicId) {
					replicaClasses.add(replica.getValue());
				}
			}
		}

		int index = 0;
		ReplicaOrb orb;

		int limit = this.magicNumber;
		if (this.upgraded || limit > replicaClasses.size()) { limit = replicaClasses.size(); }

		for (int c = 0; c < limit; c++ ) {
			index = MathUtils.random(0,replicaClasses.size()-1);
			orb = null;

			try {
				Class rClass = replicaClasses.get(index);

				Class partypes[] = new Class[1];
				partypes[0] = Boolean.TYPE;
	            Object arglist[] = new Object[1];
	            arglist[0] = new Boolean(false); // Set to this.upgraded to spawn upgraded replicas instead

				Constructor constructor = rClass.getConstructor(partypes);
				orb = (ReplicaOrb)constructor.newInstance(arglist); 
			} catch(Throwable e) {			
				ChronoMod.log("Some sort of stack trace error.");
	            e.printStackTrace();
			}

			replicaClasses.remove(index);

			if (orb != null) {
	 				    AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(orb));
			}
	    }
	}

	@Override
	public AbstractCard makeCopy() {
		return new Facsimile();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(RELIC_CAP_UPGRADE);
   		   	initializeDescription();
			this.updateRelicDescription();
		}
	}
}