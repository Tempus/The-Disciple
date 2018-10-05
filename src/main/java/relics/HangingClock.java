package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.powers.TimeWarpMinorPower;

public class HangingClock extends CustomRelic {
    public static final String ID = "HangingClock";

    public HangingClock() {
        super(ID, new Texture("chrono_images/relics/HangingClock.png"), new Texture("chrono_images/relics/outline/HangingClock.png"), RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + " NL " + this.DESCRIPTIONS[1];
    }
    
    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TimeWarpMinorPower(AbstractDungeon.player), 1));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public void onEquip()
    {
      AbstractDungeon.player.energy.energyMaster += 1;
    }
    
    public void onUnequip()
    {
      AbstractDungeon.player.energy.energyMaster -= 1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HangingClock();
    }
}