package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.powers.TimeDilationPower;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import basemod.helpers.TooltipInfo;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.TimeDilatePower;

import java.util.ArrayList;
import java.util.List;

public class Pendulum extends MetricsCard {
	public static final String ID = "Pendulum";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	// public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int TURNS_APPLIED = 3;
	private static final int TURNS_APPLIED_UP = 2;
	public ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();

	public Pendulum() {
		super(ID, NAME, "chrono_images/cards/Pendulum.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);

		this.baseMagicNumber = TURNS_APPLIED;
		this.magicNumber = TURNS_APPLIED;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new TimeDilatePower(m, this.magicNumber), this.magicNumber, true));
		AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-TICKINGCLEAN"));
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		this.tips.clear();

		if (AbstractDungeon.getCurrMapNode() == null) { return this.tips; }
		if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) { return this.tips; }
		
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDead && !mo.escaped) {
				if (mo.hasPower("DelayedAttack")) {
					this.tips.add(new TooltipInfo("Delayed Attack", "Pendulum will cause a time paradox, causing the delayed attack to occur immediately AND be reapplied once the time dilation wears off."));
				}
			}
		}

	    return this.tips;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(TURNS_APPLIED_UP);
      		// this.rawDescription = UPGRADE_DESCRIPTION;
   		   // 	initializeDescription();
		}
	}
}