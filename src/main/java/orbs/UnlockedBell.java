package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
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
import java.util.UUID;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedBell;
import chronomuncher.actions.RandomReplicaAction;

public class UnlockedBell extends ReplicaOrb
{
  ArrayList<UUID> curses = new ArrayList();

  public UnlockedBell(boolean upgraded)
  {
    super(  "Bell",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            1,                  // int passiveUp,
            3,                  // int timer
            4,                  // int timerUp
            new LockedBell(),
            "Calling Bell"); // AbstractCard locked)
  }

  @Override
  public void atTurnStartPostDraw()
  { 
    AbstractPlayer p = AbstractDungeon.player;

    this.activateEffect();
    AbstractDungeon.actionManager.addToBottom(new RandomReplicaAction());
    AbstractCard c = AbstractDungeon.getCard(AbstractCard.CardRarity.CURSE);
    curses.add(c.uuid);
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, false, false));
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(p, new CallingBell()));
  }

  @Override
  public void onShatter()
  { 
    if (this.upgraded) {
      this.activateEffect();

      for (AbstractCard c : AbstractDungeon.player.hand.group) {
        for (UUID curse : curses) {
          if (c.uuid == curse) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
          }
        }
      }

      for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
        for (UUID curse : curses) {
          if (c.uuid == curse) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
          }
        }
      }

      for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
        for (UUID curse : curses) {
          if (c.uuid == curse) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
          }
        }
      }
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedBell(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary