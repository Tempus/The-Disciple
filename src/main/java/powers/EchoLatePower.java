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
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import javafx.util.Pair;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

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
    this.region128 = new TextureAtlas.AtlasRegion(new Texture("images/powers/EchoLate.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(new Texture("images/powers/EchoLateSmall.png"), 0, 0, 32, 32);
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
      ChronoMod.log("Echo Late: " + Integer.toString(cardsDoubledThisTurn));

      AbstractCard card;
      playedCard = this.cardsPlayedThisTurn.pollFirst();
      if (playedCard == null) { return; }

      card = playedCard.getKey();
      // if (card.cardID == "Echonomics") { return; }
      
      if (!card.purgeOnUse) {
        this.doubleCard(card, playedCard.getValue());
      }
    }

    this.cardsPlayedThisTurn.clear();
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    ChronoMod.log("Adding to list: " + card.cardID);
    this.cardsPlayedThisTurn.addFirst(new Pair(card, action));
  }

  public void doubleCard(AbstractCard card, UseCardAction action) {
    ChronoMod.log("Doubling: " + card.cardID);
    flash();
    AbstractMonster m = null;
    if (action.target != null) {
      m = (AbstractMonster)action.target;
    }
    AbstractCard tmp = card.makeStatEquivalentCopy();
    AbstractDungeon.player.limbo.addToBottom(tmp);
    tmp.current_x = card.current_x;
    tmp.current_y = card.current_y;
    tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
    tmp.target_y = (Settings.HEIGHT / 2.0F);
    tmp.freeToPlayOnce = true;
    tmp.purgeOnUse = true;
    if (m != null) {
      tmp.calculateCardDamage(m);
    }
    AbstractDungeon.player.useCard(tmp, m, card.energyOnUse);
    if (tmp.cardID.equals("Rampage")) {
      AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(card, tmp.magicNumber));
    } else if (tmp.cardID.equals("Genetic Algorithm")) {
      AbstractDungeon.actionManager.addToBottom(new IncreaseMiscAction(card.cardID, card.misc + card.magicNumber, card.magicNumber));
    }
  }
}
