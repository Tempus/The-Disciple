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
import chronomuncher.cards.LockedMercury;

public class UnlockedMercury extends ReplicaOrb
{
  public UnlockedMercury(boolean upgraded)
  {
    super(  "Mercury",            // string ID, 
            upgraded,           // boolean upgraded, 
            3,                  // int passive, 
            5,                  // int passiveUp, 
            5,                  // int timer
            5,                  // int timerUp
            new LockedMercury(),
            "Mercury Hourglass"); // AbstractCard locked)
  }

  @Override
  public void onEndOfTurn()
  { 
    this.activateEffect();

    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, new MercuryHourglass()));
    AbstractDungeon.actionManager.addToBottom(
    new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.passiveAmount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedMercury(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary