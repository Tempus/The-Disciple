package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

import chronomuncher.patches.Enum;
import chronomuncher.orbs.ReplicaOrb;

@SpirePatch(clz=AbstractPlayer.class, method="applyStartOfTurnPostDrawRelics")
public class StartOfTurnPostDrawOrbsPatch
{
    public static void Postfix(AbstractPlayer __instance) {
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof ReplicaOrb) {
                ReplicaOrb u = (ReplicaOrb)o;
                u.atTurnStartPostDraw();
            }
        }    
    }
}
