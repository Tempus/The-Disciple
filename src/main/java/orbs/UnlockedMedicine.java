package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.relics.*;

import basemod.BaseMod;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedMedicine;

public class UnlockedMedicine extends ReplicaOrb
{
  public boolean doOnce = true;

  public UnlockedMedicine(boolean upgraded)
  {
    super(  "Medicine",            // string ID, 
            upgraded,           // boolean upgraded, 
            0,                  // int passive, 
            1,                  // int passiveUp, 
            7,                  // int timer
            7,                  // int timerUp
            new LockedMedicine(),
            "Medical Kit"); // AbstractCard locked)
  }
    
  @Override
  public void updateDescription()
  {
    applyFocus();
    this.description = this.descriptions[0];
    if (this.upgraded) {
      this.description = this.descriptions[1];
    }
  }

  public void onCardDraw(AbstractCard c) {
    if (c.type == AbstractCard.CardType.STATUS){
      this.activateEffect();
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
      AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new MedicalKit()));
      if (this.upgraded) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1)); 
      }
    }
  }

  @Override
  public void onStartOfTurn() {
    super.onStartOfTurn();
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedMedicine(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary