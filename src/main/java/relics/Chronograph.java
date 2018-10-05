package chronomuncher.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Chronograph extends CustomRelic {
    public static final String ID = "Chronograph";

    private AbstractPlayer p;

    public Chronograph() {
        super(ID, new Texture("chrono_images/relics/Chronograph.png"), new Texture("chrono_images/relics/outline/Chronograph.png"), RelicTier.BOSS, LandingSound.CLINK);
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
    public void obtain() {
        if (AbstractDungeon.player.hasRelic("Chronometer")) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals("Chronometer")) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Chronograph();
    }
}