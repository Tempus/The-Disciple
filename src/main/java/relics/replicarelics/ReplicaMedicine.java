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

public class ReplicaMedicine extends CustomRelic {
    public static final String ID = "Medicine?";

    public ReplicaMedicine() {
        super(ID, new Texture("chrono_images/relics/Medicine.png"), new Texture("chrono_images/relics/outline/Medicine.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip("Medicine", "Whenever you draw a #yStatus card, #yExhaust it and draw a card. NL #pShatters #pin #b7 #pturns."));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.player.energy.use(1);
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedMedicine(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaMedicine(); }
}