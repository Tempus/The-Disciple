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

import com.badlogic.gdx.graphics.Texture;
import basemod.abstracts.CustomRelic;
import chronomuncher.ChronoMod;
import chronomuncher.powers.HastePower;

public class SpikedShell extends CustomRelic {
    public static final String ID = "SpikedShell";

    public SpikedShell() {
        super(ID, new Texture("chrono_images/relics/SpikedShell.png"), new Texture("chrono_images/relics/outline/SpikedShell.png"), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + (fibbonacci(AbstractDungeon.actionManager.cardsPlayedThisTurn.size())+1) + this.DESCRIPTIONS[1];
    }
    
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        flash();
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void trigger() {
        if (AbstractDungeon.player.hand.size() == 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(AbstractDungeon.getRandomMonster(), new DamageInfo(AbstractDungeon.player, fibbonacci(AbstractDungeon.actionManager.cardsPlayedThisTurn.size()), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public static int fibbonacci(int position) {
        return position > 1 ? fibbonacci(position - 1) + fibbonacci(position - 2) : position; 
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpikedShell();
    }
}