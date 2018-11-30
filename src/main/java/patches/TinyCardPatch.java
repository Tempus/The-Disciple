package chronomuncher.patches;

import com.badlogic.gdx.graphics.Color;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import com.megacrit.cardcrawl.screens.runHistory.TinyCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class TinyCardPatch {

	@SpirePatch(cls = "com.megacrit.cardcrawl.screens.runHistory.TinyCard", method="getIconBackgroundColor")
	public static class getIconBackgroundColor {
	
		public static SpireReturn<Color> Prefix(TinyCard __instance, AbstractCard card)
		{
		    if (card.color == Enum.CHRONO_GOLD) {
		    	return SpireReturn.Return(ChronoMod.DARKCHRONO_GOLD);
		    }
		    return SpireReturn.Continue(); 
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.screens.runHistory.TinyCard", method="getIconDescriptionColor")
	public static class getIconDescriptionColor {
	
		public static SpireReturn<Color> Prefix(TinyCard __instance, AbstractCard card)
		{
		    if (card.color == Enum.CHRONO_GOLD) {
		    	return SpireReturn.Return(ChronoMod.DARKCHRONO_GOLD);
		    }
		    return SpireReturn.Continue(); 
		}
	}

}