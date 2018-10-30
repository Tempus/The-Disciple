package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedMawBank;
import chronomuncher.actions.GainGoldAction;

public class UnlockedMawBank extends ReplicaOrb
{
  public UnlockedMawBank(boolean upgraded)
  {
    super(  "MawBank",            // string ID, 
            upgraded,           // boolean upgraded, 
            12,                  // int passive, 
            16,                  // int passiveUp,
            7,                  // int timer
            7,                  // int timerUp
            new LockedMawBank(),
            "MawBank"); // AbstractCard locked)
  }

  @Override
  public void onVictory()
  { 
    this.activateEffect();
    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new MawBank()));
    RewardItem r = new RewardItem(this.passiveAmount, true);
    r.text = this.passiveAmount + " Gold from Maw Bank Replica";
    AbstractDungeon.getCurrRoom().rewards.add(r);
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedMawBank(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary