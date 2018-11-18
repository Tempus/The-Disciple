package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.actions.EndTurnAction;

public class HangingClock extends CustomRelic {
    public static final String ID = "HangingClock";
    private static final int COUNTDOWN_AMT = 12;

    public HangingClock() {
        super(ID, new Texture("chrono_images/relics/HangingClock.png"), new Texture("chrono_images/relics/outline/HangingClock.png"), RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + " NL " + this.DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    public void onEquip()
    {
      AbstractDungeon.player.energy.energyMaster += 1;
    }
    
    public void onUnequip()
    {
      AbstractDungeon.player.energy.energyMaster -= 1;
    }


    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        flash();
        this.counter += 1;
        if (this.counter == COUNTDOWN_AMT)
        {
            this.counter = 0;
            AbstractDungeon.actionManager.addToTop(new EndTurnAction());
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HangingClock();
    }
}