package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.IntentTransformAction;
import chronomuncher.cards.*;
import chronomuncher.cards.AbstractSwitchCard;


public class Tempo extends AbstractSwitchCard {
	public static final String ID = "Tempo";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

	private static final int COST = 0;

	public Tempo() {
		super(ID, NAME, "images/cards/Tempo.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.BRONZE, AbstractCard.CardRarity.COMMON, AbstractCard.CardTarget.ENEMY, null);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToTop(new IntentTransformAction(p, m, this));
	}

	@Override
	public void atTurnStart() {
		if (this.upgraded) {
			this.retain = true;
		}
	}

	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		this.costForTurn = this.cost;
		this.isCostModifiedForTurn = false;
	}

	@Override
	public AbstractCard makeCopy() {
		return new Tempo();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
      		this.rawDescription = UPGRADE_DESCRIPTION;
   		   	initializeDescription();
		}
	}

	@Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);

        this.bullshit = true;

        if (m.intent == AbstractMonster.Intent.ATTACK) {
        	this.cardToPreview = new Allegro(); }
        else if (m.intent == AbstractMonster.Intent.ATTACK_BUFF) {
        	this.cardToPreview = new Vivace(); }
        else if (m.intent == AbstractMonster.Intent.ATTACK_DEBUFF) {
        	this.cardToPreview = new Moderato(); }
        else if (m.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
        	this.cardToPreview = new Allegretto(); }
        else if (m.intent == AbstractMonster.Intent.BUFF) {
        	this.cardToPreview = new Accelerando(); }
        else if (m.intent == AbstractMonster.Intent.DEBUFF) {
        	this.cardToPreview = new Rallentando(); }
        else if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
        	this.cardToPreview = new Ritenuto(); }
        else if (m.intent == AbstractMonster.Intent.DEFEND) {
        	this.cardToPreview = new Lento(); }
        else if (m.intent == AbstractMonster.Intent.DEFEND_BUFF) {
        	this.cardToPreview = new Adagio(); }
        else if (m.intent == AbstractMonster.Intent.DEFEND_DEBUFF) {
        	this.cardToPreview = new Largo(); }
        else if (m.intent == AbstractMonster.Intent.ESCAPE) {
        	this.cardToPreview = new Presto(); }
        else if (m.intent == AbstractMonster.Intent.MAGIC) {
        	this.cardToPreview = new Maestoso(); }
        else if (m.intent == AbstractMonster.Intent.SLEEP) {
        	this.cardToPreview = new Grave(); }
        else if (m.intent == AbstractMonster.Intent.STUN) {
        	this.cardToPreview = new Sospirando(); }
        else if (m.intent == AbstractMonster.Intent.UNKNOWN) {
        	this.cardToPreview = new Misterioso(); }
        else {
        	this.cardToPreview = null;
        	this.bullshit = false;
        }

        if (this.upgraded) { this.cardToPreview.upgrade(); }
    }	
}