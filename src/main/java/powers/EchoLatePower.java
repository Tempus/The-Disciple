package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.blights.TimeMaze;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import javafx.util.Pair;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;
import chronomuncher.actions.PlayEchoCardAction;

public class EchoLatePower
  extends AbstractPower
{
  public static final String POWER_ID = "Echo Late";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("EchoLate");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  
  private static Deque<Pair<AbstractCard, UseCardAction>> cardsPlayedThisTurn = new LinkedList();
  private static Pair<AbstractCard, UseCardAction> playedCard;

  private static float x;
  private static float y;

  public EchoLatePower(AbstractCreature owner, int amount)
  {
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.amount = amount;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/EchoLate.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/EchoLateSmall.png"), 0, 0, 32, 32);
    this.isPostActionPower = true;
  }
  
  public void updateDescription()
  {
    if (this.amount == 1) {
      this.description = DESCRIPTIONS[0];
    } else {
      this.description = (DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]);
    }
  }

  public void atStartOfTurn()
  {
    this.cardsPlayedThisTurn.clear();
  }

  public void atEndOfTurn(boolean isPlayer)
  {
    ChronoMod.log("Echo Late activating");
    for (int cardsDoubledThisTurn = 0; cardsDoubledThisTurn < this.amount; cardsDoubledThisTurn++) {
      if (this.shouldStopTimeWarp() || this.shouldStopTimeMazePower() || this.shouldStopTimeMazeBlight()) { return; }
      ChronoMod.log("Echo Late: " + Integer.toString(cardsDoubledThisTurn));

      AbstractCard card;
      playedCard = this.cardsPlayedThisTurn.pollFirst();
      if (playedCard == null) { return; }

      card = playedCard.getKey();
      // if (card.cardID == "Echonomics") { return; }
      
      if (!card.purgeOnUse) {
        AbstractDungeon.actionManager.addToBottom(new PlayEchoCardAction(card, playedCard.getValue().target));
      }
    }

    this.cardsPlayedThisTurn.clear();
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    ChronoMod.log("Adding to list: " + card.cardID);
    if (card.cardID == "Tempo") {
      this.cardsPlayedThisTurn.addFirst(new Pair(card.makeStatEquivalentCopy(), action));
    } else {
      this.cardsPlayedThisTurn.addFirst(new Pair(card, action));
    }
  }

  public boolean shouldStopTimeWarp() {
        // If we're facing Time Eater, and at 12 cards, stop playing.
    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
      if (!mo.isDead && !mo.escaped) {
        if (mo.hasPower("Time Warp")) {
          if (mo.getPower("Time Warp").amount > 11 || mo.getPower("Time Warp").amount == 0) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean shouldStopTimeMazePower() {
    // If we're facing Time Eater, and at 12 cards, stop playing.
    if (AbstractDungeon.player.hasPower("Time Maze")) {
      if (AbstractDungeon.player.getPower("Time Maze").amount < 1) {
        return true;
      }
    }
    return false;
  }  

  public boolean shouldStopTimeMazeBlight() {
    // If we're facing Time Eater, and at 12 cards, stop playing.
    if (AbstractDungeon.player.hasBlight("TimeMaze")) {
      if (((TimeMaze)AbstractDungeon.player.getBlight("TimeMaze")).counter >= 15) {
        return true;
      }
    }
    return false;
  }  
}
