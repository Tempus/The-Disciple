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

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedAstrolabe;
import chronomuncher.actions.RandomReplicaAction;
import chronomuncher.actions.DiscoveryUpAction;

public class UnlockedAstrolabe extends ReplicaOrb
{
  ArrayList<AbstractCard> curses = new ArrayList();

  public UnlockedAstrolabe(boolean upgraded)
  {
    super(  "Astrolabe",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            1,                  // int passiveUp,
            3,                  // int timer
            3,                  // int timerUp
            new LockedAstrolabe(),
            "Astrolabe"); // AbstractCard locked)
  }

  @Override
  public void atTurnStartPostDraw()
  { 
    AbstractPlayer p = AbstractDungeon.player;

    this.activateEffect();
    AbstractDungeon.actionManager.addToBottom(new ExhaustAction(p,p,this.passiveAmount,false));
    if (!this.upgraded) {
      AbstractCard newCard = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
      newCard.upgrade();
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(newCard));
    } else {
      AbstractDungeon.actionManager.addToBottom(new DiscoveryUpAction());
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedAstrolabe(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary