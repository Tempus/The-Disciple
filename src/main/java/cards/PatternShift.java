package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.combat.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;

import chronomuncher.cards.AbstractSelfRetainingCard;
import chronomuncher.ChronoMod;
import chronomuncher.vfx.PatternShiftPreviewEffect;
import chronomuncher.patches.Enum;
import chronomuncher.actions.PatternShiftAction;
import chronomuncher.vfx.PatternLinesEffect;

import basemod.ReflectionHacks;

import java.lang.reflect.*;


public class PatternShift extends AbstractSelfRetainingCard {
	public static final String ID = "PatternShift";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = 0;
	public EnemyMoveInfo move;
	public EnemyMoveInfo nextMove;
	public AbstractMonster newTarget;
	public boolean intentRevert = false;
	public static long songID = 0;

	public PatternShift() {
		super(ID, NAME, "chrono_images/cards/PatternShift.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.BASIC, AbstractCard.CardTarget.ENEMY);

		this.retains = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToTop(new PatternShiftAction(p, m));
        CardCrawlGame.sound.playA("POWER_TIME_WARP", 0.5F);
		if (m.hasPower("Sleep")) { AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p, "Sleep")); }
		if (m.hasPower("Stun"))  { AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, p, "Stun"));  }
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
		AbstractDungeon.effectsQueue.add(new PatternLinesEffect(m.intentHb.cX, m.intentHb.cY));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
   		   	this.isInnate = true;
		}
	}

	@Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);

        if (this.newTarget == null) {
        	CardCrawlGame.sound.stop("EVENT_SHINING");
   	        this.songID = CardCrawlGame.sound.playA("EVENT_SHINING", -0.5F);

	        this.newTarget = m;

	        this.move = (EnemyMoveInfo)ReflectionHacks.getPrivate(m, AbstractMonster.class, "move");
			
			// Save the random state
			int counter = AbstractDungeon.aiRng.counter;
			long seed0 = (long)ReflectionHacks.getPrivate(AbstractDungeon.aiRng.random, RandomXS128.class, "seed0");
			long seed1 = (long)ReflectionHacks.getPrivate(AbstractDungeon.aiRng.random, RandomXS128.class, "seed1");

			PatternShiftAction.previewNextIntent(this.newTarget);

			// Restore the random state
			AbstractDungeon.aiRng.counter = counter;
			AbstractDungeon.aiRng.random.setState(seed0, seed1);
		}

		this.intentRevert = false;
	}

    public void update() {
        super.update();

        if (this.newTarget != null && !this.intentRevert) {
			AbstractDungeon.effectsQueue.add(new PatternShiftPreviewEffect(this.newTarget.intentHb.cX, this.newTarget.intentHb.cY, 0.75F, 1.75F));
		}

        if (this.newTarget != null && this.intentRevert) {
        	CardCrawlGame.sound.stop("EVENT_SHINING");

			// Remove the move we added, and the one we're about to readd
		    this.newTarget.moveHistory.remove(this.newTarget.moveHistory.size() - 1);
		    if (this.newTarget.moveHistory.size() > 0)
		    this.newTarget.moveHistory.remove(this.newTarget.moveHistory.size() - 1);

		    // Set old move
			PatternShiftAction.restorePreviewedSpecialCases(this.newTarget);

	        this.newTarget.setMove(this.move.nextMove, this.move.intent, this.move.baseDamage, this.move.multiplier, this.move.isMultiDamage);
			this.newTarget.createIntent();

	        this.newTarget = null;
        }

        this.intentRevert = true;
    }	
}