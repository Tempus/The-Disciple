package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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

public class PlayFreePowersAction
  extends AbstractGameAction
{
  private boolean includeDiscard;
  
  public PlayFreePowersAction(boolean includeDiscard)
  {
    this.duration = Settings.ACTION_DUR_FAST;
    this.actionType = AbstractGameAction.ActionType.WAIT;
    this.source = AbstractDungeon.player;
    this.target = AbstractDungeon.getCurrRoom().monsters.monsters.get(0);
    this.includeDiscard = includeDiscard;
  }
  
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_FAST)
    {

      for (AbstractCard card : AbstractDungeon.player.hand.getPowers().group) {
        AbstractDungeon.player.hand.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        this.playCard(card);
      }

      if (this.includeDiscard) {
        for (AbstractCard card : AbstractDungeon.player.discardPile.getPowers().group) {
          AbstractDungeon.player.discardPile.group.remove(card);
          AbstractDungeon.getCurrRoom().souls.remove(card);
          this.playCard(card);
        }
      }

      this.isDone = true;
    }
  }

  public void playCard(AbstractCard card) {
    card.freeToPlayOnce = true;
    AbstractDungeon.player.limbo.group.add(card);

    card.current_y = (-200.0F * Settings.scale);
    card.target_x = (Settings.WIDTH / 2.0F + 200.0F * Settings.scale);
    card.target_y = (Settings.HEIGHT / 2.0F);
    card.targetAngle = 0.0F;
    card.lighten(false);
    card.drawScale = 0.12F;
    card.targetDrawScale = 0.75F;

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
