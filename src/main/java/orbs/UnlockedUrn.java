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
import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;
import chronomuncher.cards.LockedUrn;
import basemod.BaseMod;

public class UnlockedUrn extends ReplicaOrb
{
  public UnlockedUrn(boolean upgraded)
  {
    super(  "Urn",            // string ID, 
            upgraded,           // boolean upgraded, 
            2,                  // int passive, 
            3,                  // int passiveUp, 
            4,                  // int timer
            4,                  // int timerUp
            new LockedUrn(),
            "Bird Faced Urn"); // AbstractCard locked)
  }

  @Override
  public void onCardUse(AbstractCard c)
  { 
    this.activateEffect();

    if (c.type == AbstractCard.CardType.POWER)
    {
      AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, new BirdFacedUrn()));

      AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, this.passiveAmount));
    }
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedUrn(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary