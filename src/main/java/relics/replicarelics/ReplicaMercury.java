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

public class ReplicaMercury extends CustomRelic {
    public static final String ID = "Mercury?";

    public ReplicaMercury() {
        super(ID, new Texture("chrono_images/relics/Mercury.png"), new Texture("chrono_images/relics/outline/Mercury.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip("Mercury", "Deals #g3 damage to ALL enemies at the start of your turn. NL #pShatters #pin #b5 #pturns."));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedMercury(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaMercury(); }
}