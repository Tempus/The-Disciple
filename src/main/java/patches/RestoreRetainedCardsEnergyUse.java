package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
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
import chronomuncher.patches.RetainedForField;

public class RestoreRetainedCardsEnergyUse {

	public static void lowerCostFromRetain(AbstractCard c) {
		c.modifyCostForTurn(-1);
		int kept = RetainedForField.retainedFor.get(c);
		RetainedForField.retainedFor.set(c, kept+1);
		c.isCostModifiedForTurn = true;
	}

	public static void upgradeFromChronograph(AbstractCard c) {
		if ( AbstractDungeon.player.hasRelic("Chronograph")) {
		  if (c.canUpgrade())
		  {
			c.superFlash();
			c.upgrade();
			c.applyPowers();
		  }
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method="resetAttributes")
	public static class resetAttributes {
	
		public static SpireReturn Prefix(AbstractCard __instance)
		{
			// Check required for Compendium
			if (AbstractDungeon.player != null) {

				// Duplicated because patches suck
				__instance.block = __instance.baseBlock;
				__instance.isBlockModified = false;
				__instance.damage = __instance.baseDamage;
				__instance.isDamageModified = false;
				__instance.magicNumber = __instance.baseMagicNumber;
				__instance.isMagicNumberModified = false;
				__instance.damageTypeForTurn = (DamageInfo.DamageType)ReflectionHacks.getPrivate(__instance, AbstractCard.class, "damageType");

				// We have a discounting relic, and the card is in the player's hand, and it should be discounted (retain or equilibrium)
				if ((AbstractDungeon.player.hasRelic("Chronometer") || AbstractDungeon.player.hasRelic("Chronograph")) 
					&& AbstractDungeon.player.hand.contains(__instance)
					&& ((__instance.retain == true) || AbstractDungeon.player.hasPower("Equilibrium"))) {
					// lower costs and such
					RestoreRetainedCardsEnergyUse.lowerCostFromRetain(__instance);
					RestoreRetainedCardsEnergyUse.upgradeFromChronograph(__instance);
					return SpireReturn.Return(null);
				}

				// The card has previously been discounted, and should not be discounted
				// This only happens if it is in your hand, you have runic pyramid, and it's been retained before
				else if (AbstractDungeon.player.hand.contains(__instance)
					&& AbstractDungeon.player.hasRelic("Runic Pyramid")
					&& RetainedForField.retainedFor.get(__instance) > 0) {
					// Just leave kthx
					return SpireReturn.Return(null);
				}

				// No discounting relic exists, or it shouldn't be discounted
				else {
					RetainedForField.retainedFor.set(__instance, 0);
					return SpireReturn.Continue();
				}
			}

			return SpireReturn.Continue();
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
	
		public static SpireReturn Prefix(AbstractPower __instance, boolean isPlayer)
		{
			if ((isPlayer) && (!AbstractDungeon.player.hand.isEmpty())) {
				AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(__instance.owner, __instance.amount));
			}
			return SpireReturn.Return(null);
		}
	}
}