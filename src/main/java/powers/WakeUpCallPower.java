package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;

import chronomuncher.ChronoMod;

public class WakeUpCallPower extends AbstractPower
{
  public static final String POWER_ID = "WakeUpCall";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("WakeUpCall");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  public DamageInfo damage;
  private static int bombIdOffset;
  private boolean useable = true;
  
  public WakeUpCallPower(AbstractCreature owner, int damage)
  {
    this.name = NAME;
    this.ID = ("WakeUpCall" + bombIdOffset);
    bombIdOffset += 1;
    this.owner = owner;
    this.damage = new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL);;
    this.type = AbstractPower.PowerType.BUFF;
    updateDescription();

    this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/WakeUpCall.png"), 0, 0, 84, 84);
    this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/WakeUpCallSmall.png"), 0, 0, 32, 32);
  }

  @Override
  public void update(int slot)
  {
    super.update(slot);
    if ((((AbstractMonster)this.owner).intent == AbstractMonster.Intent.SLEEP || 
         ((AbstractMonster)this.owner).intent == AbstractMonster.Intent.STUN) &&
        this.useable)
    {
      this.useable = false;
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(this.owner.hb.cX, this.owner.hb.cY)));
      AbstractDungeon.actionManager.addToBottom(new SFXAction("BELL", 1.0F));

      this.damage.applyPowers(AbstractDungeon.player, this.owner);
      AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, this.damage, AttackEffect.NONE));
    }
    // updateDescription();
  }

  @Override
  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
    updateDescription();
  }

  public void atStartOfTurn() { updateDescription(); }
  public void atEndOfRound() { updateDescription(); }
  public void atEndOfTurn(boolean isPlayer) { updateDescription(); }
  public void onAfterUseCard(AbstractCard card, UseCardAction action) { updateDescription(); }

  @Override
  public void updateDescription()
  {
    if (!this.owner.isDead) {
      this.applyPowers();
      this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
  }

  public void applyPowers() {
    float tmp = this.damage.base;
      for (AbstractPower p : AbstractDungeon.player.powers) {
        tmp = p.atDamageGive(tmp, this.damage.type);
      }
      for (AbstractPower p : this.owner.powers) {
        tmp = p.atDamageReceive(tmp, this.damage.type);
      }
      for (AbstractPower p : AbstractDungeon.player.powers) {
        tmp = p.atDamageFinalGive(tmp, this.damage.type);
      }
      for (AbstractPower p : this.owner.powers) {
        tmp = p.atDamageFinalReceive(tmp, this.damage.type);
      }
      this.amount = MathUtils.floor(tmp);
      if (this.amount < 0) {
        this.amount = 0;
      }
  }
}
