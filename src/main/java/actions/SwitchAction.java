package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

import chronomuncher.ChronoMod;
import chronomuncher.cards.AbstractSelfSwitchCard;

public class SwitchAction extends AbstractGameAction
{
  
  public AbstractSelfSwitchCard card;

  public SwitchAction(AbstractSelfSwitchCard card)
  {
    this.card = card;
    this.duration = Settings.ACTION_DUR_MED;
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
  }
    
  public void update()
  {
    if (this.duration == Settings.ACTION_DUR_MED)
    {
      CardCrawlGame.sound.play("HEAL_1");
      this.card.superFlash();
      this.card.lighten(false);
      this.card.switchCardInDeck();
      this.card.switchTo(this.card.switchID);
    }
    tickDuration();
    if (this.isDone) {
      CardCrawlGame.sound.play("CARD_OBTAIN");
    }
  }
}
