package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.beyond.SpireHeart;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

import chronomuncher.patches.Enum;

@SpirePatch(
        clz=SpireHeart.class,
        method="buttonEffect"
)
public class deathTextPatch
{
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(SpireHeart __instance, int buttonPressed) {
        if (AbstractDungeon.player.chosenClass == Enum.CHRONO_CLASS) {
            __instance.roomEventText.updateBodyText("NL It seems like your time has run out... once again.");
        }
    }

    public static class Locator extends SpireInsertLocator
    {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(RoomEventDialog.class, "updateDialogOption");

            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}