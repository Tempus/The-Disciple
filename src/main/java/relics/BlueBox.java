package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

import java.util.ArrayList;
import java.util.HashMap;

public class BlueBox extends CustomRelic {
    public static final String ID = "BlueBox";
    private boolean cardSelected = true;
    public static final RelicStrings relicString = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String TEXT[] = relicString.DESCRIPTIONS;

    public BlueBox() {
        super(ID, new Texture("chrono_images/relics/BlueBox.png"), new Texture("chrono_images/relics/outline/BlueBox.png"), RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public void onEquip() {
        CardGroup timeWarpCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (HashMap<String, Object> cardChoice : CardCrawlGame.metricData.card_choices) {
            for (String card : (ArrayList<String>)cardChoice.get("not_picked")) {
                String cardName = card;
                boolean upgraded = false;
                if (cardName.indexOf("+") != -1) {
                    cardName = cardName.substring(0, cardName.indexOf("+"));
                    upgraded = true;
                }

                ChronoMod.log("Adding "+ cardName + " to time warp");
                AbstractCard addedCard = CardLibrary.getCopy(cardName);
                if (upgraded) { addedCard.upgrade(); }
                timeWarpCards.addToTop(addedCard);
            }
        }

        if (timeWarpCards.size() <= 0) { return; }

        this.cardSelected = false;
        if (AbstractDungeon.isScreenUp)
        {
          AbstractDungeon.dynamicBanner.hide();
          AbstractDungeon.overlayMenu.cancelButton.hide();
          AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        
        AbstractDungeon.gridSelectScreen.open(timeWarpCards, 2, TEXT[1], false, false, false, false);
    }

    public void update()
    {
      super.update();
      if ((!this.cardSelected) && 
        (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2))
      {
        this.cardSelected = true;
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
            ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
            ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(1)).makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
    
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
      }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlueBox();
    }
}

// Blue Box - Tardis
// Stolen Pendant - Chrono Trigger

// Goo Banana - Stein's Gate
