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
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import chronomuncher.actions.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import com.badlogic.gdx.math.MathUtils;

import chronomuncher.cards.MetricsCard;
import basemod.helpers.TooltipInfo;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;
import chronomuncher.orbs.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class Facsimile extends MetricsCard {
	public static final String ID = "Facsimile";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	private static final int COST = -1;
	private static final int RELIC_CAP = 3;
	private static final int RELIC_CAP_UPGRADE = 7;

	public static ArrayList<String> replicaStrings = new ArrayList<String>();
	private final ArrayList<String> relicListKey = new ArrayList<String>();
	private final ArrayList<Class>  relicListVal = new ArrayList<Class>();

	public Facsimile() {
		super(ID, NAME, "chrono_images/cards/Facsimile.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = RELIC_CAP;
		this.magicNumber = this.baseMagicNumber;

		this.exhaust = true;

		relicListKey.add("Blood Vial");	
			relicListVal.add(UnlockedBlood.class);
		relicListKey.add("StoneCalendar");
			relicListVal.add(UnlockedCalendar.class);
		relicListKey.add("Paper Turtyl");
			relicListVal.add(UnlockedTurtyl.class);
		relicListKey.add("Bottled Flame");	
			relicListVal.add(UnlockedFlame.class);
		relicListKey.add("Bottled Lightning");
			relicListVal.add(UnlockedLightning.class);
		relicListKey.add("Bottled Tornado");
			relicListVal.add(UnlockedTornado.class);
		relicListKey.add("Mummified Hand");	
			relicListVal.add(UnlockedHand.class);
		relicListKey.add("Ice Cream");
			relicListVal.add(UnlockedIceCream.class);
		relicListKey.add("Medical Kit");
			relicListVal.add(UnlockedMedicine.class);
		relicListKey.add("Mercury Hourglass");
			relicListVal.add(UnlockedMercury.class);
		relicListKey.add("Cryopreserver");
			relicListVal.add(UnlockedNitrogen.class);
		relicListKey.add("Orichalcum");
			relicListVal.add(UnlockedOrichalcum.class);
		relicListKey.add("Thread and Needle"); 
			relicListVal.add(UnlockedThread.class);
		relicListKey.add("Bird Faced Urn"); 	
			relicListVal.add(UnlockedUrn.class);
		relicListKey.add("Metronome");	
			relicListVal.add(UnlockedPlans.class);
		relicListKey.add("War Paint");
			relicListVal.add(UnlockedWarPaint.class);
		relicListKey.add("Whetstone");
			relicListVal.add(UnlockedWhetstone.class);
		relicListKey.add("MawBank");
			relicListVal.add(UnlockedMawBank.class);
		relicListKey.add("Anchor");
			relicListVal.add(UnlockedAnchor.class);
		relicListKey.add("Astrolabe");
			relicListVal.add(UnlockedAstrolabe.class);
		relicListKey.add("Calling Bell");	
			relicListVal.add(UnlockedBell.class);
		relicListKey.add("Blue Candle");
			relicListVal.add(UnlockedMedicine.class);
		relicListKey.add("Bronze Scales");
			relicListVal.add(UnlockedScales.class);

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

   		String plural = (this.replicaStrings.size() > 1) ? EXTENDED_DESCRIPTION[0] : "";
   		String end = EXTENDED_DESCRIPTION[1] + Integer.toString(this.replicaStrings.size()) + EXTENDED_DESCRIPTION[2] + plural + EXTENDED_DESCRIPTION[3];
   		if (this.replicaStrings.size() == 0) {
   			end = EXTENDED_DESCRIPTION[4];
   		}
	    
	    this.rawDescription = DESCRIPTION + end;
	    if (this.upgraded) {
	    	this.rawDescription = UPGRADE_DESCRIPTION + end;
	    }	
      	this.initializeDescription();
   	}

   	public void updateRelicList() {
		this.replicaStrings.clear();

		if (AbstractDungeon.player == null) { return; }

	    for (AbstractRelic relic : AbstractDungeon.player.relics) {
			for (String relicKey : relicListKey) {
				if (relicKey == relic.relicId) {
					if (relicKey == "Metronome") {
						this.replicaStrings.add(EXTENDED_DESCRIPTION[5] + relic.name);
					} else if (relicKey == "Blue Candle") {
						this.replicaStrings.add(EXTENDED_DESCRIPTION[6] + relic.name);
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
			relicsListed = EXTENDED_DESCRIPTION[4];
		} else {
			relicsListed = String.join(", ", this.replicaStrings);
		}

		String amount = Integer.toString(EnergyPanel.totalCount);

		ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
				tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[7], EXTENDED_DESCRIPTION[8] + amount + EXTENDED_DESCRIPTION[9] + relicsListed));
			} else {
				tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[7], EXTENDED_DESCRIPTION[10] + relicsListed));
			}
		}
		
	    return tips;
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
	    	generateOrbs(effect);

	        if (!this.freeToPlayOnce) {
	            p.energy.use(EnergyPanel.totalCount);
	        }
	    }
	}

	public void generateOrbs(int amount) {
		ArrayList<Class> replicaClasses = new ArrayList<Class>();
		ArrayList<String> replicaNames = new ArrayList<String>();

	    for (AbstractRelic relic : AbstractDungeon.player.relics) {
			for (int i = 0; i < relicListKey.size(); i++) {
				if (relicListKey.get(i) == relic.relicId) {
					replicaClasses.add(relicListVal.get(i));
					replicaNames.add(relicListKey.get(i));
				}
			}
		}

		int index = 0;
		ReplicaOrb orb;

		int limit = amount;
		// if (this.upgraded || limit > replicaClasses.size()) { limit = replicaClasses.size(); }
		String chosenName;

		for (int c = 0; c < amount; c++ ) {
			orb = null;
			index = -1;
			chosenName = " ";

			// No orbs left case
			Class rClass;
			if (replicaClasses.size() == 0) {
				rClass = UnlockedRock.class;
			} else {
				index = MathUtils.random(0,replicaClasses.size()-1);
				rClass = replicaClasses.get(index);
				chosenName = replicaNames.get(index);
			}

			try {
				Class partypes[] = new Class[1];
				partypes[0] = Boolean.TYPE;
	            Object arglist[] = new Object[1];
	            boolean upgrade = false;

	            if (chosenName == "Chronograph" || chosenName == "Blue Candle") { upgrade = true; }
	            ChronoMod.log("Facsimile: created " + chosenName);

	            arglist[0] = new Boolean(upgrade); // Set to this.upgraded to spawn upgraded replicas instead

				Constructor constructor = rClass.getConstructor(partypes);
				orb = (ReplicaOrb)constructor.newInstance(arglist); 
			} catch(Throwable e) {			
				ChronoMod.log("Some sort of stack trace error.");
	            e.printStackTrace();
			}

			if (index != -1) {
				replicaClasses.remove(index);
				replicaNames.remove(index);
			}

			if (orb != null) {
	 			AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(orb));
			}
	    }
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.exhaust = false;
			upgradeName();
			upgradeMagicNumber(RELIC_CAP_UPGRADE);
   		   	initializeDescription();
			this.updateRelicDescription();
		}
	}
}