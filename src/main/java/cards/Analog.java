package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import java.util.HashMap;
import java.util.ArrayList;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.RetainOncePower;


public class Analog extends AbstractSelfRetainingCard {
	public static final String ID = "Analog";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;	

	private static final int COST = 1;
	private static final int POWER = 1;

	private static final float PADDING = 12.0F;
	private boolean bullshit = false;

	public class texTip {
		public int count = 0;
		public TextureAtlas.AtlasRegion tex;

		texTip(TextureAtlas.AtlasRegion tex) {
			this.tex = tex;
		}
	}

	public static HashMap<String, texTip> icons = new HashMap();
	private ArrayList<texTip> renderQueue = new ArrayList();

	public Analog() {
		super(ID, NAME, "chrono_images/cards/Analog.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ALL_ENEMY);

		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;

		// this.retains = true;

		if (icons.size() == 0) {
	    	TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("powers/powers.atlas"));
	    	this.icons.put("strength", 	new texTip(atlas.findRegion("128/strength")));
	    	this.icons.put("dexterity", new texTip(atlas.findRegion("128/dexterity")));
	    	this.icons.put("artifact", 	new texTip(atlas.findRegion("128/artifact")));
	    	this.icons.put("retain", 	new texTip(new TextureAtlas.AtlasRegion(ImageMaster.loadImage("chrono_images/powers/RetainOnce.png"), 0, 0, 84, 84)));
	    }
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
	   
	   		if (!mo.isDead && !mo.escaped) {

		   		// Strength
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new StrengthPower(p,this.magicNumber),this.magicNumber,true));
		    	}

		    	// Dexterity
		    	if ((mo.intent == AbstractMonster.Intent.DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new DexterityPower(p,this.magicNumber),this.magicNumber,true));
		    	}
		    			
		    	// Artifact
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.STRONG_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEBUFF)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new ArtifactPower(p,this.magicNumber),this.magicNumber,true));
		    	}

		    	// Retain Card
		    	if ((mo.intent == AbstractMonster.Intent.ESCAPE) ||
		    		(mo.intent == AbstractMonster.Intent.MAGIC) ||
		    		(mo.intent == AbstractMonster.Intent.SLEEP) ||
		    		(mo.intent == AbstractMonster.Intent.STUN) ||
		    		(mo.intent == AbstractMonster.Intent.UNKNOWN)) {
		    		AbstractDungeon.actionManager.addToBottom(
		    			new ApplyPowerAction(p,p,new RetainCardPower(p,this.magicNumber),this.magicNumber,true));
		    	}
	    	}
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			// upgradeMagicNumber(1);
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
  			this.retains = true;
		}
	}

	@Override
	public void hover() {
		super.hover();
		this.bullshit = true;
	}

	@Override
	public void unhover() {
		super.unhover();
		this.bullshit = false;
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);

		if (!CardCrawlGame.isInARun()) { return; }
		if ((Settings.hideCards) || (!this.bullshit)) { return; }
		if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) { return; }

     	float current_y = this.current_y + ((AbstractCard.IMG_HEIGHT / 2.0F) + PADDING * Settings.scale) * this.drawScale;
		float current_x = this.current_x;

	    // Determine the countto add for each buff
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
	   		if (!mo.isDead && !mo.escaped) {
		   		// Strength
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK)) {
		    		this.icons.get("strength").count += this.magicNumber;
		    	}

		    	// Dexterity
		    	if ((mo.intent == AbstractMonster.Intent.DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEFEND) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF)) {
		    		this.icons.get("dexterity").count += this.magicNumber;
		    	}
		    			
		    	// Artifact
		    	if ((mo.intent == AbstractMonster.Intent.ATTACK_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.ATTACK_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.STRONG_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_BUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEFEND_DEBUFF) ||
		    		(mo.intent == AbstractMonster.Intent.DEBUFF)) {
		    		this.icons.get("artifact").count += this.magicNumber;
		    	}

		    	// Retain Card
		    	if ((mo.intent == AbstractMonster.Intent.ESCAPE) ||
		    		(mo.intent == AbstractMonster.Intent.MAGIC) ||
		    		(mo.intent == AbstractMonster.Intent.SLEEP) ||
		    		(mo.intent == AbstractMonster.Intent.STUN) ||
		    		(mo.intent == AbstractMonster.Intent.UNKNOWN)) {
		    		this.icons.get("retain").count += this.magicNumber;
		    	}
	    	}
		}

		// Grab the ones that are in use
		this.renderQueue.clear();
	    for (texTip icon : this.icons.values()) {
	    	if (icon.count > 0) {
	    		this.renderQueue.add(icon);
	    	}
	    }

	    // Render
	    int size = this.renderQueue.size();
	    float offset;
	    float cardOffset = AbstractCard.IMG_WIDTH * this.drawScale / 1.5F;
	    for (int i=size; i > 0; i--) {
	    	// tex, x, y, originx, originy, scalex, scaley, rot
	    	offset = (size-i+2)*42.0F*this.drawScale;

			sb.draw(this.renderQueue.get(i-1).tex, current_x + cardOffset - offset, current_y, 0, 0, 84, 84, 0.5F * this.drawScale, 0.5F * this.drawScale, 0.0F);
			FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.renderQueue.get(i-1).count), current_x + cardOffset - offset + (42.0F*this.drawScale), current_y, 1.0F, new Color(1.0F, 1.0F, 1.0F, 1.0F));

			this.renderQueue.get(i-1).count = 0;
	    }
	}
}