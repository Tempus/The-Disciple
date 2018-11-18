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
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.orbs.ReplicaOrb;
import chronomuncher.actions.ShatterAction;

import java.lang.Math;

// Break Down
// Detonate
// Knock-off
// Split Second
// Crunch Time

public class CrunchTime extends MetricsCard {
	public static final String ID = "CrunchTime";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 5;
	private static final int ATTACK_UPGRADE = 3;

	public CrunchTime() {
		super(ID, NAME, "chrono_images/cards/CrunchTime.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		// Return if there are no orbs
		if (p.orbs.size() == 0) { return; }

		// Get the newest Replica orb with a non-zero timer
		ReplicaOrb r = null;
		for (AbstractOrb o : p.orbs) {
            if (o instanceof ReplicaOrb) {
            	if (((ReplicaOrb)o).timer > 0) {
                	r = (ReplicaOrb)o;
            	}
            }
		}

		if (r == null) { return; }

		// Read the timer
		int hits = r.timer;

		// Shatter it
		AbstractDungeon.actionManager.addToBottom(new ShatterAction(r));

		// Hit a whole bunch
		for (int hit = 0; hit < hits; hit++) {
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(ATTACK_UPGRADE);
		}
	}
}