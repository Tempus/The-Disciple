package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;
import chronomuncher.vfx.OracleScreenEffect;
import chronomuncher.vfx.OracleStarEffect;

public class AuguryPower extends AbstractPower
{
  public static final String POWER_ID = "Augury";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Augury");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public boolean upgraded = false;
  public boolean starsOn = false;
  public int DISCARDS = 2;
  public static SeekAction activeAction;

  public AuguryPower(Boolean upgraded)
  {
    this.name = NAME;
    this.ID = "Augury";
    this.owner = AbstractDungeon.player;
    this.amount = AbstractDungeon.player.gameHandSize-1;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/Augury.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/AugurySmall.png"), 0, 0, 32, 32);

    this.upgraded = upgraded;
    this.starsOn = false;
    this.type = AbstractPower.PowerType.BUFF;
  }
  
  public void onInitialApplication()
  {
    AbstractDungeon.player.gameHandSize = 0;
  }
    
  public void setHandAmount() {
    this.amount = AbstractDungeon.player.masterHandSize;

    if (!this.upgraded) {
      this.amount -= 1; }

    if (this.owner.hasPower("Draw Down")) {
      this.amount -= this.owner.getPower("Draw Down").amount; }

    if (this.owner.hasPower("Draw Reduction")) {
      this.amount -= 1; }

    if (this.owner.hasPower("Draw")) {
      this.amount += this.owner.getPower("Draw").amount; }

    if (this.amount > 10 - AbstractDungeon.player.hand.size()){
      this.amount = 10 - AbstractDungeon.player.hand.size();
    }

    ChronoMod.log("Draw amount for Augury set to: " + Integer.toString(this.amount));
  }

  public void atStartOfTurnPostDraw()
  {
    this.setHandAmount();

    if (AbstractDungeon.player.drawPile.size() < this.amount) {
        AbstractDungeon.actionManager.addToBottom(new EmptyDeckShuffleAction());
    }

    // for (int i = 0; i<DISCARDS; i++) {
    //   if (!this.upgraded && AbstractDungeon.player.drawPile.size() > 0) {
    //     AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(AbstractDungeon.player.drawPile.getRandomCard(true), AbstractDungeon.player.drawPile));
    //   }
    // }

    activeAction = new SeekAction(this.amount);
    AbstractDungeon.actionManager.addToBottom(activeAction);
    this.starsOn = true;
  }
  
  public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
    super.renderIcons(sb, x, y, c);
    if (this.starsOn) {
      if (activeAction.isDone) {
        ChronoMod.log("Switching off stars");
        this.starsOn = false;
      }

      AbstractDungeon.effectsQueue.add(new OracleScreenEffect());
      AbstractDungeon.effectsQueue.add(new OracleStarEffect());
    }
  }

  public void onRemove()
  {
    AbstractDungeon.player.gameHandSize = AbstractDungeon.player.masterHandSize;
  }
  
  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
  }
}
