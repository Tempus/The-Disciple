package chronomuncher.cards;

import basemod.abstracts.CustomCard;
import chronomuncher.cards.MetricsCard;
import chronomuncher.patches.Enum;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import basemod.interfaces.OnCardUseSubscriber;
import chronomuncher.patches.RetainedForField;

public abstract class AbstractSelfRetainingCard extends MetricsCard {
	public boolean retains = false;

	public AbstractSelfRetainingCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
	}

	@Override
	public void update() {
		super.update();
		if (CardCrawlGame.isInARun()) {
			if (this.retains && !AbstractDungeon.player.hasRelic("HeavySwitch")) {
				this.retain = true;
			}
		}
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		RetainedForField.retainedFor.set(this, 0);
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
	}
}