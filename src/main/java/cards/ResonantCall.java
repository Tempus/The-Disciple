package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.PlayLowerBlockFromDeckAction;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Color;

public class ResonantCall extends AbstractSelfRetainingCard {
	public static final String ID = "ResonantCall";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public AbstractCard mimic;
	public boolean displayMimic = false;
	protected static final float CARD_TIP_PAD = 16.0F;

	private static final int COST = -1;
	// private static final int CARDS_TO_PLAY = 3;
	// private static final int CARDS_TO_PLAY_UP = 2;

	public static final Texture attackEdge = ImageMaster.loadImage("chrono_images/cards/ResonantCallAttack.png");
	public static final Texture skillEdge = ImageMaster.loadImage("chrono_images/cards/ResonantCallSkill.png");
	public static final Texture powerEdge = ImageMaster.loadImage("chrono_images/cards/ResonantCallPower.png");

	public ResonantCall() {
		super(ID, NAME, "chrono_images/cards/ResonantCall.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
	}

	public void triggerOnOtherCardPlayed(AbstractCard c) {
		try {
			if (c.cardID == this.cardID) { return; }
			this.mimic = c.makeStatEquivalentCopy();

			// Flash depending on what you switch to
			if (this.mimic.type == AbstractCard.CardType.ATTACK) {
				this.superFlash(Color.CORAL.cpy());
			} else if (this.mimic.type == AbstractCard.CardType.SKILL) {
				this.superFlash(Color.SKY.cpy());
			} else {
				this.superFlash(Color.LIME.cpy());
			}

			// Update cost
			this.target = c.target;
			this.cost = c.cost;
			this.energyOnUse = c.cost;
			this.costForTurn = c.costForTurn;
			this.isCostModified = true;
			this.isCostModifiedForTurn = true;
			this.exhaust = c.exhaust;
			this.purgeOnUse = c.purgeOnUse;

			// Update description
			this.rawDescription = EXTENDED_DESCRIPTION[0] + c.name + EXTENDED_DESCRIPTION[1];
			if (this.upgraded) { this.rawDescription = this.rawDescription + UPGRADE_DESCRIPTION; }
			initializeDescription();

			// Match type to card type
			this.type = this.mimic.type;

			// Laod card portrait
			if (this.mimic instanceof CustomCard) {
				this.loadCardImage(((CustomCard)this.mimic).textureImg);
			} else {
				Texture img = null;
				img = (Texture)ReflectionHacks.getPrivate(this.mimic, AbstractCard.class, "portraitImg");

				if (img == null) {
					TextureAtlas.AtlasRegion a = (TextureAtlas.AtlasRegion)ReflectionHacks.getPrivate(this.mimic, AbstractCard.class, "portrait");
					ReflectionHacks.setPrivateInherited(this, CustomCard.class, "portrait", a);
				} else {
					TextureAtlas.AtlasRegion cardImg = new TextureAtlas.AtlasRegion(img, 0, 0, 250, 190);
					ReflectionHacks.setPrivateInherited(this, CustomCard.class, "portrait", cardImg);
				}
			}
		}
		catch (Exception e) {
			this.resetResonance();
		}

	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		if (this.mimic != null) { return this.mimic.canUse(p,m); }
		return false;
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) { 
		super.calculateCardDamage(mo);
		if (this.mimic != null) { this.mimic.calculateCardDamage(mo); }
	}

	@Override
	public void applyPowers() { 
		super.applyPowers();
		if (this.mimic != null) { this.mimic.applyPowers(); }
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.mimic.use(p,m);
		this.superFlash(Color.GOLD.cpy());
		this.loadCardImage("chrono_images/cards/ResonantCall.png");
	}

	@Override
	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.resetResonance();
	}

	@Override
	public void atTurnStart() {
		if (this.mimic != null) { 
		ChronoMod.log("CardID is " + this.mimic.cardID);
			if (this.mimic.cardID == "Parity") {
				this.parityUpdate(); }}
	}

	@Override
	public void update() {
		super.update();
		if (this.mimic != null) { this.mimic.update(); }
	}

	public void parityUpdate() {
		String ATTACKIMG = "chrono_images/cards/Parity.png";
		String SKILLIMG = "chrono_images/cards/ParityS.png";	

		if (AbstractDungeon.actionManager.turn % 2 == 1) {
			this.superFlash(Color.CORAL.cpy());
			this.target = AbstractCard.CardTarget.ENEMY;
			this.type = AbstractCard.CardType.ATTACK;
			this.loadCardImage(ATTACKIMG);
		} else if (AbstractDungeon.actionManager.turn % 2 == 0) {
			this.superFlash(Color.LIME.cpy());
			this.target = AbstractCard.CardTarget.SELF;
			this.type = AbstractCard.CardType.SKILL;
			this.loadCardImage(SKILLIMG);
		}
  		initializeDescription();
	}

	public void resetResonance() {
		this.cost = -1;
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
		this.mimic = null;
		this.rawDescription = DESCRIPTION;
		this.type = AbstractCard.CardType.SKILL;
		this.exhaust = false;
		this.purgeOnUse = false;

		if (this.upgraded) {
			this.rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
		}
		this.loadCardImage("chrono_images/cards/ResonantCall.png");
		this.resetAttributes();
		initializeDescription();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			this.retains = true;
			this.rawDescription = DESCRIPTION + UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}

	@Override
	public void hover() {
		if (this.mimic != null) { this.displayMimic = true; }
		super.hover();
	}

	@Override
	public void unhover() {
		super.unhover();
		if (this.mimic != null) { this.displayMimic = false; }
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);
	    if ((this.mimic != null) && (!Settings.hideCards) && (this.displayMimic))
		{
			float tmpScale = this.drawScale / 1.5F;

		    if ((AbstractDungeon.player != null) && (AbstractDungeon.player.isDraggingCard)) { return; }

	   	    if (this.current_x > Settings.WIDTH * 0.75F) {
		        this.mimic.current_x = this.current_x + (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
		    } else {
		        this.mimic.current_x = this.current_x - (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
		    }

	        this.mimic.current_y = this.current_y + ((AbstractCard.IMG_HEIGHT / 2.0F) - (AbstractCard.IMG_HEIGHT / 2.0F / 1.5F)) * this.drawScale;

	        this.mimic.drawScale = tmpScale;
	        this.mimic.render(sb);
	    }
	}

	@Override
	public void render(SpriteBatch sb, boolean selected) { 
		super.render(sb, selected);
		// this.renderMimicPortrait(sb);
	}

	private void renderMimicPortrait(SpriteBatch sb) {
		if (this.mimic != null)
		{
			sb.setColor(Color.WHITE.cpy());

			if (this.type == AbstractCard.CardType.ATTACK) {
				sb.draw(attackEdge, this.current_x - 125.0F, this.current_y - 95.0F + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250*2, 190*2, false, false); }
			else if (this.type == AbstractCard.CardType.SKILL) {
				sb.draw(skillEdge, this.current_x - 125.0F, this.current_y - 95.0F + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250*2, 190*2, false, false); }
			else if (this.type == AbstractCard.CardType.POWER) {
				sb.draw(powerEdge, this.current_x - 125.0F, this.current_y - 95.0F + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 250*2, 190*2, false, false); }
		}
	}
}