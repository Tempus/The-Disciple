package chronomuncher;

import java.nio.charset.StandardCharsets;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.SetUnlocksSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostDeathSubscriber;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.PreStartGameSubscriber;
import basemod.interfaces.PostDrawSubscriber;
import basemod.interfaces.OnPowersModifiedSubscriber;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import chronomuncher.cards.*;
import chronomuncher.relics.*;
import chronomuncher.potions.*;
import chronomuncher.patches.Enum;
import chronomuncher.patches.EnumLib;
import chronomuncher.patches.customMetrics;
import chronomuncher.character.Chronomuncher;
import chronomuncher.orbs.ReplicaOrb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class ChronoMod implements 
        PostInitializeSubscriber, EditCardsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, PreStartGameSubscriber,
        EditStringsSubscriber, SetUnlocksSubscriber, EditRelicsSubscriber, OnCardUseSubscriber, PostDeathSubscriber, PostDrawSubscriber,
        OnPowersModifiedSubscriber {

    public static final Logger logger = LogManager.getLogger(ChronoMod.class.getName());
    
    private static final String MOD_NAME = "The Disciple";
    private static final String AUTHOR = "Chronometrics";
    private static final String DESCRIPTION = "The Disciple is a challenging custom Slay the Spire character themed after the Time Eater. The deck is designed around choosing the correct time for cards to be played to gain max value, and has four central themes: Card Retention, Intent Shifting, Card Transforming, and Temporary Relic Cycling.";

    public static Color BRONZE = new Color(215f / 255f, 145f / 255f, 0f, 1f);
    public static Color DARKBRONZE = new Color(155f / 255f, 105f / 255f, 0f, 1f);

    public static Gson gson;
    private static Map<String, Keyword> keywords;

    public static ArrayList<String> card_uses = new ArrayList();
    public static ArrayList<String> card_discards = new ArrayList();

    @SuppressWarnings("deprecation")
    public ChronoMod() {
        BaseMod.subscribe(this);
    }

    public static void log(String s) {
        logger.info(s);
    }

    public static void addCardUsage(String cardName) {
        card_uses.add(cardName);
    }

    public static void addCardDiscard(String cardName) {
        card_discards.add(cardName);
    }

    public void receivePreStartGame() {
        card_uses.clear();
        card_discards.clear();
        // setGameSwitchCards();
    }

    public static void initialize() {
        logger.info("========================= CHRONOMUNCHER IS A GO =========================");

        logger.info("  _____ _            ____  _          _       _      ");
        logger.info(" |_   _| |__   ___  |  _ \\(_)___  ___(_)_ __ | | ___ ");
        logger.info("   | | | '_ \\ / _ \\ | | | | / __|/ __| | '_ \\| |/ _ \\");
        logger.info("   | | | | | |  __/ | |_| | \\__ \\ (__| | |_) | |  __/");
        logger.info("   |_| |_| |_|\\___| |____/|_|___/\\___|_| .__/|_|\\___|");
        logger.info("                                       |_|           ");

        BaseMod.addColor(
                Enum.BRONZE, 
                BRONZE, // bgColor
                // BRONZE, // back Color            - RED   -
                // BRONZE, // frame Color           - GREEN -
                // new Color(1f, 1f, 0f, 1f), // Card PLayed VFX Trail
                // new Color(1f, 1f, 0f, 1f), // descBoxColor          - YELLOW-
                // BRONZE,                    // Collection Page Frame Colour (and maybe VFX Trail, the data is unclear)
                // new Color(1f, 0f, 1f, 1f), // glowColor             - MAGEN -  The colour of the tiny cards in the statistics page?
                "images/cardui/bg_attack_bronze_512.png", 
                "images/cardui/bg_skill_bronze_512.png",
                "images/cardui/bg_power_bronze_512.png",
                "images/cardui/card_bronze_orb_512.png",
                "images/cardui/bg_attack_bronze.png", 
                "images/cardui/bg_skill_bronze.png",
                "images/cardui/bg_power_bronze.png",
                "images/cardui/card_bronze_orb.png",
                "images/cardui/description_bronze_orb.png"
        );

        @SuppressWarnings("unused")
        ChronoMod chronoMod = new ChronoMod();

        logger.info("=========================================================================");
    }    
    
    public void receivePostInitialize() {
        BaseMod.addPotion(HastePotion.class, Color.SKY, Color.TAN, null, "HastePotion", Enum.CHRONO_CLASS);
        BaseMod.addPotion(WardPotion.class, Color.SLATE, null, Color.ROYAL, "WardPotion", Enum.CHRONO_CLASS);

        Texture badgeTexture = ImageMaster.loadImage("images/badge.png");
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, null);
    }
        
    public void receivePostDeath() {
        customMetrics metrics = new customMetrics();
        
        Thread t = new Thread(metrics);
        t.setName("Metrics");
        t.start();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(Chronomuncher.class, "The Disciple", 
                "The Disciple class", Enum.BRONZE, 
                "The Disciple", 
                "images/char/chronoButton.png", 
                "images/char/chronoPortrait.png", 
                Enum.CHRONO_CLASS);
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics....");
        BaseMod.addRelicToCustomPool(new Chronometer(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new Chronograph(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new Metronome(), Enum.BRONZE);
        // BaseMod.addRelicToCustomPool(new Lockbox(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new Cryopreserver(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new Carbonhydrate(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new SlipperyGoo(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new HangingClock(), Enum.BRONZE);
        BaseMod.addRelicToCustomPool(new BlueBox(), Enum.BRONZE);
    }

    public void setCompendiumSwitchCards() {
        ArrayList<AbstractCard> cards = new ArrayList();

        AbstractCard c;
        c = new SwitchReapSow("Reap");
        c.cardID = "Reap";
        BaseMod.addCard(c);
        BaseMod.addCard(new SwitchReapSow("Sow"));

        AbstractCard d;
        d = new SwitchExoVibe("Exocoating");
        d.cardID = "Exocoating";
        BaseMod.addCard(d);
        BaseMod.addCard(new SwitchExoVibe("CoatedVibrissa"));

        AbstractCard e;
        e = new SwitchGoo("AcidicGoo");
        e.cardID = "AcidicGoo";
        BaseMod.addCard(e);
        AbstractCard f;
        f = new SwitchGoo("ThickGoo");
        f.cardID = "ThickGoo";
        BaseMod.addCard(f);
        BaseMod.addCard(new SwitchGoo("ViscousGoo"));

        AbstractCard g;
        g = new SwitchSavings("SpringForward");
        g.cardID = "SpringForward";
        BaseMod.addCard(g);
        BaseMod.addCard(new SwitchSavings("Fallback"));

        AbstractCard h;
        h = new SwitchSharpShooter("ClockandLoad");
        h.cardID = "ClockandLoad";
        BaseMod.addCard(h);
        BaseMod.addCard(new SwitchSharpShooter("FastForward"));
    }

    public void setGameSwitchCards() {
        BaseMod.removeCard("Reap", Enum.BRONZE);
        BaseMod.removeCard("ReapSow", Enum.BRONZE);

        BaseMod.removeCard("Exocoating", Enum.BRONZE);
        BaseMod.removeCard("ExoVibe", Enum.BRONZE);

        BaseMod.removeCard("AcidicGoo", Enum.BRONZE);
        BaseMod.removeCard("ThickGoo", Enum.BRONZE);
        BaseMod.removeCard("Goo", Enum.BRONZE);

        BaseMod.removeCard("SpringForward", Enum.BRONZE);
        BaseMod.removeCard("DaylightSavings", Enum.BRONZE);

        BaseMod.removeCard("ClockandLoad", Enum.BRONZE);
        BaseMod.removeCard("SharpShooter", Enum.BRONZE);

        ChronoMod.log("REMOVING CARDS FROM LIBRARY NOW");
        BaseMod.addCard(new SwitchReapSow());
        BaseMod.addCard(new SwitchExoVibe());
        BaseMod.addCard(new SwitchGoo());
        BaseMod.addCard(new SwitchSavings());
        BaseMod.addCard(new SwitchSharpShooter());
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding cards....");

        // setCompendiumSwitchCards();
        BaseMod.addCard(new SwitchReapSow());
        BaseMod.addCard(new SwitchExoVibe());
        BaseMod.addCard(new SwitchGoo());
        BaseMod.addCard(new SwitchSavings());
        BaseMod.addCard(new SwitchSharpShooter());

        // BaseMod.addCard(new ASecondTooLate());
        BaseMod.addCard(new Accelerando());
        BaseMod.addCard(new Accruing());
        // BaseMod.addCard(new AcidicGoo());
        BaseMod.addCard(new Adagio());
        BaseMod.addCard(new Allargando());
        BaseMod.addCard(new Allegretto());
        BaseMod.addCard(new Allegro());
        BaseMod.addCard(new AlternateTimeline());
        BaseMod.addCard(new Analog());
        BaseMod.addCard(new BackFourSeconds());
        BaseMod.addCard(new Backlash());
        BaseMod.addCard(new BeatsPerMinute());
        BaseMod.addCard(new BiteCommand());
        // BaseMod.addCard(new BiteofTime());
        BaseMod.addCard(new BlueShift());
        BaseMod.addCard(new Break());
        BaseMod.addCard(new Chronoelasticity());
        // BaseMod.addCard(new CoatedVibrissa());
        // BaseMod.addCard(new ClockandLoad());
        // BaseMod.addCard(new CraftedGuard());
        BaseMod.addCard(new CrunchTime());
        // BaseMod.addCard(new OldCrunchTime());
        BaseMod.addCard(new Cuckoo());
        BaseMod.addCard(new Defend_Bronze());
        BaseMod.addCard(new Echonomics());
        BaseMod.addCard(new Echoward());
        BaseMod.addCard(new Engulf());
        BaseMod.addCard(new EscortCommand());
        // BaseMod.addCard(new Exocoating());
        BaseMod.addCard(new Facsimile());
        // BaseMod.addCard(new FeedbackCycle());
        // BaseMod.addCard(new Flashback());
        BaseMod.addCard(new Flay());
        // BaseMod.addCard(new FollowThrough());
        // BaseMod.addCard(new Foolish());
        // BaseMod.addCard(new ForgedGuard());
        BaseMod.addCard(new FormalWear());
        BaseMod.addCard(new Fragmentalize());
        BaseMod.addCard(new Grave());
        // BaseMod.addCard(new GuardCommand());
        BaseMod.addCard(new HandsUp());
        BaseMod.addCard(new Keepsakes());
        BaseMod.addCard(new Largo());
        BaseMod.addCard(new Lento());
        BaseMod.addCard(new LockedBlood());
        // BaseMod.addCard(new LockedCalipers());
        BaseMod.addCard(new LockedCalendar());
        BaseMod.addCard(new LockedCarbon());
        BaseMod.addCard(new LockedFlame());
        // BaseMod.addCard(new LockedHand());
        BaseMod.addCard(new LockedIceCream());
        BaseMod.addCard(new LockedLightning());
        BaseMod.addCard(new LockedMedicine());
        BaseMod.addCard(new LockedMercury());
        // BaseMod.addCard(new LockedNitrogen());
        BaseMod.addCard(new LockedOrichalcum());
        BaseMod.addCard(new LockedPlans());
        BaseMod.addCard(new LockedThread());
        BaseMod.addCard(new LockedTornado());
        BaseMod.addCard(new LockedUrn());
        BaseMod.addCard(new LockedWarPaint());
        BaseMod.addCard(new LockedWhetstone());
        BaseMod.addCard(new MasterKey());
        // BaseMod.addCard(new Maestoso());
        BaseMod.addCard(new Misterioso());
        BaseMod.addCard(new Moderato());
        // BaseMod.addCard(new NaturalGuard());
        // BaseMod.addCard(new Nibble());
        BaseMod.addCard(new OldTimer());
        BaseMod.addCard(new OracleForm());
        BaseMod.addCard(new OverTime());
        BaseMod.addCard(new Parity());
        BaseMod.addCard(new PatternShift());
        BaseMod.addCard(new Pendulum());
        // BaseMod.addCard(new PreemptiveStrike());
        BaseMod.addCard(new Presto());
        BaseMod.addCard(new PrimeTime());
        BaseMod.addCard(new RageCommand());
        BaseMod.addCard(new Rallentando());
        // BaseMod.addCard(new Reap());
        BaseMod.addCard(new Recurrance());
        // BaseMod.addCard(new Reiterate());
        BaseMod.addCard(new ResonantCall());
        // BaseMod.addCard(new Rewind());
        BaseMod.addCard(new Ritenuto());
        BaseMod.addCard(new SecondHand());
        // BaseMod.addCard(new SenseKnock());
        BaseMod.addCard(new ShortSighted());
        BaseMod.addCard(new SlimeSpray());
        BaseMod.addCard(new Sospirando());
        // BaseMod.addCard(new Sow());
        // BaseMod.addCard(new SpareTime());
        BaseMod.addCard(new Stagnate());
        BaseMod.addCard(new Strike_Bronze());
        BaseMod.addCard(new Tempo());
        // BaseMod.addCard(new ThickGoo());
        BaseMod.addCard(new TickedOff());
        // BaseMod.addCard(new TimeConsuming());
        // BaseMod.addCard(new TimeDevouring());
        BaseMod.addCard(new TiringSlam());
        BaseMod.addCard(new VestedLegacy());
        // BaseMod.addCard(new ViscousGoo());
        BaseMod.addCard(new Vivace());
        BaseMod.addCard(new WakeUpCall());
        // BaseMod.addCard(new WastingAway());
        BaseMod.addCard(new Ward());
        BaseMod.addCard(new WatchCommand());
        BaseMod.addCard(new WindUp());
    }

    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String keywordStrings = Gdx.files.internal("localization/chronoKeywords.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();

        keywords = (Map)gson.fromJson(keywordStrings, typeToken);

        keywords.forEach((k,v)->{
                // Keyword word = (Keyword)v;
                logger.info("ChronoMod: Adding Keyword - " + v.NAMES[0]);
                BaseMod.addKeyword(v.NAMES, v.DESCRIPTION);
            });
    }

    @Override
    public void receiveEditStrings() {        
        // RelicStrings
        String relicStrings = Gdx.files.internal("localization/chronoRelics.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

        // CardStrings
        String cardStrings = Gdx.files.internal("localization/chronoCards.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);

        // OrbStrings
        String orbStrings = Gdx.files.internal("localization/chronoOrbs.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(OrbStrings.class, orbStrings);

        // PowerStrings
        String powerStrings = Gdx.files.internal("localization/chronoPowers.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);

        // PowerStrings
        String potionStrings = Gdx.files.internal("localization/chronoPotions.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
    }

    public void receiveCardUsed(AbstractCard c)
    { 
        addCardUsage(c.name);

        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof ReplicaOrb) {
                ReplicaOrb u = (ReplicaOrb)o;
                u.onCardUse(c);
            }
        }    
    }

    public void receivePostDraw(AbstractCard c) {
        for (AbstractOrb o : AbstractDungeon.player.orbs) {
            if (o instanceof ReplicaOrb) {
                ReplicaOrb u = (ReplicaOrb)o;
                u.onCardDraw(c);
            }
        }    
    }

    public void receivePowersModified() {
        if (AbstractDungeon.player.hasRelic(Chronometer.ID))  {
            ((Chronometer)AbstractDungeon.player.getRelic(Chronometer.ID)).removeConfusion();
        }
        if (AbstractDungeon.player.hasRelic(Chronograph.ID))  {
            ((Chronograph)AbstractDungeon.player.getRelic(Chronograph.ID)).removeConfusion();
        }
    }

    @Override
    public void receiveSetUnlocks() {
//      UnlockTracker.addCard("Flicker");
//      UnlockTracker.addCard("Transference");
//      UnlockTracker.addCard("ForceRipple");
//      // seeker unlock 1
//      BaseMod.addUnlockBundle(new CustomUnlockBundle(
//              "Flicker", "Transference", "ForceRipple"
//              ), TheSeekerEnum.THE_SEEKER, 1);
//      
//      // seeker unlock 2
//      BaseMod.addUnlockBundle(new CustomUnlockBundle(
//              "Channel", "Shimmer", "ThoughtRaze"
//              ), TheSeekerEnum.THE_SEEKER, 2);
//      UnlockTracker.addCard("Channel");
//      UnlockTracker.addCard("Shimmer");
//      UnlockTracker.addCard("ThoughtRaze");
//      
//      // seeker unlock 3 (Vacuum tmp in place of Feedback)
//      BaseMod.addUnlockBundle(new CustomUnlockBundle(
//              "Convergence", "Hypothesis", "Nexus"
//              ), TheSeekerEnum.THE_SEEKER, 3);
//      UnlockTracker.addCard("Convergence");
//      UnlockTracker.addCard("Hypothesis");
//      UnlockTracker.addCard("Nexus");
    }

}
