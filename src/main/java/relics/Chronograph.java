package chronomuncher.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Chronograph extends CustomRelic {
    public static final String ID = "Chronograph";

    private AbstractPlayer p;

    public Chronograph() {
        super(ID, new Texture("images/relics/Chronograph.png"), RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
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
    public void obtain() {
        if (AbstractDungeon.player.hasRelic("Chronometer")) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals("Chronometer")) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Chronograph();
    }
}