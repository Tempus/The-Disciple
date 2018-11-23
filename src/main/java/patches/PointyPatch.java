package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import com.megacrit.cardcrawl.monsters.city.BanditPointy;
import java.util.ArrayList;

import chronomuncher.patches.Enum;
import chronomuncher.orbs.ReplicaOrb;

@SpirePatch(clz=BanditPointy.class, method="takeTurn")
public class PointyPatch
{
    public static void Prefix(BanditPointy __instance) {
    	if (__instance.nextMove == -2) {
    		SpireReturn.Return(null);
    	}
    	SpireReturn.Continue();
    }
}