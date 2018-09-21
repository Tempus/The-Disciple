package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import java.lang.reflect.*;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.actions.TransformCardPermanently;


public abstract class AbstractSelfSwitchCard extends MetricsCard {

	public class switchCard {
		public CardType type;
		public CardTarget target;
		public int cost;

		public boolean isMultiDamage = false;
		public boolean isInnate = false;
		public boolean exhaust = false;
		public boolean isEthereal = false;

		public int damage = -1;
		public int block = -1;
		public int magicNumber = -1;

		public int damageUp = -1;
		public int blockUp = -1;
		public int magicNumberUp = -1;

		public String portraitImg;

		public String originalName;
		public String name;
		public String description;
		public String upgradeDescription;
		public String cardID;
		public String switchID;

		public switchCard(String CardID, String switchID, Integer cost, Integer damage, Integer damageUp, 
						  Integer block, Integer blockUp, Integer magicNum, Integer magicNumUp, 
						  CardType type, CardTarget target, boolean isMultiDamage, boolean isInnate, boolean exhaust, boolean isEthereal)
		{
			this.type = type;
			this.target = target;
			this.cost = cost;

			this.isMultiDamage = isMultiDamage;
			this.isInnate = isInnate;
			this.exhaust = exhaust;
			this.isEthereal = isEthereal;

			this.damage = damage;
			this.block = block;
			this.magicNumber = magicNum;

			this.damageUp = damageUp;
			this.blockUp = blockUp;
			this.magicNumberUp = magicNumUp;

    		this.portraitImg = "images/cards/" + CardID + ".png";

			CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CardID);			
			this.originalName = cardStrings.NAME;
			this.name = cardStrings.NAME;
			this.description = cardStrings.DESCRIPTION;
			this.upgradeDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.cardID = CardID;
			this.switchID = switchID;
		}
	}

	public List<switchCard> switchList;

	AbstractSelfSwitchCard cardToPreview = null;
	protected static final float CARD_TIP_PAD = 16.0F;
	public boolean bullshit = false;
	protected Class switchClass;

	public int damageUp = 0;
	public int blockUp = 0;
	public int magicNumberUp = 0;
	public String upgradeDescription = "";
	public String switchID;
	public String currentID;

	public AbstractSelfSwitchCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, Class previewCard) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);

		this.switchClass = previewCard;
	}

	@Override
	public void renderInLibrary(SpriteBatch sb)
	{
	  if (!(this.current_y >= -200.0F * Settings.scale) && (this.current_y <= Settings.HEIGHT + 200.0F * Settings.scale)) {
	    return;
	  }
	  if ((SingleCardViewPopup.isViewingUpgrade) && (this.isSeen) && (!this.isLocked))
	  {
	    AbstractCard copy = makeStatEquivalentCopy();
	    copy.current_x = this.current_x;
	    copy.current_y = this.current_y;
	    copy.drawScale = this.drawScale;
	    copy.upgrade();
	    copy.displayUpgrades();
	    copy.render(sb);
	  }
	  else
	  {
		super.renderInLibrary(sb);
	  }
	}

	@Override
	public AbstractCard makeCopy() {
		AbstractCard c = null;
		try {
			if (this.switchClass != null) {
				c = (AbstractSelfSwitchCard)this.switchClass.newInstance(); 
			}
		} catch (Throwable e) {
			ChronoMod.log(e.toString());
		}
		return c;
	}

	public AbstractCard makeStatEquivalentCopy()
	{
		AbstractSelfSwitchCard card = (AbstractSelfSwitchCard)makeCopy();
		card.switchTo(this.currentID);

		for (int i = 0; i < this.timesUpgraded; i++) {
		  card.upgrade();
		}
		card.name = this.name;
		card.target = this.target;
		card.upgraded = this.upgraded;
		card.timesUpgraded = this.timesUpgraded;
		card.baseDamage = this.baseDamage;
		card.baseBlock = this.baseBlock;
		card.baseMagicNumber = this.baseMagicNumber;
		card.cost = this.cost;
		card.costForTurn = this.costForTurn;
		card.isCostModified = this.isCostModified;
		card.isCostModifiedForTurn = this.isCostModifiedForTurn;
		card.inBottleLightning = this.inBottleLightning;
		card.inBottleFlame = this.inBottleFlame;
		card.inBottleTornado = this.inBottleTornado;
		card.isSeen = this.isSeen;
		card.isLocked = this.isLocked;
		card.misc = this.misc;
		return card;
	}

	public switchCard findSwitch(String id) {
		for (switchCard card : switchList) {
			if (card.cardID == id) {
				return card;
			}
		}

		ChronoMod.log(this.cardID + " could not find the switch card " + id);
		return null;
	}

	public void switchTo(String id) {
		switchCard card = this.findSwitch(id);

		if (card != null) {
			this.type = card.type;
			this.cost = card.cost;
			this.target = card.target;

			this.isMultiDamage = card.isMultiDamage;
			this.isInnate = card.isInnate;
			this.exhaust = card.exhaust;
			this.isEthereal = card.isEthereal;

			this.baseDamage = card.damage;
			this.baseBlock = card.block;
			this.baseMagicNumber = card.magicNumber;

			this.damageUp = card.damageUp;
			this.blockUp = card.blockUp;
			this.magicNumberUp = card.magicNumberUp;

			this.loadCardImage(card.portraitImg);
			this.textureImg = card.portraitImg;

			this.originalName = card.originalName;
			this.name = card.name;
			this.rawDescription = card.description;
			this.upgradeDescription = card.upgradeDescription;
			this.currentID = card.cardID;
			this.switchID = card.switchID;

			resetAttributes();

			if (this.upgraded) {
				upgradeName();
				upgradeDamage(damageUp);
				upgradeBlock(blockUp);
				upgradeMagicNumber(magicNumberUp);
	      		this.rawDescription = card.upgradeDescription;
			}

    		initializeTitle();
        	initializeDescription();
    		resetAttributes();

    		if (AbstractDungeon.player != null) {
	    		if (AbstractDungeon.player.hasRelic("Bottled Flame")) { 
	    			((BottledFlame)AbstractDungeon.player.getRelic("Bottled Flame")).setDescriptionAfterLoading(); }
	    		if (AbstractDungeon.player.hasRelic("Bottled Lightning")) { 
	    			((BottledLightning)AbstractDungeon.player.getRelic("Bottled Lightning")).setDescriptionAfterLoading(); }
	    		if (AbstractDungeon.player.hasRelic("Bottled Tornado")) { 
	    			((BottledTornado)AbstractDungeon.player.getRelic("Bottled Tornado")).setDescriptionAfterLoading(); }
	    	}
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(damageUp);
			upgradeBlock(blockUp);
			upgradeMagicNumber(magicNumberUp);
      		this.rawDescription = this.upgradeDescription;
   		   	initializeDescription();
		}
	}

	// Card Preview
	@Override
	public void hover() {
		try {
			if (this.switchClass != null && this.cardToPreview == null) {
				this.cardToPreview = (AbstractSelfSwitchCard)this.switchClass.newInstance(); 
				this.cardToPreview.switchTo(this.switchID);
				if (this.upgraded) { this.cardToPreview.upgrade(); }
			}
		} catch (Throwable e) {
			ChronoMod.log(e.toString());
		}

		super.hover();
		this.bullshit = true;
	}

	@Override
	public void unhover() {
		super.unhover();
		this.bullshit = false;
		this.cardToPreview = null;
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
	    if ((!Settings.hideCards) && (this.bullshit))
	    {
	      if ((SingleCardViewPopup.isViewingUpgrade) && (this.isSeen) && (!this.isLocked))
	      {
	        AbstractCard copy = makeStatEquivalentCopy();
	        copy.current_x = this.current_x;
	        copy.current_y = this.current_y;
	        copy.drawScale = this.drawScale;
	        copy.upgrade();
	        
	        TipHelper.renderTipForCard(copy, sb, copy.keywords);
	      } else {
		    super.renderCardTip(sb);
		  }
	    }

	    if ((this.cardToPreview != null) && (!Settings.hideCards) && (this.bullshit))
		{
			float tmpScale = this.drawScale / 1.5F;

		    if ((AbstractDungeon.player != null) && (AbstractDungeon.player.isDraggingCard)) {
		        return;
		    }

		    	//						x    = card center	  + half the card width 			 + half the preview width 					  + Padding			* Viewport scale * drawscale
	   	    if (this.current_x > Settings.WIDTH * 0.75F) {
		        this.cardToPreview.current_x = this.current_x + (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
		    } else {
		        this.cardToPreview.current_x = this.current_x - (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
		    }

	        this.cardToPreview.current_y = this.current_y + ((AbstractCard.IMG_HEIGHT / 2.0F) - (AbstractCard.IMG_HEIGHT / 2.0F / 1.5F)) * this.drawScale;

	        this.cardToPreview.drawScale = tmpScale;
	        this.cardToPreview.render(sb);
	    }
	}

	public boolean switchCardInDeck() {
		if (AbstractDungeon.currMapNode == null) { return false; }
		if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
		    for (Iterator<AbstractCard> i = AbstractDungeon.player.masterDeck.group.iterator(); i.hasNext();)
		    {
		        AbstractCard e = (AbstractCard)i.next();
	            if (e instanceof AbstractSelfSwitchCard) {
	                AbstractSelfSwitchCard sCard = (AbstractSelfSwitchCard)e;
			        if (sCard.cardID.equals(this.cardID) && sCard.upgraded == this.upgraded && sCard.currentID == this.currentID)
			        {
			          sCard.switchTo(sCard.switchID);
			          return true;
			        }
			    }
		    }
		}
	    return false;
   	}

}