package chronomuncher.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;

public class TimeWarpMinorPower
  extends AbstractPower
{
  public static final String POWER_ID = "Time Warp Minor";
  private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Time Warp Minor");
  public static final String NAME = powerStrings.NAME;
  public static final String[] DESC = powerStrings.DESCRIPTIONS;
  private static final int COUNTDOWN_AMT = 12;
  
  public TimeWarpMinorPower(AbstractCreature owner)
  {
    this.name = NAME;
    this.ID = "Time Warp";
    this.owner = owner;
    this.amount = 0;
    updateDescription();
    loadRegion("time");
    this.type = AbstractPower.PowerType.BUFF;
  }
  
  public void updateDescription()
  {
    this.description = (DESC[0] + Integer.toString(COUNTDOWN_AMT) + DESC[1]);
  }
  
  public void onAfterUseCard(AbstractCard card, UseCardAction action)
  {
    flashWithoutSound();
    this.amount += 1;
    if (this.amount == COUNTDOWN_AMT)
    {
      this.amount = 0;
      AbstractDungeon.actionManager.cardQueue.clear();
      for (AbstractCard c : AbstractDungeon.player.limbo.group) {
        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
      }
      AbstractDungeon.player.limbo.group.clear();
      AbstractDungeon.player.releaseCard();
      AbstractDungeon.overlayMenu.endTurnButton.disable(true);
    }
    updateDescription();
  }
}
