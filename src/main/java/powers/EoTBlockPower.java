package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import chronomuncher.ChronoMod;

public class EoTBlockPower extends AbstractPower
{
  public static final String POWER_ID = "DelayedAttack";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("DelayedAttack");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  private int damage;
  private static int bombIdOffset;
  public boolean ignoreBlock = false;
  
  public EoTBlockPower(AbstractCreature owner)
  {
    this.name = NAME;
    this.ID = ("EoTBlockAttack" + bombIdOffset);
    bombIdOffset += 1;
    this.owner = owner;
    this.type = AbstractPower.PowerType.BUFF;
    updateDescription();

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedBlock.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/DelayedBlockSmall.png"), 0, 0, 32, 32);
  }

  public void atEndOfRound()
  {
    if (!this.owner.isDead)
    {
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(this.owner.hb.cX, this.owner.hb.cY - 40.0F * Settings.scale, Color.RED.cpy()), 0.3F));
      AbstractDungeon.actionManager.addToBottom(
        new DamageAction(this.owner, new DamageInfo(null, AbstractDungeon.player.currentBlock, DamageInfo.DamageType.THORNS), AttackEffect.NONE));
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
    }
  }

  public void updateDescription()
  {
    this.description = String.format("Will do damage based on the remaining block at the end of the enemy's turn.");
  }
}
