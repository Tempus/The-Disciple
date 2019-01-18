package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ConfuseHandAction extends AbstractGameAction {
  
  public ConfuseHandAction() {}
    
  public void update()
  {
    int newCost = 0;
    for (AbstractCard c : AbstractDungeon.player.hand.group) {
      if (c.cost >= 0)
      {
        newCost = AbstractDungeon.cardRandomRng.random(3);
        if (c.cost != newCost)
        {
          c.cost = newCost;
          c.costForTurn = c.cost;
          c.isCostModified = true;
        }
      }
    }

    this.isDone = true;
    return;
  }
}

