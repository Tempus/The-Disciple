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

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedBlood;

public class UnlockedBlood extends ReplicaOrb
{
  public UnlockedBlood(boolean upgraded)
  {
    super(  "Blood",            // string ID, 
            upgraded,           // boolean upgraded, 
            2,                  // int passive, 
            3,                  // int passiveUp,
            3,                  // int timer
            3,                  // int timerUp
            new LockedBlood(),
            "Blood Vial"); // AbstractCard locked)
  }

  @Override
  public void onStartOfTurn()
  { 
    super.onStartOfTurn();
    AbstractPlayer p = AbstractDungeon.player;

    this.activateEffect();

    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(p, new BloodVial()));
    AbstractDungeon.actionManager.addToTop(new HealAction(p, p, this.passiveAmount));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedBlood(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary