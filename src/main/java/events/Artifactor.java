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
import com.megacrit.cardcrawl.relics.AbstractRelic;
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

  public ArrayList<AbstractRelic> relics = new ArrayList();
  
  public Artifactor()
  {
    super(NAME, DIALOG_1, "chrono_images/events/Artifactor.png");

    int tmp = MathUtils.random(2);

    if (tmp == 0) {
      relics.add(new ReplicaWarPaint()); }
    else if (tmp == 1) {
      relics.add(new ReplicaMedicine()); }
    else {
      relics.add(new ReplicaOrichalcum()); }
    
    this.imageEventText.setDialogOption(OPTIONS[0] + relics.get(0).name);

    tmp = MathUtils.random(2);
    if (tmp == 0) {
      relics.add(new ReplicaWhetstone()); }
    else if (tmp == 1) {
      relics.add(new ReplicaScales()); }
    else {
      relics.add(new ReplicaMercury()); }

    this.imageEventText.setDialogOption(OPTIONS[1] + relics.get(1).name);

    tmp = MathUtils.random(2);
    if (tmp == 0) {
      relics.add(new ReplicaIceCream()); }
    else if (tmp == 1) {
      relics.add(new ReplicaHand()); }
    else {
      relics.add(new ReplicaNitrogen()); }

    this.imageEventText.setDialogOption(OPTIONS[2] + relics.get(2).name);

    tmp = MathUtils.random(2);
    if (tmp == 0) {
      relics.add(new ReplicaFlame()); }
    else if (tmp == 1) {
      relics.add(new ReplicaLightning()); }
    else {
      relics.add(new ReplicaTornado()); }

    this.imageEventText.setDialogOption(OPTIONS[3] + relics.get(3).name + OPTIONS[4]);

    // this.imageEventText.setDialogOption(OPTIONS[5]);
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
      case 4: 
        this.screenNum = 2;
        this.imageEventText.updateBodyText(LEAVE_RESULT);
        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
      default: 
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, relics.get(buttonPressed));

        this.screenNum = 2;
        this.imageEventText.updateBodyText(REPLICATE_RESULT);        
        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
        break;
      }
      this.imageEventText.clearRemainingOptions();
      break;
    default: 
      AbstractDungeon.eventList.remove("Artifactor");
      AbstractDungeon.eventList.remove("Artifactor");
      openMap();
    }
  }
}