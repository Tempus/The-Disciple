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

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.unique.SkillFromDeckToHandAction;
import com.megacrit.cardcrawl.actions.unique.AttackFromDeckToHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedFlame;
import chronomuncher.actions.RandomCardFromDeckToHandAction;
import chronomuncher.actions.PowerFromDeckToHandAction;

public class UnlockedFlame extends ReplicaOrb
{
  public UnlockedFlame(boolean upgraded)
  {
    super(  "Flame",            // string ID, 
            upgraded,           // boolean upgraded, 
            0,                  // int passive, 
            0,                  // int passiveUp, 
            0,                  // int timer
            0,                  // int timerUp
            new LockedFlame(),
            "Bottled Flame"); // AbstractCard locked)

    this.showPassive = false;
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
    
  @Override
  public void onStartOfTurn()
  { 
    this.activateEffect();
    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new BottledFlame()));

    if (!this.upgraded) {
      AbstractDungeon.actionManager.addToTop(new RandomCardFromDeckToHandAction(AbstractCard.CardType.ATTACK));
    } else {
      AbstractDungeon.actionManager.addToTop(new AttackFromDeckToHandAction(1));      
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedFlame(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary