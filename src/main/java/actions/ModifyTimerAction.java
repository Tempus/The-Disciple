package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import chronomuncher.orbs.ReplicaOrb;

public class ModifyTimerAction
  extends AbstractGameAction
{
  public ReplicaOrb orb;
  public int amount;
  public float secondaryTimer;

  public ModifyTimerAction(ReplicaOrb orb, int modifier)
  {
    this.duration = Settings.ACTION_DUR_FASTER;
    this.orb = orb;
    this.amount = modifier;
  }
  
  public void update()
  {
    if (this.amount == 0)    { this.isDone = true; return; }
    if (this.orb.timer == 0) { this.isDone = true; return; }

    if (this.duration == Settings.ACTION_DUR_FASTER) {
      this.orb.activateEffect();
      if (this.amount > 0) {
        CardCrawlGame.sound.play("CHRONO-SPEEDUP", 0.2F);
        this.orb.timer++;
        this.orb.timeElapsed--;
        this.amount--;
      }
      else {
        CardCrawlGame.sound.play("CHRONO-SLOWDOWN", 0.2F);
        this.orb.decrementTimer();
        this.amount++;
      }
    }

    tickDuration();
    if (this.isDone && this.amount !=0) { 
      this.isDone = false;
      this.duration = Settings.ACTION_DUR_FASTER;
    }
    return;
  }
}
