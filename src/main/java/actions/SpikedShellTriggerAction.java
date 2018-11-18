package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import chronomuncher.relics.SpikedShell;

public class SpikedShellTriggerAction extends AbstractGameAction
{
  
  public SpikedShellTriggerAction() {}
    
  public void update()
  {
    if (AbstractDungeon.player.hasRelic("SpikedShell")) {
      ((SpikedShell)AbstractDungeon.player.getRelic("SpikedShell")).trigger();
    }

    this.isDone = true;
    return;
  }
}
