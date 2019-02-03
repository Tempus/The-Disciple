package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.orbs.*;
import chronomuncher.actions.*;

public class ReplicaTornado extends CustomRelic {
    public static final String ID = "Tornado?";
    public static final RelicStrings relicString = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String TEXT[] = relicString.DESCRIPTIONS;

    public ReplicaTornado() {
        super(ID, new Texture("chrono_images/relics/Tornado.png"), new Texture("chrono_images/relics/outline/Tornado.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip(TEXT[1], TEXT[2]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(1));
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedTornado(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaTornado(); }
}