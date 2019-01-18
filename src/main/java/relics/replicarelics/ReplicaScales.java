package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.helpers.PowerTip;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.orbs.*;
import chronomuncher.actions.*;

public class ReplicaScales extends CustomRelic {
    public static final String ID = "Scales?";

    public ReplicaScales() {
        super(ID, new Texture("chrono_images/relics/Scales.png"), new Texture("chrono_images/relics/outline/Scales.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip("Scales", "When receiving #yAttack damage, deals #b3 damage back. NL #pShatters #pin #b6 #pturns."));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.player.energy.use(1);
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedScales(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaScales(); }
}