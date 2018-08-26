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
import java.lang.Math;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedThread;

public class UnlockedThread extends ReplicaOrb
{
  public UnlockedThread(boolean upgraded)
  {
    super(  "Thread",            // string ID, 
            upgraded,           // boolean upgraded, 
            2,                  // int passive, 
            2,                  // int passiveUp, 
            2,                  // int timer
            3,                  // int timerUp
            new LockedThread(),
            "Thread and Needle"); // AbstractCard locked)
  }

  @Override
  public void onStartOfTurn()
  { 
    super.onStartOfTurn();

    // int existingArmor = 0;
    // int apply = this.passiveAmount;

    // if (AbstractDungeon.player.hasPower("Plated Armor")) {
    //   AbstractPower power = AbstractDungeon.player.getPower("Plated Armor");
    //   existingArmor = power.amount;
    // }

    // if (!this.upgraded) {
    //   if (existingArmor + this.passiveAmount > 5) { 
    //     apply = 5 - existingArmor; }
    // } else {
    //   if (existingArmor + this.passiveAmount > 8) {
    //     apply = 8 - existingArmor; }
    // }

    // if (apply <= 0) { return; }

    this.activateEffect();

    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, this.passiveAmount), this.passiveAmount));    
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new ThreadAndNeedle()));
  }

  @Override
  public AbstractOrb makeCopy() { return new UnlockedThread(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary