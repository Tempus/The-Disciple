package chronomuncher.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Chronometer extends CustomRelic {
    public static final String ID = "Chronometer";

    private AbstractPlayer p;

    public Chronometer() {
        super(ID, new Texture("images/relics/Chronometer.png"), RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    // Depreciated, using Patch instead.
    public void cardCostDecrease() {
        // Retained Cards cost 1 less energy

        // Grabs all cards
        // Iterate over each card to see if it was retained
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            ChronoMod.log(c.name + ": " + Boolean.toString(c.retain));
            if (c.retain) {
                ChronoMod.log("Was retained.");
                // Flash and decrease energy
                flash();
                c.modifyCostForTurn(-1);
            }
        }

        // No Confuse
        this.removeConfusion();
    }
    
    public void atPreBattle()               { this.removeConfusion(); }
    public void atBattleStart()             { this.removeConfusion(); }
    public void atBattleStartPreDraw()      { this.removeConfusion(); }
    public void atTurnStart()               { this.removeConfusion(); }
    public void atTurnStartPostDraw()       { this.removeConfusion(); }
    public void onCardDraw(AbstractCard c)  { this.removeConfusion(); }
    public void onDrawOrDiscard()           { this.removeConfusion(); }

    public void removeConfusion() {
        this.p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.p, this.p, "Confusion"));
    }

    @Override
    public void onUnequip() {
        RelicLibrary.bossList.remove("Chronograph");
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Chronometer();
    }
}