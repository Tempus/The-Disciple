package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;

import com.badlogic.gdx.graphics.Color;

public class Parity extends MetricsCard {
	public static final String ID = "Parity";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	private static final int COST = 1;

	private static final int DMG = 11;
	private static final int UPGRADE_PLUS_DMG = 4;

	private static final int BLOCK = 8;
	private static final int UPGRADE_PLUS_BLOCK = 4;

	private static final String ATTACKIMG = "chrono_images/cards/Parity.png";
	private static final String SKILLIMG = "chrono_images/cards/ParityS.png";	

	public Parity() {
		super(ID, NAME, SKILLIMG, COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.SELF);

		this.baseDamage = DMG;
		this.baseBlock = BLOCK;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		if (AbstractDungeon.actionManager.turn % 2 == 0) {
			AbstractDungeon.actionManager.addToBottom(
				new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		} else {
			AbstractDungeon.actionManager.addToBottom(
				new GainBlockAction(p, p, this.block));
		}
	}
	
	public void update() {
		super.update();
		if (AbstractDungeon.currMapNode != null) { 
			if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
				if (AbstractDungeon.player != null) {
					if (AbstractDungeon.player.masterDeck.contains(this)) {
						this.rawDescription = DESCRIPTION;
				      	initializeDescription();
					} else {
						this.updateParity();
					}
				} else if (this.rawDescription != DESCRIPTION) {
					this.rawDescription = DESCRIPTION;
				    initializeDescription();
				}
			}
		}
	}

	public void updateParity() {
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			if (AbstractDungeon.actionManager.turn % 2 == 0) {
				if (this.rawDescription == EXTENDED_DESCRIPTION[0]) {return;}
				this.superFlash(Color.CORAL.cpy());
				this.target = AbstractCard.CardTarget.ENEMY;
				this.type = AbstractCard.CardType.ATTACK;
				this.loadCardImage(ATTACKIMG);
				this.rawDescription = EXTENDED_DESCRIPTION[0];
			} else if (AbstractDungeon.actionManager.turn % 2 == 1) {
				if (this.rawDescription == EXTENDED_DESCRIPTION[1]) {return;}
				this.superFlash(Color.LIME.cpy());
				this.target = AbstractCard.CardTarget.SELF;
				this.type = AbstractCard.CardType.SKILL;
				this.loadCardImage(SKILLIMG);
				this.rawDescription = EXTENDED_DESCRIPTION[1];
			}
		} else {
			this.rawDescription = DESCRIPTION;
		}
      	initializeDescription();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
			upgradeBlock(UPGRADE_PLUS_BLOCK);
		}
	}
}