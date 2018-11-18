package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import java.util.Iterator;

public class RestoreSelfRetainedCardsAction extends AbstractGameAction
{
  private CardGroup group;
  
  public RestoreSelfRetainedCardsAction(CardGroup group)
  {
    setValues(AbstractDungeon.player, this.source, -1);
    this.group = group;
  }
  
  public void update()
  {
    this.isDone = true;
    for (Iterator<AbstractCard> c = this.group.group.iterator(); c.hasNext();)
    {
      AbstractCard e = (AbstractCard)c.next();
      AbstractDungeon.player.hand.addToTop(e);
      c.remove();
    }
    AbstractDungeon.player.hand.refreshHandLayout();
  }
}
