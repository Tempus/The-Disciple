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

import java.util.Iterator;

import chronomuncher.actions.RestoreSelfRetainedCardsAction;
import chronomuncher.patches.Enum;
import chronomuncher.ChronoMod;

@SpirePatch(clz=HandCardSelectScreen.class, method="open", paramtypez={String.class,int.class,boolean.class,boolean.class,boolean.class,boolean.class,boolean.class })
public class RetainCardSelectPatch
{
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
