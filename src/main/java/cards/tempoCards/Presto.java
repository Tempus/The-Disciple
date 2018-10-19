package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import chronomuncher.actions.PlayExhaustedCardAction;
import chronomuncher.actions.SmokeBombAction;

import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import chronomuncher.powers.HastePower;
import chronomuncher.powers.SleepPower;
import chronomuncher.powers.StunPower;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.GainGoldAction;

public class Presto extends MetricsCard {
	public static final String ID = "Presto";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 2;

	private static final int DMG = 12;
	private static final int UPGRADE_PLUS_DMG = 5;

	private static final int BLOCK = 10;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;

	public Presto() {
		super(ID, NAME, "chrono_images/cards/Presto.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE);

		// this.baseDamage = DMG;
		
		// this.baseBlock = BLOCK;

		// this.baseMagicNumber = MAGIC;
		// this.magicNumber = UPGRADE_PLUS_MAGIC;

    	this.tags.add(Enum.TEMPO_CARD);
    	this.isEthereal = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
  		if (this.upgraded) {
			int increaseGold = 0;
	
		    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				if (!mo.isDead && !mo.escaped) {
					if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
			            increaseGold = AbstractDungeon.treasureRng.random(25, 35) / AbstractDungeon.getCurrRoom().monsters.monsters.size(); }
			        else if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
			            increaseGold = AbstractDungeon.treasureRng.random(10, 20) / AbstractDungeon.getCurrRoom().monsters.monsters.size(); }

			  		AbstractDungeon.actionManager.addToBottom(new GainGoldAction(mo, increaseGold));
			    }
			}
		}

  		AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
  		AbstractDungeon.actionManager.addToBottom(new SmokeBombAction(this.upgraded));
	}

	@Override
	public AbstractCard makeCopy() {
		return new Presto();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
			// upgradeDamage(UPGRADE_PLUS_DMG);
			// upgradeBlock(UPGRADE_PLUS_BLOCK);
			// upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
		}
	}
}