package chronomuncher.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class Lockbox extends CustomRelic {
    public static final String ID = "Lockbox";

    public Lockbox() {
        super(ID, new Texture("chrono_images/relics/Lockbox.png"), new Texture("chrono_images/relics/outline/Lockbox.png"), RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public void atBattleStart() {
      flash();
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
      AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(1));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Lockbox();
    }
}