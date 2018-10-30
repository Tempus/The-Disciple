package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import java.util.ArrayList;

public class DiscoveryUpAction
  extends AbstractGameAction
{
  private boolean retrieveCard = false;
  private AbstractCard.CardType cardType = null;
  
  public DiscoveryUpAction()
  {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = Settings.ACTION_DUR_FAST;
  }
  
  public DiscoveryUpAction(AbstractCard.CardType type)
  {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = Settings.ACTION_DUR_FAST;
    this.cardType = type;
  }
  
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_FAST)
    {
      if (this.cardType == null) {
        AbstractDungeon.cardRewardScreen.discoveryOpen();
      } else {
        AbstractDungeon.cardRewardScreen.discoveryOpen(this.cardType);
      }
      tickDuration();
      return;
    }
    if (!this.retrieveCard)
    {
      if (AbstractDungeon.cardRewardScreen.discoveryCard != null)
      {
        AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
        disCard.current_x = (-1000.0F * Settings.scale);
        if (AbstractDungeon.player.hand.size() < 10) {
          AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        } else {
          AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        }
        disCard.upgrade();
        AbstractDungeon.cardRewardScreen.discoveryCard = null;
      }
      this.retrieveCard = true;
    }
    tickDuration();
  }
}
