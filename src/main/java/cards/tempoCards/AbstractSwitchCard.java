package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.badlogic.gdx.math.MathUtils;

import com.megacrit.cardcrawl.helpers.Hitbox;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.lang.reflect.*;
import com.badlogic.gdx.Gdx;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.TransformCardPermanently;


public abstract class AbstractSwitchCard extends MetricsCard {
	AbstractCard cardToPreview = null;
	protected static final float CARD_TIP_PAD = 16.0F * Settings.scale;
	public boolean bullshit = false;
	protected Class previewClass;
	public AbstractMonster newTarget = null;
  	public int switchCardUniqueID;

	public AbstractSwitchCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target, Class previewCard) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);

    	this.switchCardUniqueID =  MathUtils.random(0,2147483646);
		this.previewClass = previewCard;
	}

    public void update() {
        super.update();
        this.newTarget = null;
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

	public AbstractCard makeStatEquivalentCopy()
	{
		AbstractSwitchCard card = (AbstractSwitchCard)super.makeStatEquivalentCopy();
		card.switchCardUniqueID = this.switchCardUniqueID;

		return card;
	}

	@Override
	public void renderCardTip(SpriteBatch sb) {
		super.renderCardTip(sb);
	    if ((this.cardToPreview != null) && (!Settings.hideCards) && (this.bullshit))
		{
			float tmpScale = this.drawScale / 1.5F;

			if (newTarget == null) { return; }

        	Hitbox target = newTarget.hb;

			this.cardToPreview.current_x = Gdx.input.getX() + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F);
			this.cardToPreview.current_y = Settings.HEIGHT - Gdx.input.getY() + ((AbstractCard.IMG_HEIGHT / 2.0F) / 1.5F);

	        this.cardToPreview.drawScale = tmpScale;
			if (AbstractDungeon.player.hasRelic("Runic Dome")) {
				this.cardToPreview.setLocked();
			}
	        this.cardToPreview.render(sb);
	    }
	}
}