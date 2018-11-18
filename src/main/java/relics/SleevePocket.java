package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

import java.util.ArrayList;

public class SleevePocket extends CustomRelic {
    public static final String ID = "SleevePocket";
    public ArrayList<AbstractCard> cards = new ArrayList();

    public SleevePocket() {
        super(ID, new Texture("chrono_images/relics/SleevePocket.png"), new Texture("chrono_images/relics/outline/SleevePocket.png"), RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void onEquip()
    {
      if (AbstractDungeon.player.masterDeck.getPurgeableCards().size() > 0)
      {
        if (AbstractDungeon.isScreenUp)
        {
          AbstractDungeon.dynamicBanner.hide();
          AbstractDungeon.overlayMenu.cancelButton.hide();
          AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck
          .getPurgeableCards(), 5, true, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD);
      }
    }
        
    public void update()
    {
      super.update();
      if (!AbstractDungeon.gridSelectScreen.confirmScreenUp && this.cards.size()==0)
      {
        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          cards.add(c);
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();

        this.setCardListDescription();
      }
    }
    
    public void setCardListDescription() {
        ArrayList<String> cardNames = new ArrayList();
        for (AbstractCard c: this.cards) {
            cardNames.add(c.name);
        }

        if (this.cards.size() == 0) {return;}
        else if (this.cards.size() > 1) {
            cardNames.set(cardNames.size()-1, "or " + cardNames.get(cardNames.size()-1));
        }

        this.description = (this.DESCRIPTIONS[2] + String.join(", ", cardNames) + this.DESCRIPTIONS[3]);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void setDescriptionAfterLoading()
    {
        this.setCardListDescription();
    }

    public void atTurnStart() {
        for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
            ChronoMod.log("Looking for " + c.name);
            if (cards.contains(c)) {
                AbstractDungeon.player.hand.addToHand(c);
                AbstractDungeon.player.drawPile.removeCard(c);
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.gameHandSize--;
                return;
            }
        }
    }

    public void onPlayerEndTurn() {
        AbstractDungeon.player.gameHandSize = AbstractDungeon.player.masterHandSize;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SleevePocket();
    }
}