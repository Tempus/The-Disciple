package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class TimeDilatePower extends AbstractPower
{
  public static final String POWER_ID = "TimeDilate";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("TimeDilate");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  private ArrayList<AbstractPower> savedPowers = new ArrayList();
  
  public TimeDilatePower(AbstractCreature owner, int amount)
  {
    ChronoMod.log("Power Applied");
    this.name = NAME;
    this.ID = "TimeDilate";
    this.owner = owner;
    this.amount = amount;
    updateDescription();
    this.region128 = new TextureAtlas.AtlasRegion(new Texture("images/powers/TimeDilate.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(new Texture("images/powers/TimeDilateSmall.png"), 0, 0, 32, 32);
    this.type = AbstractPower.PowerType.DEBUFF;
    this.isTurnBased = true;
  }
  
  public void onInitialApplication()
  {
    for (Object e = this.owner.powers.iterator(); ((Iterator)e).hasNext();)
    {
      AbstractPower p = (AbstractPower)((Iterator)e).next();
      if (p.type == AbstractPower.PowerType.BUFF
        && !p.ID.equals("Mode Shift")
        && !p.ID.equals("Minion")
        && !p.ID.equals("Fading")
        && !p.ID.equals("Unawakened")
        && !p.ID.equals("Split")
        && !p.ID.equals("Life Link")
        && !p.ID.equals("TimeDilate")) {

        ChronoMod.log("Storing Power: " + p.ID);
        if (!p.ID.equals("Flight")) {
          this.savedPowers.add(p); }
        p.onRemove();
        ((Iterator)e).remove();
        AbstractDungeon.onModifyPower();
      }
    }
    updateDescription();
  }
  
  public void atEndOfRound()
  {
    if (this.amount == 0) {
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, "TimeDilate"));
    } else {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, "TimeDilate", 1));
    }
  }
  
  public void updateDescription()
  {
    this.description = (DESCRIPTIONS[0] + this.savedPowers.size() + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]);
  }
  
  public void onRemove()
  {
    if (!this.owner.isDying)
    {
      for (AbstractPower power : this.savedPowers) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, AbstractDungeon.player, power, power.amount));
      }
      this.savedPowers.clear();
    }
  }
}
