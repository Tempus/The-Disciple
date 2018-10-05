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
import com.megacrit.cardcrawl.core.Settings;
import java.util.ArrayList;

public class GainGoldAction
  extends AbstractGameAction
{
  private int increaseGold;
  
  public GainGoldAction(AbstractCreature source, int goldAmount)
  {
    this.source = source;
    this.target = AbstractDungeon.player;
    this.increaseGold = goldAmount;
    this.actionType = AbstractGameAction.ActionType.DAMAGE;
    this.duration = Settings.ACTION_DUR_MED;
  }
  
  public void update()
  {
    if ((this.duration == Settings.ACTION_DUR_MED) && (this.target != null))
    {
        AbstractDungeon.player.gainGold(increaseGold);
        for (int i = 0; i < increaseGold; i++) {
          AbstractDungeon.effectList.add(new GainPennyEffect(this.target, this.source.hb.cX, this.source.hb.cY, this.target.hb.cX, this.target.hb.cY, true));
        }
    }
    tickDuration();
  }
}
