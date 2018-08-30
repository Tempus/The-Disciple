package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.PowerTip;
import java.util.ArrayList;
import java.util.List;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import basemod.ReflectionHacks;
import basemod.helpers.TooltipInfo;

import chronomuncher.ChronoMod;

@SpirePatch(cls="com.megacrit.cardcrawl.screens.SingleCardViewPopup", method="renderTips")
public class SingleCardViewTipsPatch {

  @SpireInsertPatch(rloc=11, localvars={"t"})
  public static void Insert(SingleCardViewPopup __this, SpriteBatch sb, @ByRef ArrayList<PowerTip>[] t)
  {
    // Dumb reflection shit because private variables are pointless
    AbstractCard acard = (AbstractCard)ReflectionHacks.getPrivate(__this, SingleCardViewPopup.class, "card");

    // Custom things here
    if (acard instanceof CustomCard) {
      CustomCard card = (CustomCard)acard;
      List<TooltipInfo> tooltips = card.getCustomTooltips();
      if (tooltips != null) {
          for (TooltipInfo tooltip : tooltips) {
              t[0].add(new PowerTip(tooltip.title, tooltip.description));
          }
      }
    }
  }

}