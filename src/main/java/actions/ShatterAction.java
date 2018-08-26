package chronomuncher.actions;

import java.util.Collections;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import chronomuncher.orbs.ReplicaOrb;

public class ShatterAction extends AbstractGameAction 
{

  private ReplicaOrb orb;


  public ShatterAction(ReplicaOrb orbToShatter)
  {
    this.duration = Settings.ACTION_DUR_FAST;
    this.actionType = AbstractGameAction.ActionType.BLOCK;
    this.orb = orbToShatter;
  }
  
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_FAST) {

      orb.onShatter();
      AbstractPlayer p = AbstractDungeon.player;

      // Rotate up the remaining orbs
      int index = p.orbs.indexOf(this.orb);

      AbstractOrb orbSlot = new EmptyOrbSlot();
      for (int i = index+1; i < p.orbs.size(); i++) {
        Collections.swap(p.orbs, i, i - 1);
      }
      p.orbs.set(p.orbs.size() - 1, orbSlot);
      for (int i = index; i < p.orbs.size(); i++) {
        ((AbstractOrb)p.orbs.get(i)).setSlot(i, p.maxOrbs);
      }

      AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(this.orb.cX, this.orb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
      CardCrawlGame.sound.play("ORB_FROST_EVOKE");
      CardCrawlGame.sound.play("RELIC_DROP_CLINK");
      CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");

      // Decrease the slots
      AbstractDungeon.player.decreaseMaxOrbSlots(1);

    }
    tickDuration();
  }
}
