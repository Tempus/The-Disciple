package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
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
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.megacrit.cardcrawl.cards.DamageInfo;

import com.megacrit.cardcrawl.actions.common.DamageAction;

public class DiscardAndDamageAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private int damagePerCard;
    private int amount;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("UnlockButton");
    public static final String[] TEXT = uiStrings.TEXT;

    public DiscardAndDamageAction(AbstractCreature target, int baseDamage, int amount) {
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.damagePerCard = baseDamage;
        this.amount = amount;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
                return;
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0] + this.amount + TEXT[1], this.amount, true, true);
                AbstractDungeon.player.hand.applyPowers();
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this.target, new DamageInfo(this.player, this.damagePerCard, DamageInfo.DamageType.NORMAL), AttackEffect.SLASH_VERTICAL));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();
    }
}

// public class DiscardAndDamageAction
//   extends AbstractGameAction
// {
//   // private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("DiscardAndDamageAction");
//   // public static final String[] TEXT = uiStrings.TEXT;
//   private AbstractPlayer p;
//   private AbstractMonster target;
//   public static int damage;
//   private static final float DURATION = Settings.ACTION_DUR_XFAST;
  
//   public DiscardAndDamageAction(AbstractMonster target, int damage)
//   {
//     this.p = AbstractDungeon.player;
//     this.target = target;
//     this.actionType = AbstractGameAction.ActionType.DISCARD;
//     this.damage = damage;
//     this.duration = DURATION;
//   }
  
//   public void update()
//   {
//     if (this.duration == DURATION)
//     {
//       if (AbstractDungeon.getMonsters().areMonstersBasicallyDead())
//       {
//         this.isDone = true;
//         return;
//       }
//       if (this.p.hand.size() <= this.amount)
//       {
//         this.amount = this.p.hand.size();
//         int tmp = this.p.hand.size();
//         for (int i = 0; i < tmp; i++)
//         {
//           AbstractCard c = this.p.hand.getTopCard();
//           this.p.hand.moveToDiscardPile(c);
//           c.triggerOnManualDiscard();
//           GameActionManager.incrementDiscard(false);
//           AbstractDungeon.actionManager.addToBottom(
//             new DamageAction(this.target, new DamageInfo(this.p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
//         }
//         AbstractDungeon.player.hand.applyPowers();
//         tickDuration();
//         return;
//       }
//         AbstractDungeon.handCardSelectScreen.open("Discard any number of cards.", 99, true, true);
//         AbstractDungeon.player.hand.applyPowers();
//         tickDuration();
//     }
//     if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
//     {
//       for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
//       {
//         this.p.hand.moveToDiscardPile(c);
//         c.triggerOnManualDiscard();
//         GameActionManager.incrementDiscard(false);
//         AbstractDungeon.actionManager.addToBottom(
//           new DamageAction(this.target, new DamageInfo(this.p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
//       }
//       AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
//     }
//     tickDuration();
//   }
// }
