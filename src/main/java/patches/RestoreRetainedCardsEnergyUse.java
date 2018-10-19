package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;

import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.common.MonsterStartTurnAction;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;

import basemod.ReflectionHacks;

import chronomuncher.ChronoMod;
import chronomuncher.cards.PrimeTime;
import chronomuncher.actions.EndTurnResetAttributesAction;

public class RestoreRetainedCardsEnergyUse {

	@SpirePatch(cls = "com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction", method="update")
	public static class Update {
	
		@SpireInsertPatch( rloc = 7, localvars={"e"} )
		public static void Insert(RestoreRetainedCardsAction __instance, AbstractCard e)
		{
			if ( AbstractDungeon.player.hasRelic("Chronometer") || AbstractDungeon.player.hasRelic("Chronograph")) {
	            e.modifyCostForTurn(-1);
			}
    		if ( AbstractDungeon.player.hasRelic("Chronograph")) {
		      if (e.canUpgrade())
		      {
		        e.superFlash();
		        e.upgrade();
		        e.applyPowers();
		      }
  			}
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method="resetAttributes")
	public static class resetAttributes {
	
		public static void Replace(AbstractCard __instance)
		{
		    __instance.block = __instance.baseBlock;
		    __instance.isBlockModified = false;
		    __instance.damage = __instance.baseDamage;
		    __instance.isDamageModified = false;
		    __instance.magicNumber = __instance.baseMagicNumber;
		    __instance.isMagicNumberModified = false;
		    __instance.damageTypeForTurn = (DamageInfo.DamageType)ReflectionHacks.getPrivate(__instance, AbstractCard.class, "damageType");

			if (AbstractDungeon.player != null) {

			    if (AbstractDungeon.player.hasPower("Equilibrium") 
			    	&& AbstractDungeon.player.hand.contains(__instance)
					&& (AbstractDungeon.player.hasRelic("Chronometer") || AbstractDungeon.player.hasRelic("Chronograph")) 
			    	) {
					__instance.modifyCostForTurn(-1);
					__instance.isCostModifiedForTurn = true;
		    		if ( AbstractDungeon.player.hasRelic("Chronograph")) {
				      if (__instance.canUpgrade())
				      {
				        __instance.superFlash();
				        __instance.upgrade();
				        __instance.applyPowers();
				      }
		  			}
					return;
			    }

				if ( __instance.retain == false) { 
					if (!AbstractDungeon.player.hasRelic("Runic Pyramid") || !AbstractDungeon.player.hand.contains(__instance)) {
						// ChronoMod.log(__instance.cardID + "'s cost was reset, no Pyramid or not in hand.");
					    __instance.costForTurn = __instance.cost;
					    __instance.isCostModifiedForTurn = false;			
					}
				} else {
					if (AbstractDungeon.player.hasRelic("Runic Pyramid") 
						&& __instance.retain 
						&& (AbstractDungeon.player.hasRelic("Chronometer") || AbstractDungeon.player.hasRelic("Chronograph")) 
						&& AbstractDungeon.player.hand.contains(__instance)) {
						// Decrease cost here?
						// ChronoMod.log(__instance.cardID + " was retained with Runic Pyramid active, and is in your hand.");
					    __instance.modifyCostForTurn(-1);
					    __instance.isCostModifiedForTurn = true;
					    __instance.retain = false;
					} 
					// else {
					// 	ChronoMod.log(__instance.cardID + " did not qualify for cost decrease. In hand is " + Boolean.toString(AbstractDungeon.player.hand.contains(__instance)));
					// }
				}
			}
		}
	}

	@SpirePatch(clz = AbstractRoom.class, method="endTurn")
	public static class endTurn {
	
		public static void Replace(AbstractCard __instance)
		{
			AbstractDungeon.player.applyEndOfTurnTriggers();
			
			AbstractDungeon.actionManager.addToBottom(new EndTurnResetAttributesAction());
			AbstractDungeon.actionManager.addToBottom(new ClearCardQueueAction());
			AbstractDungeon.actionManager.addToBottom(new DiscardAtEndOfTurnAction());

			AbstractDungeon.actionManager.addToBottom(new EndTurnAction());
			if (Settings.FAST_MODE) {
			  AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
			} else {
			  AbstractDungeon.actionManager.addToBottom(new WaitAction(1.2F));
			}
			AbstractDungeon.actionManager.addToBottom(new MonsterStartTurnAction());
			AbstractDungeon.actionManager.monsterAttacksQueued = false;

			AbstractDungeon.player.isEndingTurn = false;
		}
	}

	@SpirePatch(clz = RetainCardPower.class, method="atEndOfTurn")
	public static class atEndOfTurn {
	
		public static void Replace(AbstractPower __instance, boolean isPlayer)
		{
		    if ((isPlayer) && (!AbstractDungeon.player.hand.isEmpty())) {
				AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(__instance.owner, __instance.amount));
		    }
		}
	}
}