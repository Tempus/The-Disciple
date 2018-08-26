package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;

import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.DamageInfo;

import basemod.ReflectionHacks;

import chronomuncher.ChronoMod;
import chronomuncher.cards.PrimeTime;

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
			if ( __instance.retain == false ) { 
			    __instance.costForTurn = __instance.cost;
			    __instance.isCostModifiedForTurn = false;			
			} else {
				if (AbstractDungeon.player.hasRelic("Runic Pyramid") 
					&& __instance.retain 
					&& (AbstractDungeon.player.hasRelic("Chronometer") || AbstractDungeon.player.hasRelic("Chronograph")) 
					&& AbstractDungeon.player.hand.contains(__instance)) {
					// Decrease cost here?
				    __instance.modifyCostForTurn(-1);
				    __instance.isCostModifiedForTurn = true;
				    __instance.retain = false;
				}
				if (__instance.cardID == "PrimeTime") {
				    __instance.damage = __instance.baseDamage * 2;
				    __instance.isDamageModified = true;

				    PrimeTime p = (PrimeTime)__instance;
				    p.wasRetained = true;
				}
			}
		}
	}
}