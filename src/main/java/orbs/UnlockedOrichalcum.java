package chronomuncher.orbs;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedOrichalcum;

public class UnlockedOrichalcum extends ReplicaOrb
{
  public UnlockedOrichalcum(boolean upgraded)
  {
    super(  "Orichalcum",            // string ID, 
            upgraded,           // boolean upgraded, 
            6,                  // int passive, 
            9,                  // int passiveUp, 
            5,                  // int timer
            5,                  // int timerUp
            new LockedOrichalcum(),
            "Orichalcum"); // AbstractCard locked)
  }

  @Override
  public void onEndOfTurn()
  { 
    this.activateEffect();

    // for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
    //   if (c.block > 0) {
    //     return;
    //   }
    // }

    if (AbstractDungeon.player.currentBlock == 0 || ((AbstractDungeon.player.currentBlock == 6) && (AbstractDungeon.player.hasRelic("Orichalcum"))))
    {
      AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new Orichalcum()));
      AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.passiveAmount));
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedOrichalcum(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary