package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import javassist.CannotCompileException;
import javassist.CtBehavior;


import java.util.ArrayList;

import chronomuncher.patches.Enum;
import chronomuncher.actions.SpikedShellTriggerAction;

@SpirePatch(clz=DiscardAtEndOfTurnAction.class, method="update")
public class SpikedShellTriggerPatch
{
	@SpireInsertPatch( rloc = 30 )
    public static void Insert(DiscardAtEndOfTurnAction self) {
    	AbstractDungeon.actionManager.addToTop(new SpikedShellTriggerAction());
    }
}

