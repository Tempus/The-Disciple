package chronomuncher.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import com.badlogic.gdx.Gdx;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.RestoreRetainedCardsAction;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.Iterator;

import chronomuncher.actions.RestoreSelfRetainedCardsAction;
import chronomuncher.patches.Enum;
import chronomuncher.ChronoMod;

public class RetainCardSelectPatch
{

	@SpirePatch(clz=HandCardSelectScreen.class, method="open", paramtypez={String.class,int.class,boolean.class,boolean.class,boolean.class,boolean.class,boolean.class })
	public static class HandCardSelectPatch { 
	    public static void Postfix(HandCardSelectScreen self, String msg, int amount, boolean anyNumber, boolean canPickZero, boolean forTransform, boolean forUpgrade, boolean upTo) {
	    	if (!anyNumber && canPickZero && !forTransform & !forUpgrade && upTo) {
	    		// We retainin'
		        CardGroup newHand = (CardGroup)ReflectionHacks.getPrivate(self, HandCardSelectScreen.class, "hand");

			    for (Iterator<AbstractCard> e = newHand.group.iterator(); e.hasNext();)
			    {
					AbstractCard c = (AbstractCard)e.next();
			        if (c.retain == true) {
			            AbstractDungeon.player.limbo.addToTop(c);
			            e.remove();
	   		        }
	    		}
				AbstractDungeon.actionManager.addToTop(new RestoreSelfRetainedCardsAction(AbstractDungeon.player.limbo));
	    		newHand.refreshHandLayout();
			    if (Settings.isControllerMode) {
			      Gdx.input.setCursorPosition((int)((AbstractCard)newHand.group.get(0)).hb.cX, (int)((AbstractCard)newHand.group.get(0)).hb.cY);
			    }
	       	}

	    	return;
	    }
	}

	// @SpirePatch(clz=RestoreRetainedCardsAction.class, method="update")
	// public static class RestoreRetainedCardsActionPatch { 
	// 	public static void Replace(RestoreRetainedCardsAction self) {
	// 	    CardGroup newHand = (CardGroup)ReflectionHacks.getPrivate(self, RestoreRetainedCardsAction.class, "group");

	// 	    self.isDone = true;
	// 	    for (Iterator<AbstractCard> c = newHand.group.iterator(); c.hasNext();)
	// 	    {
	// 	      AbstractCard e = (AbstractCard)c.next();
	// 	      if (e.retain)
	// 	      {
	// 	        AbstractDungeon.player.hand.addToTop(e);
	// 	        e.retain = false;
	// 	        c.remove();
	// 	      }
	// 	    }
	// 	    AbstractDungeon.player.hand.refreshHandLayout();
 //   		}	
	// }
}