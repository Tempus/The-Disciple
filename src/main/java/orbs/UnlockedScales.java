package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedScales;
import chronomuncher.actions.RandomReplicaAction;
import chronomuncher.actions.DiscoveryUpAction;

public class UnlockedScales extends ReplicaOrb
{
  ArrayList<AbstractCard> curses = new ArrayList();

  public UnlockedScales(boolean upgraded)
  {
    super(  "Bronze Scales",            // string ID, 
            upgraded,           // boolean upgraded, 
            3,                  // int passive, 
            5,                  // int passiveUp,
            6,                  // int timer
            6,                  // int timerUp
            new LockedScales(),
            "Bronze Scales"); // AbstractCard locked)

    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, this.passiveAmount), this.passiveAmount));
  }

  @Override
  public void onEvoke()
  {
    super.onEvoke();
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "Thorns", this.passiveAmount));
  }

  @Override
  public void onShatter() {
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, "Thorns", this.passiveAmount));
  }

  @Override
  public AbstractOrb makeCopy() { return new UnlockedScales(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary