package chronomuncher.actions;

import chronomuncher.ChronoMod;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import java.util.ArrayList;

public class PlayLowerBlockFromDeckAction
  extends AbstractGameAction
{
  private int block = 1;
  private int amount = 1;
  
  public PlayLowerBlockFromDeckAction(AbstractCreature target, int amount, int block)
  {
    this.duration = Settings.ACTION_DUR_FAST;
    this.actionType = AbstractGameAction.ActionType.WAIT;
    this.source = AbstractDungeon.player;
    this.target = target;
    this.block = block;
    this.amount = amount;
  }
  
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_FAST)
    {
      if (AbstractDungeon.player.drawPile.size() == 0)
      {
        this.isDone = true;
        return;
      }
      else
      {
        ChronoMod.log ("Checking against " + Integer.toString(this.block) + " block. Player block is " + Integer.toString(AbstractDungeon.player.currentBlock));

        AbstractCard c;
        for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i++) {

          c = AbstractDungeon.player.drawPile.group.get(i);
          if (c != null) { 

            c.applyPowers();
            ChronoMod.log (c.name + " - Block: " + Integer.toString(c.block) + " baseBlock: " + Integer.toString(c.baseBlock));
            if (c.block < this.block && c.block > 0) {
              this.playCard(c);
              this.amount -= 1;
              
              if (this.amount == 0) {
                this.isDone = true; 
                return;
              }
            }

          } 
        }        
      }
      this.isDone = true;
    }
  }

  public void playCard(AbstractCard card) {
    AbstractDungeon.player.drawPile.group.remove(card);
    AbstractDungeon.getCurrRoom().souls.remove(card);
    card.freeToPlayOnce = true;
    AbstractDungeon.player.limbo.group.add(card);
    card.current_y = (-200.0F * Settings.scale);
    card.target_x = (Settings.WIDTH / 2.0F + 200.0F * Settings.scale);
    card.target_y = (Settings.HEIGHT / 2.0F);
    card.targetAngle = 0.0F;
    card.lighten(false);
    card.drawScale = 0.12F;
    card.targetDrawScale = 0.75F;
    if (!card.canUse(AbstractDungeon.player, (AbstractMonster)this.target))
    {
      AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
      AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
      
      AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
    }
    else
    {
      card.applyPowers();
      AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
      AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
      if (!Settings.FAST_MODE) {
        AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
      } else {
        AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
      }
    }
  }
}
