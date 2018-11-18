package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import chronomuncher.actions.ChronoChannelAction;
import chronomuncher.orbs.*;
import java.util.ArrayList;
import java.util.Random;
import java.lang.reflect.*;

import chronomuncher.ChronoMod;

public class RandomReplicaAction
  extends AbstractGameAction
{
  private AbstractOrb orbType;
  private boolean autoEvoke = false;
  
  public RandomReplicaAction()
  {
    this.duration = Settings.ACTION_DUR_FAST;
  }
  
  public void update()
  {
    ArrayList<Class> choices = new ArrayList();

    choices.add(UnlockedAnchor.class);
    choices.add(UnlockedAstrolabe.class);
    choices.add(UnlockedCalendar.class);
    choices.add(UnlockedTurtyl.class);
    choices.add(UnlockedFlame.class);
    choices.add(UnlockedHand.class);
    choices.add(UnlockedIceCream.class);
    choices.add(UnlockedLightning.class);
    choices.add(UnlockedMawBank.class);
    choices.add(UnlockedMedicine.class);
    choices.add(UnlockedMercury.class);
    choices.add(UnlockedNitrogen.class);
    choices.add(UnlockedOrichalcum.class);
    choices.add(UnlockedPlans.class);
    choices.add(UnlockedRock.class);
    choices.add(UnlockedScales.class);
    choices.add(UnlockedThread.class);
    choices.add(UnlockedTornado.class);
    choices.add(UnlockedWarPaint.class);

    ReplicaOrb orb = null;
    try {
      Class rClass = choices.get(new Random().nextInt(choices.size()));

      Class partypes[] = new Class[1];
      partypes[0] = Boolean.TYPE;
            Object arglist[] = new Object[1];
            arglist[0] = new Boolean(false); // Set to this.upgraded to spawn upgraded replicas instead

      Constructor constructor = rClass.getConstructor(partypes);
      orb = (ReplicaOrb)constructor.newInstance(arglist); 
    } catch(Throwable e) {      
      ChronoMod.log("Some sort of stack trace error.");
            e.printStackTrace();
    }

    AbstractDungeon.actionManager.addToTop(new ChronoChannelAction(orb));

    this.isDone = true;
    return;
  }
}
