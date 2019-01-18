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

public class ReplicaWhetstone extends CustomRelic {
    public static final String ID = "Whetstone?";

    public ReplicaWhetstone() {
        super(ID, new Texture("chrono_images/relics/Whetstone.png"), new Texture("chrono_images/relics/outline/Whetstone.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip("Whetstone", "Each turn, upgrade an Attack in your hand for the rest of combat. NL #pShatters #pin #b4 #pturns."));
        this.initializeTips();
   }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedWhetstone(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaWhetstone(); }
}