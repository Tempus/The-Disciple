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
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.powers.ConservePower;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedIceCream;

public class UnlockedIceCream extends ReplicaOrb
{
  public UnlockedIceCream(boolean upgraded)
  {
    super(  "IceCream",            // string ID, 
            upgraded,           // boolean upgraded, 
            0,                  // int passive, 
            1,                  // int passiveUp, 
            5,                  // int timer
            5,                  // int timerUp
            new LockedIceCream(),
            "Ice Cream"); // AbstractCard locked)

    this.activateEffect();
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new IceCream()));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConservePower(AbstractDungeon.player, 1), 1));
  }
    
  @Override
  public void onStartOfTurn()
  {
    super.onStartOfTurn();
    this.activateEffect();

    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new IceCream()));
    AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.passiveAmount));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConservePower(AbstractDungeon.player, 1), 1));
  }

  @Override
  public void onEvoke()
  {
    super.onEvoke();
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Conserve"));
  }

  @Override
  public AbstractOrb makeCopy() { return new UnlockedIceCream(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary