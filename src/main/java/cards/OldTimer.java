package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class OldTimer extends MetricsCard {
    public static final String ID = "OldTimer";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final int COST = 2;
    private static final int ATTACK_DMG = 1;
    private static final int UPGRADE_ENERGY = 1;

    public OldTimer() {
        super(ID, NAME, "chrono_images/cards/OldTimer.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
                Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE,
                AbstractCard.CardTarget.ENEMY);

        this.baseDamage = 0;
        this.baseMagicNumber = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-CHIME"));
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void atTurnStart() {
        this.baseDamage = (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() * this.magicNumber);
        initializeDescription();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        this.baseDamage = (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() * this.magicNumber);
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() * this.magicNumber);
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }
}