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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.WakeUpCallPower;

import java.lang.Math;
import java.util.Iterator;
import basemod.helpers.TooltipInfo;

import java.util.ArrayList;
import java.util.List;

public class Recurrance extends MetricsCard {
	public static final String ID = "Recurrance";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 3;
	private static final int HITS = 2;
	private static final int HITS_UPGRADE = 0;
	public ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();

	public Recurrance() {
		super(ID, NAME, "chrono_images/cards/Recurrance.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
		this.baseMagicNumber = HITS;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		for (int hit = 0; hit < this.magicNumber; hit++) {
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
		}

		AbstractPower pow;
	    for (Object e = m.powers.iterator(); ((Iterator)e).hasNext();) {
      		pow = (AbstractPower)((Iterator)e).next();
	    	if (!m.hasPower(pow.ID)) { continue; }

			if (pow.type == AbstractPower.PowerType.DEBUFF) {
				if (pow.canGoNegative == true && pow.amount < 0) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, -this.magicNumber, true));
				} else if (pow.ID != "Shackled") {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, pow, this.magicNumber, true));
				}
			} else if (pow.ID.contains("DelayedAttack") || pow.ID.contains("TheBomb")) {
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(m, p, pow, this.magicNumber));
			} else if (pow.ID.contains("WakeUpCall")) {
				((WakeUpCallPower)pow).damage.base = ((WakeUpCallPower)pow).damage.base + this.magicNumber;
				((WakeUpCallPower)pow).updateDescription();
			}
	    }
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		this.tips.clear();
		
		if (AbstractDungeon.getCurrMapNode() == null) { return this.tips; }
		if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) { return this.tips; }

	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDead && !mo.escaped) {
				if (mo.hasPower("DelayedAttack")) {
					this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
				}
			}
		}

	    return this.tips;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(2);
			// upgradeMagicNumber(HITS_UPGRADE);
		}
	}
}