package chronomuncher.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.RetainCardPower;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Metronome extends CustomRelic {
    public static final String ID = "Metronome";
    public static final int CARDS_TO_RETAIN = 1;

    public Metronome() {
        super(ID, new Texture("chrono_images/relics/Metronome.png"), new Texture("chrono_images/relics/outline/Metronome.png"), RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RetainCardPower(AbstractDungeon.player, CARDS_TO_RETAIN), CARDS_TO_RETAIN));
    }

    // public void atEndofTurn() {
    //     // Retained 1 additional card each turn
    //     flash();
    //     if (AbstractDungeon.player.hand.group.size() > 0) {
    //         AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(AbstractDungeon.player, CARDS_TO_RETAIN));
    //     }
    // }
    
    // public void atPreBattle()
    // {
    //     flash();
    //     AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    //     AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
    //     CardCrawlGame.sound.play("CARD_POWER_IMPACT", 0.1F);
    // }
  
    @Override
    public AbstractRelic makeCopy() {
        return new Metronome();
    }
}