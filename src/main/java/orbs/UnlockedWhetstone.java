package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedWhetstone;
import chronomuncher.actions.UpgradeCardsInHandAction;

public class UnlockedWhetstone extends ReplicaOrb
{
  public UnlockedWhetstone(boolean upgraded)
  {
    super(  "Whetstone",            // string ID, 
            upgraded,           // boolean upgraded, 
            0,                  // int passive, 
            0,                  // int passiveUp, 
            5,                  // int timer
            5,                  // int timerUp
            new LockedWhetstone(), // AbstractCard locked)
            "Whetstone"); 

    this.atTurnStartPostDraw();    
  }
    
  @Override
  public void atTurnStartPostDraw() {
    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new Whetstone()));
    AbstractDungeon.actionManager.addToBottom(new UpgradeCardsInHandAction(AbstractCard.CardType.ATTACK, this.upgraded));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedWhetstone(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary