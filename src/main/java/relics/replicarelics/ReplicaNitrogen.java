package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.orbs.*;
import chronomuncher.actions.*;

public class ReplicaNitrogen extends CustomRelic {
    public static final String ID = "Nitrogen?";
    public static final RelicStrings relicString = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String TEXT[] = relicString.DESCRIPTIONS;

    public ReplicaNitrogen() {
        super(ID, new Texture("chrono_images/relics/Nitrogen.png"), new Texture("chrono_images/relics/outline/Nitrogen.png"), RelicTier.SPECIAL, LandingSound.CLINK);

        this.tips.add(new PowerTip(TEXT[1], TEXT[2]));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() { return this.DESCRIPTIONS[0]; }
    
    @Override
    public void atBattleStartPreDraw() {
        flash();
        AbstractDungeon.player.energy.use(1);
        AbstractDungeon.actionManager.addToBottom(new ChronoChannelAction(new UnlockedNitrogen(false)));
    }

    @Override
    public AbstractRelic makeCopy() { return new ReplicaNitrogen(); }
}