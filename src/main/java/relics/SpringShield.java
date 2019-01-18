package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.PowerTip;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.actions.PlayEchoCardAction;
import chronomuncher.cards.Ward;

public class SpringShield extends CustomRelic {
    public static final String ID = "SpringShield";

    public SpringShield() {
        super(ID, new Texture("chrono_images/relics/SpringShield.png"), new Texture("chrono_images/relics/outline/SpringShield.png"), RelicTier.COMMON, LandingSound.CLINK);

        this.tips.add(new PowerTip("Delayed Effects", "Delayed Effects include Bite Command, Rage Command, Hands Up, Wake Up Call, and The Bomb."));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public void onPlayerEndTurn() {
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDead && !mo.escaped) {
                for (AbstractPower p : mo.powers) {
                    if (p.ID.contains("DelayedAttack") || 
                        p.ID.contains("WakeUpCall") || 
                        p.ID.contains("TheBomb") || 
                        p.ID.contains("DelayedLoseStrength") || 
                        p.ID.contains("DelayedBlock")) {
                        flash();

                        AbstractCard ward = new Ward();
                        ward.current_x = this.currentX;
                        ward.current_y = this.currentY;
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, 2));
                        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                    }
                }
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpringShield();
    }
}