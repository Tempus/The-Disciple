package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.relics.PaperTurtyl;
import chronomuncher.cards.LockedTurtyl;

public class UnlockedTurtyl extends ReplicaOrb
{
  public UnlockedTurtyl(boolean upgraded)
  {
    super(  "Turtyl",            // string ID, 
            upgraded,           // boolean upgraded, 
            3,                  // int passive, 
            3,                  // int passiveUp, 
            5,                  // int timer
            5,                  // int timerUp
            new LockedTurtyl(),
            "Paper Turtyl"); // AbstractCard locked)
  }
    
  @Override
  public void onStartOfTurn()
  { 
    super.onStartOfTurn();
    this.activateEffect();

    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
      if (!mo.isDeadOrEscaped()) {
        if (mo.hasPower("Slow") || this.upgraded) {
          AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(mo, AbstractDungeon.player, new SlowPower(mo, this.passiveAmount), this.passiveAmount, true, AbstractGameAction.AttackEffect.NONE));
          AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(mo, new PaperTurtyl())); 
        }
      }
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedTurtyl(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary