package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

// Created a field in the class to count how much the cards retain
@SpirePatch(clz=AbstractCard.class, method=SpirePatch.CLASS)
public class RetainedForField
{
    public static SpireField<Integer> retainedFor = new SpireField(() -> 0);
}
