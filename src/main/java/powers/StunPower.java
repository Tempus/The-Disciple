package chronomuncher.powers;
import chronomuncher.ChronoMod;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class StunPower extends AbstractPower
{
  public static final String POWER_ID = "Stun";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Stun");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public AbstractMonster monster;

  public StunPower(AbstractMonster owner, int amount)
  {
    this.name = NAME;
    this.ID = "Stun";
    this.owner = owner;
    this.monster = owner;
    this.amount = amount;
    updateDescription();
    this.type = AbstractPower.PowerType.DEBUFF;

    this.region128 = new TextureAtlas.AtlasRegion(new Texture("images/powers/Stun.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(new Texture("images/powers/StunSmall.png"), 0, 0, 32, 32);

    ChronoMod.log(owner.id);
    ChronoMod.log(this.monster.id);

    this.isTurnBased = true;
    this.monster.setMove((byte)0, AbstractMonster.Intent.STUN); 
    this.monster.createIntent();
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
    if (!isPlayer) { 
      this.monster.setMove((byte)0, AbstractMonster.Intent.STUN); 
      this.monster.createIntent();
    }

    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.monster, this.monster, this, 1));
      if (this.amount == 1) {
        this.monster.rollMove();
        this.monster.createIntent();
      }
    }
  }

  public void atStartOfTurn(AbstractCard card, UseCardAction action)
  {

    // Special Cases:
    switch(this.monster.id) {

      case "SlimeBoss":
        this.monster.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
        break;

      case "Looter":
      case "Mugger":
        this.monster.setMove((byte)2, AbstractMonster.Intent.DEFEND);
        break;

      default:
        this.monster.rollMove(); 
        break;
   }

    this.monster.createIntent();
  }
}

