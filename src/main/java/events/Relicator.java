package chronomuncher.events;

import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.cards.colorless.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.cards.tempCards.*;
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
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import java.util.ArrayList;
import chronomuncher.relics.*;
import chronomuncher.patches.Enum;
import java.lang.Math;
import com.megacrit.cardcrawl.relics.*;
import chronomuncher.ChronoMod;
import chronomuncher.cards.*;

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

  public ArrayList<AbstractCard> cardChoices = new ArrayList();

  public Relicator()
  {
    super(NAME, DIALOG_1, "chrono_images/events/Relicator.png");

    // Find all our replicas
    CardGroup choosers = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
      if (c.hasTag(Enum.REPLICA_CARD)) {
        choosers.addToTop(c);
      }
    }

    // If you have no replica cards, just leave.
    if (choosers.size() <= 0) { 
      this.imageEventText.setDialogOption(OPTIONS[0]);
      return; 
    }

    // Else, make up to three options for exchanging
    int replicas = Math.min(3, choosers.size());
    AbstractCard c;

    for (int i = 0; i<replicas; i++) {
      c = choosers.getRandomCard(true);
      choosers.removeCard(c);
      this.setOption(c);
      cardChoices.add(c);
    }
  }
  
  public void onEnterRoom()
  {
    if (Settings.AMBIANCE_ON) {
      CardCrawlGame.sound.play("EVENT_FORGE");
    }
  }

  public void setOption(AbstractCard c) {
    switch (c.cardID) {
      case "LockedAnchor":
        this.imageEventText.setDialogOption(OPTIONS[1]);
        break;
      case "LockedAstrolabe":
        this.imageEventText.setDialogOption(OPTIONS[2]);
        break;
      case "LockedBell":
        this.imageEventText.setDialogOption(OPTIONS[3]);
        break;
      case "LockedFlame":
        this.imageEventText.setDialogOption(OPTIONS[4], CardLibrary.getCopy("Shiv"));
        break;
      case "LockedLightning":
        this.imageEventText.setDialogOption(OPTIONS[5], CardLibrary.getCopy("Ward"));
        break;
      case "LockedTornado":
        this.imageEventText.setDialogOption(OPTIONS[6], new Dizzy());
        break;
      case "LockedBlood":
        this.imageEventText.setDialogOption(OPTIONS[7]);
        break;
      case "LockedCalendar":
        this.imageEventText.setDialogOption(OPTIONS[8]);
        break;
      case "LockedMawBank":
        this.imageEventText.setDialogOption(OPTIONS[9]);
        break;
      case "LockedMedicine":
        if (!c.upgraded) {
          this.imageEventText.setDialogOption(OPTIONS[10], CardLibrary.getCopy("Dazed")); }
        else {
          this.imageEventText.setDialogOption(OPTIONS[11], CardLibrary.getCopy("Clumsy")); }
        break;
      case "LockedMercury":
        this.imageEventText.setDialogOption(OPTIONS[12]);
        break;
      case "LockedOrichalcum":
        this.imageEventText.setDialogOption(OPTIONS[13]);
        break;
      case "LockedThread":
        this.imageEventText.setDialogOption(OPTIONS[14]);
        break;
      case "LockedPlans":
        this.imageEventText.setDialogOption(OPTIONS[15], CardLibrary.getCopy("Writhe"));
        break;
      case "LockedUrn":
        this.imageEventText.setDialogOption(OPTIONS[16]);
        break;
      case "LockedWarPaint":
        this.imageEventText.setDialogOption(OPTIONS[17]);
        break;
      case "LockedWhetstone":
        this.imageEventText.setDialogOption(OPTIONS[18]);
        break;
      case "LockedTwirtle":
        this.imageEventText.setDialogOption(OPTIONS[19]);
        break;
      case "LockedScales":
        this.imageEventText.setDialogOption(OPTIONS[20]);
        break;
      default:
        ChronoMod.log("This should never happen.");
    }  
  }

  public CardGroup getUpgradedCards(CardGroup group)
  {
    CardGroup retVal = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    for (AbstractCard c : group.group) {
      if (c.upgraded) {
        retVal.group.add(c);
      }
    }
    return retVal;
  }


  public void update()
  {
    super.update();
    if ((!AbstractDungeon.isScreenUp) && (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()))
    {
      AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
      AbstractDungeon.player.masterDeck.removeCard(c);
      AbstractDungeon.transformCard(c);
      AbstractCard transCard = AbstractDungeon.getTransformedCard();
      AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(transCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
      
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
    }
  }
  
  public void processResult(AbstractCard c) {
    CardCrawlGame.sound.play("CARD_EXHAUST");
    CardGroup g;
    AbstractCard d;

    switch (c.cardID) {
      case "LockedAnchor":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Anchor());
        AbstractDungeon.player.decreaseMaxHealth(5);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, true);
        break;
      case "LockedAstrolabe":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Astrolabe());

        g = this.getUpgradedCards(AbstractDungeon.player.masterDeck);
        if (g.size() > 0) {
          d = g.getRandomCard(true);
          AbstractDungeon.player.masterDeck.removeCard(d);
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(d.makeCopy(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
          CardCrawlGame.sound.play("INTIMIDATE");
        }
        
        g = this.getUpgradedCards(AbstractDungeon.player.masterDeck);
        if (g.size() > 0) {
          d = g.getRandomCard(true);
          AbstractDungeon.player.masterDeck.removeCard(d);
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(d.makeCopy(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
          CardCrawlGame.sound.play("INTIMIDATE");
        }
        break;
      case "LockedBell":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new CallingBell());
        break;
      case "LockedFlame":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledFlame());
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Shiv(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
        break;
      case "LockedLightning":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledLightning());
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Ward(), Settings.WIDTH / 2 - 150.0F, Settings.HEIGHT / 2));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Ward(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
        break;
      case "LockedTornado":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BottledTornado());
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Dizzy(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
        break;
      case "LockedBlood":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BloodVial());
        AbstractDungeon.player.damage(new DamageInfo(null, 10));
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
        break;
      case "LockedCalendar":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new StoneCalendar());
        AbstractDungeon.player.damage(new DamageInfo(null, 7));
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
        break;
      case "LockedMawBank":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MawBank());
        AbstractDungeon.player.loseGold(AbstractDungeon.player.gold);
        break;
      case "LockedMedicine":
        if (!c.upgraded) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MedicalKit()); 
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Dazed(), Settings.WIDTH / 2, Settings.HEIGHT / 2)); }
        else {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MedicalKit());
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BlueCandle()); 
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Clumsy(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
        }
        break;
      case "LockedMercury":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new MercuryHourglass());
        AbstractDungeon.player.damage(new DamageInfo(null, 15));
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
        break;
      case "LockedOrichalcum":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Orichalcum());
        AbstractDungeon.player.decreaseMaxHealth(3);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, true);
        break;
      case "LockedThread":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new ThreadAndNeedle());
        AbstractDungeon.player.damage(new DamageInfo(null, 30));
        CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
        break;
      case "LockedPlans":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Metronome()); 
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH / 2 - 150.0F, Settings.HEIGHT / 2));
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
        break;
      case "LockedUrn":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BirdFacedUrn());
        AbstractDungeon.player.decreaseMaxHealth(14);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, true);
        break;
      case "LockedWarPaint":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new WarPaint());
        g = this.getUpgradedCards(AbstractDungeon.player.masterDeck.getAttacks());
        if (g.size() > 0) {
          d = g.getRandomCard(true);
          AbstractDungeon.player.masterDeck.removeCard(d);
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(d.makeCopy(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
          CardCrawlGame.sound.play("INTIMIDATE");
        }
        break;
      case "LockedWhetstone":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new Whetstone());
        g = this.getUpgradedCards(AbstractDungeon.player.masterDeck.getSkills());
        if (g.size() > 0) {
          d = g.getRandomCard(true);
          AbstractDungeon.player.masterDeck.removeCard(d);
          AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(d.makeCopy(), Settings.WIDTH / 2 + 150.0F, Settings.HEIGHT / 2));
          CardCrawlGame.sound.play("INTIMIDATE");
        }
        break;
      case "LockedTwirtle":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new PaperTurtyl());
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new GremlinMask());
        break;
      case "LockedScales":
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new BronzeScales()); 
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Pain(), Settings.WIDTH / 2 - 150.0F, Settings.HEIGHT / 2));
        break;
      default:
        ChronoMod.log("This should never happen.");
    }

    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2, Settings.HEIGHT / 2));
    AbstractDungeon.player.masterDeck.removeCard(c);

    this.imageEventText.updateBodyText(REPLICATE_RESULT);
    this.screenNum = 2;         
    this.imageEventText.updateDialogOption(0, OPTIONS[0]);
  }

  protected void buttonEffect(int buttonPressed)
  {
    switch (this.screenNum)
    {
    case 0:
      if (cardChoices.size() == 0) {
        this.screenNum = 2;
        this.imageEventText.updateBodyText(LEAVE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[0]);    
      }
      else
      {
        this.processResult(cardChoices.get(buttonPressed));
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
