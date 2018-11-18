package chronomuncher.events;

import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.WarpedTongs;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import java.util.ArrayList;
import chronomuncher.relics.*;
import chronomuncher.patches.Enum;

public class Replicator
  extends AbstractImageEvent
{
  public static final String ID = "Replicator";
  private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Replicator");
  public static final String NAME = eventStrings.NAME;
  public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
  public static final String[] OPTIONS = eventStrings.OPTIONS;
  private static final String DIALOG_1 = DESCRIPTIONS[0];
  private static final String REPLICATE_RESULT = DESCRIPTIONS[1];
  private static final String LEAVE_RESULT = DESCRIPTIONS[2];
  private int screenNum = 0;
  private boolean pickCard = false;
  
  public Replicator()
  {
    super(NAME, DIALOG_1, "chrono_images/events/Replicator.png");
    this.imageEventText.setDialogOption(OPTIONS[0]);
    this.imageEventText.setDialogOption(OPTIONS[1]);
  }
  
  public void onEnterRoom()
  {
    if (Settings.AMBIANCE_ON) {
      CardCrawlGame.sound.play("EVENT_FORGE");
    }
  }
  
  public void update() {
    super.update();
    if ((this.pickCard) && 
      (!AbstractDungeon.isScreenUp) && (AbstractDungeon.gridSelectScreen.selectedCards.size() == 2))
    {
      AbstractCard newCard;
      for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
        CardCrawlGame.sound.play("CARD_EXHAUST");
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2, Settings.HEIGHT / 2));
        AbstractDungeon.player.masterDeck.removeCard(c);

        AbstractCard.CardRarity rarity = c.rarity;
        if (rarity == AbstractCard.CardRarity.BASIC) { rarity = AbstractCard.CardRarity.COMMON; }
        if (rarity == AbstractCard.CardRarity.SPECIAL){ rarity = AbstractCard.CardRarity.UNCOMMON; }

        newCard = AbstractDungeon.getCardFromPool(rarity, AbstractCard.CardType.POWER, true);
        while (!newCard.hasTag(Enum.REPLICA_CARD)) {
          newCard = AbstractDungeon.getCardFromPool(rarity, AbstractCard.CardType.POWER, true);
        }

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(newCard.makeCopy(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      }
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      this.pickCard = false;
    }
  }

  protected void buttonEffect(int buttonPressed)
  {
    switch (this.screenNum)
    {
    case 0: 
      switch (buttonPressed)
      {
      case 0: 
        this.imageEventText.updateBodyText(REPLICATE_RESULT);
        this.screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[1]);

        CardGroup choosers = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        choosers.group.addAll(AbstractDungeon.player.masterDeck.getSkills().group);
        choosers.group.addAll(AbstractDungeon.player.masterDeck.getAttacks().group);
        choosers.group.addAll(AbstractDungeon.player.masterDeck.getPowers().group);

        int picks = 2;
        // There's no cards to pick
        if (choosers.size() <= 0) { return; } 
        else if (choosers.size() == 1) { picks = 1; }

        this.pickCard = true;
        AbstractDungeon.gridSelectScreen.open(choosers, picks, "Choose 2 cards to transform into Locked Replicas", false, false, false, true);
        break;
      case 1: 
        this.screenNum = 2;
        this.imageEventText.updateBodyText(LEAVE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
      }
      this.imageEventText.clearRemainingOptions();
      break;
    default: 
      openMap();
    }
  }
}

        // AbstractCard curse = new Pain();
        // AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, Settings.WIDTH / 2, Settings.HEIGHT / 2));
