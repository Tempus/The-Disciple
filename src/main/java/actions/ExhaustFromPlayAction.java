package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import java.util.ArrayList;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import chronomuncher.ChronoMod;

// public class ExhaustFromPlayAction extends AbstractGameAction {
//   private AbstractPlayer p;
  
//   public ExhaustFromPlayAction(int amount)
//   {
//     this.p = AbstractDungeon.player;
//     setValues(this.p, AbstractDungeon.player, amount);
//     this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
//     this.duration = Settings.ACTION_DUR_MED;
//   }
  
//   public void update()
//   {
//     CardGroup tmp;
//     String cardGroup;
//     if (this.duration == Settings.ACTION_DUR_MED)
//     {
//       tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
//       for (AbstractCard c : this.p.drawPile.group) {
//         tmp.addToRandomSpot(c);
//         c.cantUseMessage = "drawPile";
//       }
//       for (AbstractCard c : this.p.hand.group) {
//         tmp.addToBottom(c);
//         c.cantUseMessage = "hand";
//       }
//       for (AbstractCard c : this.p.discardPile.group) {
//         tmp.addToTop(c);
//         c.cantUseMessage = "discardPile";
//       }
//       if (tmp.size() == 0)
//       {
//         this.isDone = true;
//         return;
//       }
//       if (tmp.size() == 1)
//       {
//         AbstractCard card = tmp.getTopCard();
//         card.unhover();
//         card.lighten(true);
//         card.setAngle(0.0F);
//         card.drawScale = 0.12F;
//         card.targetDrawScale = 0.75F;
//         card.current_x = CardGroup.DRAW_PILE_X;
//         card.current_y = CardGroup.DRAW_PILE_Y;
//         switch (card.cantUseMessage) {
//           case "drawPile":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.drawPile));
//             break;
//           case "hand":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.hand));
//             break;
//           case "discardPile":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.discardPile));
//             break;
//         }

//         this.isDone = true;
//         return;
//       }
//       if (tmp.size() <= this.amount)
//       {
//         for (int i = 0; i < tmp.size(); i++)
//         {
//           AbstractCard card = tmp.getNCardFromTop(i);
//           card.unhover();
//           card.lighten(true);
//           card.setAngle(0.0F);
//           card.drawScale = 0.12F;
//           card.targetDrawScale = 0.75F;
//           card.current_x = CardGroup.DRAW_PILE_X;
//           card.current_y = CardGroup.DRAW_PILE_Y;
//           switch (card.cantUseMessage) {
//             case "drawPile":
//               AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.drawPile));
//               break;
//             case "hand":
//               AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.hand));
//               break;
//             case "discardPile":
//               AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, this.p.discardPile));
//               break;
//           }
//         }
//         this.isDone = true;
//         return;
//       }
//       if (this.amount == 1) {
//         AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose a card to Exhaust.", false);
//       } else {
//         AbstractDungeon.gridSelectScreen.open(tmp, this.amount, "Choose " + Integer.toString(this.amount) + " cards to Exhaust.", false);
//       }
//       tickDuration();
//       return;
//     }
//     if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
//     {
//       for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
//       {
//         c.unhover();
//         switch (c.cantUseMessage) {
//           case "drawPile":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, this.p.drawPile));
//             break;
//           case "hand":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, this.p.hand));
//             break;
//           case "discardPile":
//             AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(c, this.p.discardPile));
//             break;
//         }
//       }
//       AbstractDungeon.gridSelectScreen.selectedCards.clear();
//     }
//     tickDuration();
//   }
// }

public class ExhaustFromPlayAction extends AbstractGameAction {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("UnlockButton");
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer player;
    private int numberOfCards;
    private ArrayList<AbstractCard> cardsInDraw = new ArrayList<>();
    private ArrayList<AbstractCard> cardsInHand = new ArrayList<>();
    private ArrayList<AbstractCard> cardsInDiscard = new ArrayList<>();

    public ExhaustFromPlayAction(int numberOfCards) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration) {
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.player.drawPile.group) {
                this.cardsInDraw.add(c);
                temp.addToTop(c);
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            for (AbstractCard c : this.player.hand.group) {
                this.cardsInHand.add(c);
                temp.addToBottom(c);
                c.beginGlowing();
            }
            for (AbstractCard c : this.player.discardPile.group) {
                this.cardsInDiscard.add(c);
                temp.addToTop(c);
            }
            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            }
            else if (temp.size() <= this.numberOfCards) {
                for (AbstractCard c : temp.group) {
                    if (this.cardsInDraw.contains(c)) {
                        this.player.drawPile.moveToExhaustPile(c);
                    }
                    else if (this.cardsInHand.contains(c)) {
                        this.player.hand.moveToExhaustPile(c);
                    }
                    else if (this.cardsInDiscard.contains(c)) {
                        this.player.discardPile.moveToExhaustPile(c);
                    }
                    else {
                        ChronoMod.log("WTF? Where'd it go?!");
                    }
                }
                this.player.hand.glowCheck();
                this.isDone = true;
                return;
            }
            else if (this.numberOfCards == 1) {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false);
            }
            else {
                AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (this.cardsInDraw.contains(c)) {
                    this.player.drawPile.moveToExhaustPile(c);
                }
                else if (this.cardsInHand.contains(c)) {
                    this.player.hand.moveToExhaustPile(c);
                }
                else if (this.cardsInDiscard.contains(c)) {
                    this.player.discardPile.moveToExhaustPile(c);
                }
                else {
                    ChronoMod.log("WTF? Where'd it go?!");
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.player.hand.refreshHandLayout();
            this.player.hand.glowCheck();
        }
        tickDuration();
    }
}