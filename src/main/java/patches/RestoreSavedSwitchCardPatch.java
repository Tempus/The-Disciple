package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

import chronomuncher.patches.Enum;
import chronomuncher.cards.AbstractSelfSwitchCard;

@SpirePatch(clz=CardLibrary.class, method="getCopy", paramtypez={String.class,int.class,int.class })
public class RestoreSavedSwitchCardPatch
{
    public static AbstractCard Postfix(AbstractCard retVal, String key, int upgradeTime, int misc) {
    	if (retVal instanceof AbstractSelfSwitchCard) {
    		((AbstractSelfSwitchCard)retVal).switchTo(misc);
    	}
    	return retVal;
    }
}
