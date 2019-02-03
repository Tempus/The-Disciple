package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import basemod.helpers.TooltipInfo;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.TimeDilatePower;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Pendulum extends MetricsCard {
	public static final String ID = "Pendulum";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	// public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public float duration = 0.0F;
	public float startduration = 0.7F;

	private static final int COST = 1;
	private static final int TURNS_APPLIED = 3;
	private static final int TURNS_APPLIED_UP = 2;
	public ArrayList<TooltipInfo> tips = new ArrayList<TooltipInfo>();

	public Pendulum() {
		super(ID, NAME, "chrono_images/cards/Pendulum.png", COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.ENEMY);

		this.baseMagicNumber = TURNS_APPLIED;
		this.magicNumber = TURNS_APPLIED;
		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new TimeDilatePower(m, this.magicNumber), this.magicNumber, true));
		AbstractDungeon.actionManager.addToBottom(new SFXAction("CHRONO-TICKINGCLEAN"));
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		this.tips.clear();

		if (AbstractDungeon.getCurrMapNode() == null) { return this.tips; }
		if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) { return this.tips; }
		
	    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.isDead && !mo.escaped) {
				if (mo.hasPower("DelayedAttack")) {
					this.tips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0], EXTENDED_DESCRIPTION[2]));
				}
			}
		}

	    return this.tips;
	}

	@Override
    public void calculateCardDamage(AbstractMonster m)
    {
        super.calculateCardDamage(m);

        if (duration > 0) { 
			this.duration -= Gdx.graphics.getDeltaTime();
          	return; 
        }

        duration = startduration;

        for (AbstractPower p : m.powers) {
        	ArrayList<AbstractGameEffect> effect = (ArrayList<AbstractGameEffect>)ReflectionHacks.getPrivate(p, AbstractPower.class, "effect");

	        if (p.type == AbstractPower.PowerType.BUFF) {
	          switch(p.ID) {
	            case "Mode Shift":
	            case "Minion":
	            case "Fading":
	            case "Shifting":
	            case "Unawakened":
	            case "Split":
	            case "Life Link":
	            case "Thievery":
	            case "Surrounded":
	            case "BackAttack":
	              break;
  
	            default:
	              if (p.name == "Delayed Attack" || p.name == "Wake Up Call" || p.name == "The Bomb") { break; }
  
	              effect.add(new SilentGainPowerEffect(p));
	              break;
	          }
	        } else if ((p.ID == "Shackled" && p.amount > 0) || p.ID == "BeatOfDeath") {
		  		effect.add(new SilentGainPowerEffect(p));
	        }
        }
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeMagicNumber(TURNS_APPLIED_UP);
      		// this.rawDescription = UPGRADE_DESCRIPTION;
   		   // 	initializeDescription();
		}
	}
}