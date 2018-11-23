package chronomuncher.powers;
import chronomuncher.ChronoMod;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.*;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class StunPower extends AbstractPower
{
  public static final String POWER_ID = "Stun";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Stun");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public AbstractMonster monster;

  public EnemyMoveInfo move;

  public StunPower(AbstractCreature owner, int amount)
  {
    this.name = NAME;
    this.ID = "Stun";
    this.owner = owner;
    if (this.owner instanceof AbstractMonster) { this.monster = (AbstractMonster)owner; }
    this.amount = amount;
    updateDescription();
    this.type = AbstractPower.PowerType.DEBUFF;

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/Stun.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/StunSmall.png"), 0, 0, 32, 32);

    this.isTurnBased = true;
  }
    
  public void onInitialApplication() {
    if (this.owner instanceof AbstractPlayer) { this.endTurn(); }
    else {
      this.move = (EnemyMoveInfo)ReflectionHacks.getPrivate(this.owner, AbstractMonster.class, "move");
      this.monster.setMove((byte)-2, AbstractMonster.Intent.STUN); 
      this.monster.createIntent();
    }
  }

  @Override
  public void updateDescription()
  {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  // Decrement Stun, and make sure the enemy is correctly set to sleep
  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    if (this.owner instanceof AbstractPlayer && isPlayer) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    if (!isPlayer && (this.owner instanceof AbstractMonster)) { 
      this.monster.setMove((byte)-2, AbstractMonster.Intent.STUN); 
      this.monster.createIntent();

      if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
      {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        if (this.amount == 1) {
          this.monster.setMove(this.move.nextMove, this.move.intent, this.move.baseDamage, this.move.multiplier, this.move.isMultiDamage);
          this.monster.createIntent();
        }
      }
    }
  }

  public void endTurn() {
      AbstractDungeon.actionManager.cardQueue.clear();
      for (AbstractCard c : AbstractDungeon.player.limbo.group) {
        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
      }
      AbstractDungeon.player.limbo.group.clear();
      AbstractDungeon.player.releaseCard();
      AbstractDungeon.overlayMenu.endTurnButton.disable(true);
  }
}

