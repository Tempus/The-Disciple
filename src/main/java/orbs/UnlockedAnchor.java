package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedAnchor;
import basemod.BaseMod;

public class UnlockedAnchor extends ReplicaOrb
{
  public UnlockedAnchor(boolean upgraded)
  {
    super(  "Anchor",            // string ID, 
            upgraded,           // boolean upgraded, 
            0,                  // int passive, 
            0,                  // int passiveUp, 
            1,                  // int timer
            1,                  // int timerUp
            new LockedAnchor(),
            "Anchor"); // AbstractCard locked)
  }

  @Override
  public void decrementTimer() {
    this.timer++;
    this.timeElapsed++;
    updateDescription();
  }

  @Override
  public void applyFocus()
  {
    AbstractPower power = AbstractDungeon.player.getPower("Focus");
    if (power == null) { return; }

    this.timer = Math.max(0, this.baseTimer + power.amount + this.timeElapsed);
  }

  public void onShatter() {
    if (this.upgraded) {
      AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new Anchor()));
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 10));
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedAnchor(this.upgraded); }
}


// Methods
  // public void onStartOfTAnchor()
  // public void onEndOfTAnchor()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary