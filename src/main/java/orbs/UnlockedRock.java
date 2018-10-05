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
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import java.util.Random;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.relics.*;

import chronomuncher.ChronoMod;

public class UnlockedRock extends ReplicaOrb
{

  public String Modifier = "";
  public ArrayList<String> rockType = new ArrayList();


  public UnlockedRock(boolean upgraded)
  {
    super(  "Rock",            // string ID, 
            upgraded,           // boolean upgraded, 
            1,                  // int passive, 
            1,                  // int passiveUp, 
            4,                  // int timer
            4,                  // int timerUp
            null,
            "Formal Rock"); // AbstractCard locked)

    for(int i = 0; i < 10; i++) { rockType.add(""); }
    for(int i = 0; i < 10; i++) { rockType.add("Surprise"); }
    for(int i = 0; i < 10; i++) { rockType.add("Ribbon"); }
    for(int i = 0; i < 5; i++)  { rockType.add("Wizard"); }
    for(int i = 0; i < 3; i++)  { rockType.add("Louse"); }
    for(int i = 0; i < 3; i++)  { rockType.add("Slime"); }
    for(int i = 0; i < 2; i++)  { rockType.add("Potato"); }
    for(int i = 0; i < 2; i++)  { rockType.add("Evil"); }

    Modifier = rockType.get(new Random().nextInt(rockType.size()));

    this.img = new Texture("chrono_images/orbs/" + ID + Modifier + ".png");
  }

  // @Override
  // public void onStartOfTurn() {
    // this.img = new Texture("chrono_images/orbs/" + ID + Modifier + "Sad.png");
  // }

  @Override
  public void onShatter()
  { 
    this.activateEffect();

    AbstractDungeon.actionManager.addToBottom(
        new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.passiveAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
  }
  
  @Override
  public AbstractOrb makeCopy() { return new UnlockedRock(this.upgraded); }
}


// Methods
  // public void onStartOfTurn()
  // public void onEndOfTurn()
  // public void applyFocus()
  // public void receivePostPowerApplySubscriber(AbstractPower p, AbstractCreature target, AbstractCreature source);  
  // public void receiveCardUsed(AbstractCard c);

  // We can add more hooks by patching AbstractCreature if necessary