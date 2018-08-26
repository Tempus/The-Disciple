package chronomuncher.patches;

import com.badlogic.gdx.graphics.Color;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import com.megacrit.cardcrawl.screens.runHistory.TinyCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class TinyCardPatch {

	@SpirePatch(cls = "com.megacrit.cardcrawl.screens.runHistory.TinyCard", method="getIconBackgroundColor")
	public static class getIconBackgroundColor {
	
		public static Color Replace(TinyCard __instance, AbstractCard card)
		{
		    if (card.color == CardColor.RED) { 
		      return new Color(-719117313); 
		    }
		    if (card.color == CardColor.GREEN) {
		      return new Color(1792302079);
		    }
		    if (card.color == CardColor.BLUE) {
		      return new Color(1774256127);
		    }
		    if (card.color == CardColor.COLORLESS) {
		      return new Color(2054847231);
		    }
		    if (card.color == CardColor.CURSE) {
		      return new Color(993541375);
		    }
		    if (card.color == Enum.BRONZE) {
		    	return ChronoMod.BRONZE;
		    }
		    return new Color(-9849601);
		}
	}

	@SpirePatch(cls = "com.megacrit.cardcrawl.screens.runHistory.TinyCard", method="getIconDescriptionColor")
	public static class getIconDescriptionColor {
	
		public static Color Replace(TinyCard __instance, AbstractCard card)
		{
		    if (card.color == CardColor.RED) {
		      return new Color(1613902591);
		    }
		    if (card.color == CardColor.GREEN) {
		      return new Color(894908927);
		    }
		    if (card.color == CardColor.BLUE) {
		      return new Color(1417522687);
		    }
		    if (card.color == CardColor.COLORLESS) {
		      return new Color(1077952767);
		    }
		    if (card.color == CardColor.CURSE) {
		      return new Color(454761471);
		    }
		    if (card.color == Enum.BRONZE) {
		    	return ChronoMod.DARKBRONZE;
		    }
		    return new Color(-1303806465);
		}
	}

}