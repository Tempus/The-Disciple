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
import javafx.util.Pair;
import java.util.ArrayList;
import com.megacrit.cardcrawl.relics.*;
import chronomuncher.ChronoMod;

public class Relicator
  extends AbstractImageEvent
{
  public static final String ID = "Relicator";
  private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Relicator");
  public static final String NAME = eventStrings.NAME;
  public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
  public static final String[] OPTIONS = eventStrings.OPTIONS;
  private static final String DIALOG_1 = DESCRIPTIONS[0];
  private static final String REPLICATE_RESULT = DESCRIPTIONS[1];
  private static final String LEAVE_RESULT = DESCRIPTIONS[2];
  private int screenNum = 0;
  private boolean pickCard = false;

  private final ArrayList<Pair<String, Class>> relicList = new ArrayList<Pair<String, Class>>();

  public Relicator()
  {
    super(NAME, DIALOG_1, "chrono_images/events/Relicator.png");
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
      (!AbstractDungeon.isScreenUp) && (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0))
    {
      AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
      CardCrawlGame.sound.play("CARD_EXHAUST");

      switch (c.cardID) {
        case "LockedAnchor":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Anchor());
          break;
        case "LockedAstrolabe":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Astrolabe());
          break;
        case "LockedBell":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new CallingBell());
          break;
        case "LockedFlame":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledFlame());
          break;
        case "LockedLightning":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledLightning());
          break;
        case "LockedTornado":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledTornado());
          break;
        case "LockedBlood":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BloodVial());
          break;
        case "LockedCalendar":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new StoneCalendar());
          break;
        case "LockedHand":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MummifiedHand());
          break;
        case "LockedIceCream":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new IceCream());
          break;
        case "LockedMawBank":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MawBank());
          break;
        case "LockedMedicine":
          if (!c.upgraded) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MedicalKit()); }
          else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MedicalKit());
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BlueCandle()); }
          break;
        case "LockedMercury":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MercuryHourglass());
          break;
        case "LockedNitrogen":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Cryopreserver());
          break;
        case "LockedOrichalcum":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Orichalcum());
          break;
        case "LockedThread":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new ThreadAndNeedle());
          break;
        case "LockedPlans":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Metronome()); 
          break;
        case "LockedUrn":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BirdFacedUrn());
          break;
        case "LockedWarPaint":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new WarPaint());
          break;
        case "LockedWhetstone":
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Whetstone());
          break;
        default:
          ChronoMod.log("This should never happen.");
      }

      AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2, Settings.HEIGHT / 2));
      AbstractDungeon.player.masterDeck.removeCard(c);

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
        this.pickCard = true;
          ChronoMod.log("pls plsplsplplpslsplsplspslpslsplspslpslsplspslpslspslpslspslpslspslpslspslpslspslpslspslpslspslps.");

        CardGroup choosers = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
          if (c.hasTag(Enum.REPLICA_CARD)) {
            choosers.addToTop(c);
            ChronoMod.log(c.cardID);
          }
        }

        AbstractDungeon.gridSelectScreen.open(choosers, 1, "Choose a card to turn into a real boy", false, false, false, true);

        AbstractCard curse = new Pain();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(curse, Settings.WIDTH / 2, Settings.HEIGHT / 2));

        this.imageEventText.updateBodyText(REPLICATE_RESULT);
        this.screenNum = 2;         
        this.imageEventText.updateDialogOption(0, OPTIONS[1]);
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
