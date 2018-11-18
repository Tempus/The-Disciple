package chronomuncher.relics;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;

public class PaperTurtyl extends CustomRelic {
    public static final String ID = "Paper Turtyl";

    public PaperTurtyl() {
        super(ID, new Texture("chrono_images/relics/PaperTurtyl.png"), new Texture("chrono_images/relics/outline/PaperTurtyl.png"), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public void atTurnStart() {

		int temp = AbstractDungeon.getCurrRoom().monsters.monsters.size();
		for (int i = 0; i < temp; i++) {

			// Get the monster
			AbstractMonster mon;
			mon = (AbstractMonster)AbstractDungeon.getCurrRoom().monsters.monsters.get(i);

			// Make sure Monster isn't dead
			if (!mon.isDeadOrEscaped()) {
				// Check to see if the monster has slow
				AbstractPower stackMe = null;
				stackMe = mon.getPower("Slow");

				// If it does, trigger the slow power twice, and make animations
				if (stackMe != null)
				{
			    	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mon, mon, new SlowPower(mon, 3), 3));

			        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(mon, this));
    			}
			}
		}

    }

    @Override
    public AbstractRelic makeCopy() {
        return new PaperTurtyl();
    }
}
