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
import java.util.ArrayList;
import chronomuncher.relics.*;
import com.badlogic.gdx.math.MathUtils;

public class Artifactor
  extends AbstractImageEvent
{
  public static final String ID = "Artifactor";
  private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Artifactor");
  public static final String NAME = eventStrings.NAME;
  public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
  public static final String[] OPTIONS = eventStrings.OPTIONS;
  private static final String DIALOG_1 = DESCRIPTIONS[0];
  private static final String REPLICATE_RESULT = DESCRIPTIONS[1];
  private static final String LEAVE_RESULT = DESCRIPTIONS[2];
  private int screenNum = 0;
  private boolean pickCard = false;
  
  public Artifactor()
  {
    super(NAME, DIALOG_1, "chrono_images/events/Artifactor.png");
    this.imageEventText.setDialogOption(OPTIONS[0]);
    this.imageEventText.setDialogOption(OPTIONS[1]);
    this.imageEventText.setDialogOption(OPTIONS[2]);
    this.imageEventText.setDialogOption(OPTIONS[3]);
    this.imageEventText.setDialogOption(OPTIONS[4]);
  }
  
  public void onEnterRoom()
  {
    if (Settings.AMBIANCE_ON) {
      CardCrawlGame.sound.play("EVENT_FORGE");
    }
  }
  

  // Armor Relics:
  //
  //  Orichalcum
  //  War Paint
  //  Medicine
  //
  // Weapon Relics:
  //
  //  Mercury
  //  Scales
  //  Whetstone
  //
  // Unusued/Unusual Replicas:
  //
  //  Maw Bank
  //  Ice Cream
  //  Mummified Hand
  //  Cryopresever
  //

  protected void buttonEffect(int buttonPressed)
  {
    int tmp = MathUtils.random(2);
    switch (this.screenNum)
    {
    case 0: 
      switch (buttonPressed)
      {
      case 0: 
        if (tmp == 0) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaWarPaint()); }
        else if (tmp == 1) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaMedicine()); }
        else {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaOrichalcum()); }
        
        this.imageEventText.updateBodyText(REPLICATE_RESULT);
        this.screenNum = 2;
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        break;
      case 1: 
        if (tmp == 0) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaWhetstone()); }
        else if (tmp == 1) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaScales()); }
        else {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaMercury()); }

        this.screenNum = 2;
        this.imageEventText.updateBodyText(REPLICATE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        break;
      case 2: 
        if (tmp == 0) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaIceCream()); }
        else if (tmp == 1) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaHand()); }
        else {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaNitrogen()); }

        this.screenNum = 2;
        this.imageEventText.updateBodyText(REPLICATE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        break;
      case 3: 
        if (tmp == 0) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaFlame()); }
        else if (tmp == 1) {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaLightning()); }
        else {
          AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new ReplicaTornado()); }

        this.screenNum = 2;
        this.imageEventText.updateBodyText(REPLICATE_RESULT);        
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
        break;
      case 4: 
        this.screenNum = 2;
        this.imageEventText.updateBodyText(LEAVE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
      }
      this.imageEventText.clearRemainingOptions();
      break;
    default: 
      openMap();
    }
  }
}