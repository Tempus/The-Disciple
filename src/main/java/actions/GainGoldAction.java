package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import java.util.ArrayList;

public class GainGoldAction
  extends AbstractGameAction
{
  private int increaseGold;
  private static final float DURATION = 0.1F;
  
  public GainGoldAction(AbstractCreature target, int goldAmount)
  {
    this.source = target;
    this.target = target;
    this.increaseGold = goldAmount;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.duration = 0.1F;
  }
  
  public void update()
  {
    if ((this.duration == 0.1F) && 
      (this.target != null))
    {
      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
      
        for (int i = 0; i < this.increaseGold; i++) {
          AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, true));
        }
    }
    tickDuration();
  }
}
