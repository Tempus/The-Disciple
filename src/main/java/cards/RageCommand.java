package chronomuncher.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import chronomuncher.cards.MetricsCard;
import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;
import chronomuncher.powers.DelayedAttackPower;

public class RageCommand extends MetricsCard {
  public static final String ID = "RageCommand";
  private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
  public static final String NAME = cardStrings.NAME;
  public static final String DESCRIPTION = cardStrings.DESCRIPTION;

  private static final int COST = 3;
  private static final int ATTACK_DMG = 48;
  private static final int UPGRADE_PLUS_DMG = 12;

  private static final int TURNS = 3;
  
  public RageCommand() {
    super(ID, NAME, "chrono_images/cards/RageCommand.png", COST, DESCRIPTION, AbstractCard.CardType.ATTACK,
        Enum.CHRONO_GOLD, AbstractCard.CardRarity.RARE,
        AbstractCard.CardTarget.ALL_ENEMY);
    
    this.baseDamage = ATTACK_DMG;
    // this.isMultiDamage = true;
  }
  
  @Override
  public void use(AbstractPlayer p, AbstractMonster m)
  {
    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
      if (!mo.isDeadOrEscaped()) {
        this.calculateCardDamage(mo);
        AbstractDungeon.actionManager.addToBottom(
         new ApplyPowerAction(mo, p, new DelayedAttackPower(mo, TURNS, this.damage), TURNS));
      }
    }
  }
  
  @Override
  public void upgrade()
  {
    if (!this.upgraded)
    {
      upgradeName();
      upgradeDamage(UPGRADE_PLUS_DMG);
    }
  }
}
