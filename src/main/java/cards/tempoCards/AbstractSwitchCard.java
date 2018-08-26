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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.lang.reflect.*;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.TransformCardPermanently;


public abstract class AbstractSwitchCard extends MetricsCard {
	AbstractCard cardToPreview = null;
	protected static final float CARD_TIP_PAD = 16.0F * Settings.scale;
	public boolean bullshit = false;
	protected Class previewClass;

	public AbstractSwitchCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, Class previewCard) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);

		this.previewClass = previewCard;
	}

	@Override
	public void hover() {
		try {
			if (this.previewClass != null) {
				this.cardToPreview = (MetricsCard)this.previewClass.newInstance(); 
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
		super.renderCardTip(sb);
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
}