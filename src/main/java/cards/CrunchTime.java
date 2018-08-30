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
		super(ID, NAME, "images/cards/CrunchTime.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.BRONZE, AbstractCard.CardRarity.COMMON,
				AbstractCard.CardTarget.ENEMY);

		this.baseDamage = ATTACK_DMG;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		if (p.orbs.size() == 0) { return; }

		ReplicaOrb r = (ReplicaOrb)p.orbs.get(p.orbs.size()-1);
		int hits = r.timer;

		AbstractDungeon.actionManager.addToBottom(new ShatterAction(r));

		// SANE MULTI-HITS? OR MAYBE COPY PASTA WAS RIGHT ALL ALONG
		for (int hit = 0; hit < hits; hit++) {
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new CrunchTime();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(ATTACK_UPGRADE);
		}
	}
}