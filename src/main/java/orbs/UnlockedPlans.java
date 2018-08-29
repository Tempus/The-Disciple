package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedPlans;
import chronomuncher.powers.RetainOncePower;

public class UnlockedPlans extends ReplicaOrb
{
  public UnlockedPlans(boolean upgraded)
  {
    super(  "Plans",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            2,                  // int passiveUp, 
            4,                  // int timer
            4,                  // int timerUp
            new LockedPlans(),
            ""); // AbstractCard locked)
  }

  @Override
  public void onEndOfTurn()
  { 
    // super.onEndOfTurn();
    this.activateEffect();
    if (AbstractDungeon.player.hand.group.size() > 0) {
      AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(AbstractDungeon.player, this.passiveAmount));
      // AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RetainOncePower(this.passiveAmount), this.passiveAmount));    
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedPlans(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary