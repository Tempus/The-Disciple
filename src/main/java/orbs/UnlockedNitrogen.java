package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
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
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedNitrogen;
import chronomuncher.relics.Cryopreserver;

public class UnlockedNitrogen extends ReplicaOrb
{
  public UnlockedNitrogen(boolean upgraded)
  {
    super(  "Nitrogen",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            2,                  // int passiveUp, 
            4,                  // int timer
            3,                  // int timerUp
            new LockedNitrogen(),
            "Cryopreserver"); // AbstractCard locked)

    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, this.basePassiveAmount), this.basePassiveAmount));    
  }
    
  @Override
  public void onEvoke()
  { 
    super.onEvoke();
    this.onShatter();
  }  

  public void onShatter() {
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "Focus", this.basePassiveAmount));
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new Cryopreserver()));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedNitrogen(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary