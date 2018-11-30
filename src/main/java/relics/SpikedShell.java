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
        int y = 0;
        if (AbstractDungeon.player != null) {
            y = AbstractDungeon.player.cardsPlayedThisTurn;
        }

        return this.DESCRIPTIONS[0] + fibbonacci(y) + this.DESCRIPTIONS[1] + (fibbonacci(y)+1) + this.DESCRIPTIONS[2];
    }
    
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        flash();
        this.counter = fibbonacci(AbstractDungeon.player.cardsPlayedThisTurn);
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void trigger() {
        if (AbstractDungeon.player.hand.size() + AbstractDungeon.player.limbo.size() == 0) {
            flash();
            this.counter = fibbonacci(AbstractDungeon.actionManager.cardsPlayedThisTurn.size());
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(AbstractDungeon.getRandomMonster(), new DamageInfo(AbstractDungeon.player, fibbonacci(AbstractDungeon.player.cardsPlayedThisTurn), DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.reset();
        }
    }

    public void onPlayerEndTurn() { this.reset(); }
    public void atTurnStart() { this.reset(); this.counter = 0; }
    public void onVictory() { this.reset(); }

    public void reset() {
        this.counter = -1;
        this.description = getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public static int fibbonacci(int position) {
        return position > 1 ? fibbonacci(position - 1) + fibbonacci(position - 2) : position; 
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SpikedShell();
    }
}