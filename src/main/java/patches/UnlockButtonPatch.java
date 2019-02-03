package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.megacrit.cardcrawl.core.Settings;
import chronomuncher.patches.Enum;
import chronomuncher.ui.UnlockButton;

import basemod.BaseMod;
import basemod.ReflectionHacks;

public class UnlockButtonPatch {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("UnlockButton");
    public static final String[] TEXT = uiStrings.TEXT;

	@SpirePatch(clz = CharacterSelectScreen.class, method="initialize")
	public static class initUnlockButton {
		
		public static UnlockButton button;

		public static void Postfix(CharacterSelectScreen __instance)
		{
			button = new UnlockButton(Settings.WIDTH / 2.0F - 450.0F * Settings.scale, 44.0F * Settings.scale, ImageMaster.loadImage("chrono_images/ui/UnlockButton.png"));
		}
	}

	@SpirePatch(clz = CharacterSelectScreen.class, method="render")
	public static class renderUnlockButton {
		public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
			initUnlockButton.button.render(sb);
		}
	}

	@SpirePatch(clz = CharacterSelectScreen.class, method="update")
	public static class updateUnlockButton {
		public static void Postfix(CharacterSelectScreen __instance) {

	        initUnlockButton.button.hidden = true;

	        for (CharacterOption o : __instance.options)
	        {
	          if (o.selected && o.name == UnlockButtonPatch.TEXT[0])
	          {
		        int asc = (int)ReflectionHacks.getPrivate(o, o.getClass(), "maxAscensionLevel");
		        if (asc == 20) {
	            	initUnlockButton.button.disabled = true;
		        }
	            initUnlockButton.button.hidden = false;
	          }
	        }

			initUnlockButton.button.update();
		}
	}
}