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

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class ClockandLoad extends MetricsCard {
    public static final String ID = "ClockandLoad";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final int COST = 2;
    private static final int ATTACK_DMG = 1;

    public ClockandLoad() {
        super(ID, NAME, "images/cards/ClockandLoad.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
                Enum.BRONZE, AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardTarget.ENEMY);

        this.baseDamage = 0;
        this.baseMagicNumber = ATTACK_DMG;
        this.magicNumber = this.baseMagicNumber;

        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void atTurnStart() {
        this.baseDamage = (AbstractDungeon.actionManager.energyGainedThisCombat * this.magicNumber);
        initializeDescription();
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        this.baseDamage = (AbstractDungeon.actionManager.energyGainedThisCombat * this.magicNumber);
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = (AbstractDungeon.actionManager.energyGainedThisCombat * this.magicNumber);
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new ClockandLoad();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}