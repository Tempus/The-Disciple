package chronomuncher.cards;

import basemod.abstracts.CustomCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.interfaces.OnCardUseSubscriber;

public abstract class MetricsCard extends CustomCard {

	public MetricsCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
	    if (this.name == null) {
	    	this.name = "";
	    }
	    if (this.rawDescription == null) {
	        this.rawDescription = "";
	    }
	}

	@Override
	public void onMoveToDiscard() {
    	ChronoMod.addCardDiscard(this.name);
	}
}