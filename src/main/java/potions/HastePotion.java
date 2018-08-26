package chronomuncher.potions;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import java.util.ArrayList;
import java.util.TreeMap;

import chronomuncher.ChronoMod;
import chronomuncher.powers.HastePower;

public class HastePotion
  extends AbstractPotion
{
  public static final String POTION_ID = "HastePotion";
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString("HastePotion");
  public static final String NAME = potionStrings.NAME;
  public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
  
  public HastePotion()
  {
    super(NAME, "HastePotion", AbstractPotion.PotionRarity.UNCOMMON, AbstractPotion.PotionSize.T, AbstractPotion.PotionColor.ELIXIR);
    this.potency = getPotency();
    this.description = (DESCRIPTIONS[0]);
    this.isThrown = false;
    this.tips.add(new PowerTip(this.name, this.description));
    // this.tips.add(new PowerTip(
    
    //   TipHelper.capitalize(GameDictionary.HASTE.NAMES[0]), 
    //   (String)GameDictionary.keywords.get(GameDictionary.HASTE.NAMES[0])));
  }
  
  public void use(AbstractCreature target)
  {
    target = AbstractDungeon.player;
    if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new HastePower(target, this.potency), this.potency));
    }
  }
  
  public AbstractPotion makeCopy()
  {
    return new HastePotion();
  }
  
  public int getPotency(int ascensionLevel)
  {
    return 0;
  }
}
