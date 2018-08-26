package chronomuncher.patches;

import java.lang.reflect.Field;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.screens.stats.CharStat;

import basemod.BaseMod;

import com.badlogic.gdx.math.MathUtils;
import chronomuncher.ChronoMod;

public class CharSelectPatch {

	@SpirePatch(cls="com.megacrit.cardcrawl.screens.charSelect.CharacterOption", method="updateHitbox")
	public static class updateHitbox {
		@SpireInsertPatch(rloc=43)
		public static void Insert(Object __obj_instance) {
			Field maxAscensionLevel;
			Field isAscensionModeUnlockedField;
			try {
				Prefs pref = null;
				AbstractPlayer.PlayerClass chosenClass = CardCrawlGame.chosenCharacter;
				if (chosenClass.toString() == "CHRONO_CLASS") {
					pref = SaveHelper.getPrefs("CHRONO_CLASS");
					CardCrawlGame.sound.playA("SLIME_ATTACK_2", MathUtils.random(-0.2F, 0.2F));
					CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
			        
					// Can remove everything below here if I just localvar pref....?
			        CharacterOption obj = (CharacterOption) __obj_instance;
					maxAscensionLevel = obj.getClass().getDeclaredField("maxAscensionLevel");
					maxAscensionLevel.setAccessible(true);
					CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel = pref.getInteger("ASCENSION_LEVEL", 1);

					// isAscensionModeUnlockedField = CardCrawlGame.mainMenuScreen.charSelectScreen.getClass().getDeclaredField("isAscensionModeUnlocked");
					// isAscensionModeUnlockedField.setAccessible(true);
		   //          isAscensionModeUnlockedField.set(obj, true);

					ChronoMod.log(Integer.toString(CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel));
		            if (20 < CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel) {
		                CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel = 20;
		            }
		            maxAscensionLevel.set(obj, pref.getInteger("ASCENSION_LEVEL", 1));
		            if (20 < maxAscensionLevel.getInt(obj)) {
		            	maxAscensionLevel.set(obj, 20);
		            }
		            int ascensionLevel = CardCrawlGame.mainMenuScreen.charSelectScreen.ascensionLevel;
		            if (ascensionLevel > maxAscensionLevel.getInt(obj)) {
		                ascensionLevel = maxAscensionLevel.getInt(obj);
		            }
		            if (ascensionLevel > 0) {
		                CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = CharacterSelectScreen.A_TEXT[ascensionLevel - 1];
		            }
		            else {
		                CardCrawlGame.mainMenuScreen.charSelectScreen.ascLevelInfoString = "";
		            }
				}
			} catch (NoSuchFieldException | IllegalAccessException | SecurityException | IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	@SpirePatch(cls="com.megacrit.cardcrawl.screens.stats.StatsScreen", method="getVictory")
	public static class getVictory {

		public static int Postfix(int __result, AbstractPlayer.PlayerClass c)
		{
			if (BaseMod.isBaseGameCharacter(c)) {
				return __result;
			}

			CharStat stat = BaseMod.playerStatsMap.get(c);
			if (stat != null) {
				return stat.getVictoryCount();
			}
			return 0;
		}
	}
}