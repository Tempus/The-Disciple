package chronomuncher.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Chronometer extends CustomRelic {
    public static final String ID = "Chronometer";

    private AbstractPlayer p;

    public Chronometer() {
        super(ID, new Texture("chrono_images/relics/Chronometer.png"), new Texture("chrono_images/relics/outline/Chronometer.png"), RelicTier.STARTER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    // Called from onPowersModified Subscriber
    public void removeConfusion() {
        p = AbstractDungeon.player;
        if (p.hasPower("Confusion")) {
            p.powers.remove(p.getPower("Confusion"));
            AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F - 48.0F, "Immune"));
        }
        if (p.hasPower("TPH_Confusion")) {
            p.powers.remove(p.getPower("TPH_Confusion"));
            AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F - 48.0F, "Immune"));
        }
    }

    @Override
    public void onUnequip() {
        RelicLibrary.bossList.remove("Chronograph");
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Chronometer();
    }
}