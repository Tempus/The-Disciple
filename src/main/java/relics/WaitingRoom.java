package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.powers.SleepPower;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.powers.HastePower;

public class WaitingRoom extends CustomRelic {
    public static final String ID = "WaitingRoom";

    public WaitingRoom() {
        super(ID, new Texture("chrono_images/relics/WaitingRoom.png"), new Texture("chrono_images/relics/outline/WaitingRoom.png"), RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    public void atBattleStart() {
        beginLongPulse();
    }

    public void onPlayerEndTurn() {
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0 && this.pulse) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new SFXAction("SLEEP_BLANKET"));
            AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-SHORTSLEEP"));
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDead && !mo.escaped) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo,AbstractDungeon.player,new SleepPower(mo, 1),1));
                }
            }   
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WaitingRoom();
    }
}