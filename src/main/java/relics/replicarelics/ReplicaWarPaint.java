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

public class ReplicaWarPaint extends CustomRelic {
    public static final String ID = "WarPaint?";

    public ReplicaWarPaint() {
        super(ID, new Texture("chrono_images/relics/WarPaint.png"), new Texture("chrono_images/relics/outline/WarPaint.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip("War Paint", "Each turn, upgrade a Skill in your hand for the rest of combat. NL #pShatters #pin #b4 #pturns."));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedWarPaint(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaWarPaint(); }
}