package chronomuncher.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import java.util.ArrayList;

import com.megacrit.cardcrawl.dungeons.*;
import chronomuncher.events.*;
import chronomuncher.patches.Enum;
import chronomuncher.ChronoMod;
import chronomuncher.vfx.OracleScreenEffect;
import chronomuncher.vfx.OracleStarEffect;
import chronomuncher.vfx.JumpingClockHeartEffect;

public class HeartPatch {
	@SpirePatch(clz = Cutscene.class, method = SpirePatch.CONSTRUCTOR)
	public static class addHeartPanels{
		public static void Postfix(Cutscene self, AbstractPlayer.PlayerClass chosenClass) {
	        if (chosenClass == Enum.CHRONO_CLASS) {
		    	ArrayList<CutscenePanel> panels = (ArrayList<CutscenePanel>)ReflectionHacks.getPrivate(self, Cutscene.class, "panels");
		    	panels.clear();

				ReflectionHacks.setPrivate(self, Cutscene.class, "bgImg", ImageMaster.loadImage("chrono_images/char/heart/goldBg.png"));

				panels.add(new CutscenePanel("chrono_images/char/heart/disciple1.png", "ORB_FROST_EVOKE"));
				panels.add(new CutscenePanel("chrono_images/char/heart/disciple2.png"));
				panels.add(new CutscenePanel("chrono_images/char/heart/disciple3.png"));				


		    }
		}
	}

	@SpirePatch(clz = VictoryScreen.class, method = "updateVfx")
	public static class addHeartVictoryEffects{
		private static float effectTimer = 0.0F;
		public static void Postfix(VictoryScreen self) {
			if (AbstractDungeon.player.chosenClass == Enum.CHRONO_CLASS) {

				ArrayList<AbstractGameEffect> effect = (ArrayList<AbstractGameEffect>)ReflectionHacks.getPrivate(self, VictoryScreen.class, "effect");

		        effect.add(new OracleScreenEffect());
		        effect.add(new OracleStarEffect());

			    addHeartVictoryEffects.effectTimer -= Gdx.graphics.getDeltaTime();
			    if (addHeartVictoryEffects.effectTimer < 0.0F)
			    {
			    	float randomX = MathUtils.random(0.1F, 0.9F);
      		        effect.add(new JumpingClockHeartEffect(randomX, MathUtils.random(0.05F, 0.35F), MathUtils.random(0.05F, 0.3F)));
			        addHeartVictoryEffects.effectTimer = 0.15F;
			    }
			}
		}
	}
}