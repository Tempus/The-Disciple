package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;


public class Accruing extends AbstractSelfRetainingCard {
	public static final String ID = "Accruing";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int ATTACK_DMG = 0;

	public Accruing() {
		super(ID, NAME, "chrono_images/cards/Accruing.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
	}

	@Override
	public void atTurnStart() {
		this.baseDamage = fibbonacci(AbstractDungeon.player.hand.size());
	}

	@Override
	public void triggerOnOtherCardPlayed(AbstractCard c) {
		this.baseDamage = fibbonacci(AbstractDungeon.player.hand.size());
	}

    @Override
    public void applyPowers() {
        this.baseDamage = fibbonacci(AbstractDungeon.player.hand.size());
        super.applyPowers();
        initializeDescription();
    }

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.retains = true;
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}

	public static int fibbonacci(int position) {
		return position > 1 ? fibbonacci(position - 1) + fibbonacci(position - 2) : position; 
	}
}