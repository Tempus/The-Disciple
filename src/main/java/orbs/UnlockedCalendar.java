package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedCalendar;

public class UnlockedCalendar extends ReplicaOrb
{
  public UnlockedCalendar(boolean upgraded)
  {
    super(  "Calendar",            // string ID, 
            upgraded,           // boolean upgraded, 
            52,                  // int passive, 
            52,                  // int passiveUp, 
            7,                  // int timer
            4,                  // int timerUp
            new LockedCalendar(),
            "Stone Calendar"); // AbstractCard locked)
  }

  @Override
  public void onShatter()
  { 
    this.activateEffect();

    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new StoneCalendar()));
    AbstractDungeon.actionManager.addToBottom(
      new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.passiveAmount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedCalendar(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary