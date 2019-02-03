package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.actions.IntentTransformAction;
import chronomuncher.cards.*;
import chronomuncher.cards.AbstractSwitchCard;
import basemod.helpers.TooltipInfo;
import basemod.ReflectionHacks;
import java.util.List;
import java.util.ArrayList;


public class BeatsPerMinute extends AbstractSwitchCard {
	public static final String ID = "BeatsPerMinute";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;   
    public ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

	private static final int COST = 1;
	private static final int ATTACK_DMG = 11;
	private static final int UPGRADE_PLUS_DMG = 4;

	public BeatsPerMinute() {
		super(ID, NAME, "chrono_images/cards/BeatsPerMinute.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.UNCOMMON,
				AbstractCard.CardTarget.ENEMY, null);

		this.baseDamage = ATTACK_DMG;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
	    if (m.isDying || (m.currentHealth + m.currentBlock <= this.damage)) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-SHARP2"));
			AbstractDungeon.actionManager.addToBottom(new IntentTransformAction(p, m, this.upgraded));
		}

		AbstractDungeon.actionManager.addToBottom(
			new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
	}

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        this.tips.clear();
        
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[1]));
        this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[2], EXTENDED_DESCRIPTION[3]));

        return this.tips;
    }

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_PLUS_DMG);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
		}
	}

	@Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);

        this.newTarget = m;
        this.bullshit = true;

        if (m.intent == AbstractMonster.Intent.ATTACK) {
            boolean isMultiDmg = (boolean)ReflectionHacks.getPrivate(this.newTarget, AbstractMonster.class, "isMultiDmg");
    
            if (!isMultiDmg) { this.cardToPreview = new Allegro(); } 
            else             { this.cardToPreview = new Allargando(); } }
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
            this.newTarget = null;
            this.bullshit = false;
        }

        if (this.cardToPreview != null) {
            if (this.upgraded) { this.cardToPreview.upgrade(); }
            this.cardToPreview.superFlash();
        }
    }	
}