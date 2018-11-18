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

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.powers.RetainOncePower;

public class HeavySwitch extends CustomRelic {
    public static final String ID = "HeavySwitch";

    public HeavySwitch() {
        super(ID, new Texture("chrono_images/relics/HeavySwitch.png"), new Texture("chrono_images/relics/outline/HeavySwitch.png"), RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public void onPlayerEndTurn() {
        
        for (AbstractCard c: AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractSelfRetainingCard) {
            if (((AbstractSelfRetainingCard)c).retains) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RetainOncePower(1), 1));
            }}
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HeavySwitch();
    }
}