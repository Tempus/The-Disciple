package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

import chronomuncher.ChronoMod;

public class EndTurnResetAttributesAction extends AbstractGameAction
{
  public EndTurnResetAttributesAction() {}
    
  public void update()
  {
      for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
        c.resetAttributes();
      }
      for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
        c.resetAttributes();
      }
      for (AbstractCard c : AbstractDungeon.player.hand.group) {
        c.resetAttributes();
      }
      if (AbstractDungeon.player.hoveredCard != null) {
        AbstractDungeon.player.hoveredCard.resetAttributes();
      }
      this.isDone = true;
  }
}
