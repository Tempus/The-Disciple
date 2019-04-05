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
import com.megacrit.cardcrawl.cards.DamageInfo;

import basemod.ReflectionHacks;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class SleepPower extends AbstractPower
{
  public static final String POWER_ID = "Sleep";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Sleep");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public AbstractMonster monster;

  public SleepPower(AbstractMonster owner, int amount)
  {
    this.name = NAME;
    this.ID = "Sleep";
    this.owner = owner;
    this.monster = owner;
    this.amount = amount;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/Sleep.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/SleepSmall.png"), 0, 0, 32, 32);
    this.type = AbstractPower.PowerType.DEBUFF;
    this.isTurnBased = true;
  }
    
  public void onInitialApplication() {
    this.applySleep();
  }

  @Override
  public void updateDescription()
  {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  // Decrement Sleep, and make sure the enemy is correctly set to sleep
  @Override
  public void atEndOfTurn(boolean isPlayer)
  {
    if (!isPlayer) { 
      this.applySleep();
    }

    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.monster, this.monster, this, 1));
      if (this.amount == 1) {
        this.wake();
      }
    }
  }

  public void atStartOfTurn()
  {
    this.applySleep();
    switch(this.monster.id) {
      case "GiantHead":
        int count = (int)ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "count");
        ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "count", count + 1);
        break;
      case "BookOfStabbing":
        int stabCount = (int)ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "stabCount");
        ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "stabCount", stabCount - 1);
        break;
      case "Maw":
        int turnCount = (int)ReflectionHacks.getPrivate(this.monster, this.monster.getClass(), "turnCount");
        ReflectionHacks.setPrivate(this.monster, this.monster.getClass(), "turnsTaken", turnCount - 1);
        break;
    }
  }
  
  public void applySleep() {
    this.monster.setMove((byte)-2, AbstractMonster.Intent.SLEEP); 
    this.monster.createIntent();
  }

  public void wake() {
    AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.monster, this.monster, this, 99));

    // Special Cases:
    switch(this.monster.id) {

      case "SlimeBoss":
        this.monster.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
        break;

      case "Looter":
      case "Mugger":
        this.monster.setMove((byte)3, AbstractMonster.Intent.ESCAPE);
        break;

      case "BanditChild":
        this.monster.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)this.monster.damage.get(0)).base, 2, true);;
        break;
        
      default:
        this.monster.rollMove(); 
        break;
    }

    this.monster.createIntent();
  }

  // Wake up!
  public int onAttacked(DamageInfo info, int damageAmount)
  {
    ChronoMod.log(Integer.toString(damageAmount));
    if (damageAmount > 0) { 
      this.wake();
    }
    return damageAmount;
  }
}
