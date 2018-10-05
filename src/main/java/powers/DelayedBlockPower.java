package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class DelayedBlockPower extends AbstractPower
{
  public static final String POWER_ID = "DelayedBlock";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DelayedBlock");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  private int block;
  private static int bombIdOffset;
  
  public DelayedBlockPower(AbstractCreature owner, int turns, int block)
  {
    this.name = NAME;
    this.ID = ("DelayedBlock" + bombIdOffset);
    bombIdOffset += 1;
    this.owner = owner;
    this.amount = turns;
    this.block = block;
    updateDescription();

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedBlock.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedBlockSmall.png"), 0, 0, 32, 32);
  }
  
  public void atStartOfTurn()
  {
    if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead())
    {
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
      if (this.amount == 1) {
        flash();
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.owner.hb.cX, this.owner.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
        AbstractDungeon.actionManager.addToBottom(
          new GainBlockAction(this.owner, this.owner, this.block));
      }
    }
  }
  
  public void updateDescription()
  {
    if (this.amount == 1) {
      this.description = String.format(DESCRIPTIONS[1], new Object[] { Integer.valueOf(this.block) });
    } else {
      this.description = String.format(DESCRIPTIONS[0], new Object[] { Integer.valueOf(this.amount), Integer.valueOf(this.block) });
    }
  }
}
