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
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.unlock.AbstractUnlock.UnlockType;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.animations.TalkAction;

import basemod.BaseMod;
import basemod.interfaces.*;
import basemod.abstracts.CustomUnlockBundle;
import basemod.ReflectionHacks;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import chronomuncher.cards.*;
import chronomuncher.relics.*;
import chronomuncher.potions.*;
import chronomuncher.events.*;
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
        OnPowersModifiedSubscriber, PreMonsterTurnSubscriber {

    public static final Logger logger = LogManager.getLogger(ChronoMod.class.getName());
    
    private static final String MOD_NAME = "The Disciple";
    private static final String AUTHOR = "Chronometrics";
    private static final String DESCRIPTION = "The Disciple is a challenging custom Slay the Spire character themed after the Time Eater. The deck is designed around choosing the correct time for cards to be played to gain max value, and has four central themes: Card Retention, Intent Shifting, Card Transforming, and Temporary Relic Cycling.";

    public static Color CHRONO_GOLD = new Color(215f / 255f, 145f / 255f, 0f, 1f);
    public static Color DARKCHRONO_GOLD = new Color(155f / 255f, 105f / 255f, 0f, 1f);

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
                Enum.CHRONO_GOLD, 
                CHRONO_GOLD, // bgColor
                // CHRONO_GOLD, // back Color            - RED   -
                // CHRONO_GOLD, // frame Color           - GREEN -
                // new Color(1f, 1f, 0f, 1f), // Card PLayed VFX Trail
                // new Color(1f, 1f, 0f, 1f), // descBoxColor          - YELLOW-
                // CHRONO_GOLD,                    // Collection Page Frame Colour (and maybe VFX Trail, the data is unclear)
                // new Color(1f, 0f, 1f, 1f), // glowColor             - MAGEN -  The colour of the tiny cards in the statistics page?
                "chrono_images/cardui/bg_attack_bronze_512.png", 
                "chrono_images/cardui/bg_skill_bronze_512.png",
                "chrono_images/cardui/bg_power_bronze_512.png",
                "chrono_images/cardui/card_bronze_orb_512.png",
                "chrono_images/cardui/bg_attack_bronze.png", 
                "chrono_images/cardui/bg_skill_bronze.png",
                "chrono_images/cardui/bg_power_bronze.png",
                "chrono_images/cardui/card_bronze_orb.png",
                "chrono_images/cardui/description_bronze_orb.png"
        );

        @SuppressWarnings("unused")
        ChronoMod chronoMod = new ChronoMod();

        logger.info("=========================================================================");
    }    
    
    public void receivePostInitialize() {
        BaseMod.addPotion(HastePotion.class, Color.SKY, Color.TAN, null, "HastePotion", Enum.CHRONO_CLASS);
        BaseMod.addPotion(WardPotion.class, Color.SLATE, null, Color.ROYAL, "WardPotion", Enum.CHRONO_CLASS);

        Texture badgeTexture = ImageMaster.loadImage("chrono_images/badge.png");
        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, null);

        this.loadAudio();

        // outputSQLListsForMetrics();
    }

    public void outputSQLListsForMetrics() {
        ArrayList<AbstractCard> cards = new ArrayList();
        cards.addAll(CardLibrary.getCardList(EnumLib.CHRONO_GOLD));
        cards.addAll(CardLibrary.getCardList(CardLibrary.LibraryType.COLORLESS));
        cards.addAll(CardLibrary.getCardList(CardLibrary.LibraryType.CURSE));

        ChronoMod.log("Cards in cardlist: " + CardLibrary.getCardList(EnumLib.CHRONO_GOLD).size());

        String cardstring = "INSERT INTO `meta_card_data` (`id`, `name`, `character_class`, `neutral`, `invalid`, `rarity`, `type`, `cost`, `description`, `ignore_before`, `updated_on`, `score`, `a0_total`, `a114_total`, `a15_total`, `pick_updated_on`, `a0_pick`, `a114_pick`, `a15_pick`, `a0_not_pick`, `a114_not_pick`, `a15_not_pick`, `up_updated_on`, `a0_up`, `a114_up`, `a15_up`, `a0_purchased`, `a114_purchased`, `a15_purchased`, `a0_purged`, `a114_purged`, `a15_purged`, `wr_updated_on`, `a0_wr`, `a114_wr`, `a15_wr`, `a0_floor`, `a114_floor`, `a15_floor`, `a0_floordetails`, `a114_floordetails`, `a15_floordetails`) VALUES ";
        cardstring = cardstring + "(0,'',1,0,0,'','','','',NULL,'0000-00-00 00:00:00',0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,'','',''),";

        int i = 0;
        for (AbstractCard c : cards) {
            i++;
            cardstring = cardstring + String.format("(%d,'%s',1,0,0,'%s','%s','%d','%s',NULL,'0000-00-00 00:00:00',0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,0,0,0,'0000-00-00 00:00:00',0,0,0,0,0,0,'','',''),", i, c.cardID, titleCase(c.rarity.name()), titleCase(c.type.name()), c.cost, c.rawDescription.replace("'","\'"));
        }

        cardstring = cardstring.substring(0, cardstring.length() - 1) + ";/*!40000 ALTER TABLE `meta_card_data` ENABLE KEYS */;";

        ChronoMod.log(cardstring);

        ChronoMod.log(" ");
        ChronoMod.log(" ");
        ChronoMod.log(" ");


        ArrayList<AbstractRelic> relics = new ArrayList<>();
        HashMap<String,AbstractRelic> sharedRelics = (HashMap<String,AbstractRelic>)ReflectionHacks.getPrivateStatic(RelicLibrary.class, "sharedRelics");
        for (AbstractRelic relic : sharedRelics.values()) {
            relics.add(relic);
        }
        for (HashMap.Entry<AbstractCard.CardColor,HashMap<String,AbstractRelic>> entry : BaseMod.getAllCustomRelics().entrySet()) {
            for (AbstractRelic relic : entry.getValue().values()) {
                relics.add(relic);
            }
        }

        String relicstring = "INSERT INTO `meta_relic_data` (`id`, `name`, `invalid`, `character_class`, `description`, `rarity`, `event_id`, `ignore_before`) VALUES ";


        i = 0;
        for (AbstractRelic relic: relics) {
            i++;
            relicstring = relicstring + String.format("(%d,'%s',0,1,'%s','%s',0,'0000-00-00'),", i, relic.name, relic.description, relic.tier.name().toLowerCase());
        }
        ChronoMod.log(relicstring);


    }

    public static String titleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
     
        StringBuilder converted = new StringBuilder();
     
        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
     
        return converted.toString();
    }

    public void loadAudio() {
        HashMap<String, Sfx> map = (HashMap<String, Sfx>)ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put("CHRONO-COGS", new Sfx("chrono_audio/Cogs.ogg", false));
        map.put("CHRONO-SHARP1", new Sfx("chrono_audio/Slide_Sharp_01.ogg", false));
        map.put("CHRONO-SHARP2", new Sfx("chrono_audio/Slide_Sharp_02.ogg", false));
        map.put("CHRONO-SLOWDOWN", new Sfx("chrono_audio/SlowDown.ogg", false));
        map.put("CHRONO-SPEEDUP", new Sfx("chrono_audio/SpeedUp.ogg", false));
        map.put("CHRONO-LOWWHOOSH", new Sfx("chrono_audio/LowWhoosh.ogg", false));
        map.put("CHRONO-TICKINGCLEAN", new Sfx("chrono_audio/TickingClean.ogg", false));
        map.put("CHRONO-TICKINGDIRTY", new Sfx("chrono_audio/TickingDirty.ogg", false));
        map.put("CHRONO-TICK", new Sfx("chrono_audio/Tick.ogg", false));
        map.put("CHRONO-CHIME", new Sfx("chrono_audio/Chime.ogg", false));
        map.put("CHRONO-WINDUP", new Sfx("chrono_audio/WindUp.ogg", false));
        map.put("CHRONO-SHORTSLEEP", new Sfx("chrono_audio/ShortSleep.ogg", false));
        map.put("CHRONO-ELASTIC", new Sfx("chrono_audio/Elastic.ogg", false));
        map.put("CHRONO-CUCKOO", new Sfx("chrono_audio/Cuckoo.ogg", false));
    }
        
    public void receivePostDeath() {
        customMetrics metrics = new customMetrics();
        
        Thread t = new Thread(metrics);
        t.setName("Metrics");
        t.start();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(
            new Chronomuncher(CardCrawlGame.playerName),
            // Enum.CHRONO_GOLD, 
            "chrono_images/char/chronoButton.png", 
            "chrono_images/char/chronoPortrait.png", 
            Enum.CHRONO_CLASS);
    }

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics....");
        BaseMod.addRelicToCustomPool(new Chronometer(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new Chronograph(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new Metronome(), Enum.CHRONO_GOLD);
        // BaseMod.addRelicToCustomPool(new Lockbox(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new Cryopreserver(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new PaperTurtyl(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new SlipperyGoo(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new HangingClock(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new BlueBox(), Enum.CHRONO_GOLD);
        // BaseMod.addRelicToCustomPool(new SpikedShell(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new SpringShield(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new HeavySwitch(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new MysticCrockPot(), Enum.CHRONO_GOLD);
        // BaseMod.addRelicToCustomPool(new SleevePocket(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new WaitingRoom(), Enum.CHRONO_GOLD);

        BaseMod.addRelicToCustomPool(new ReplicaOrichalcum(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaMedicine(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaWarPaint(), Enum.CHRONO_GOLD);

        BaseMod.addRelicToCustomPool(new ReplicaScales(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaMercury(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaWhetstone(), Enum.CHRONO_GOLD);

        BaseMod.addRelicToCustomPool(new ReplicaNitrogen(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaIceCream(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaHand(), Enum.CHRONO_GOLD);

        BaseMod.addRelicToCustomPool(new ReplicaFlame(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaLightning(), Enum.CHRONO_GOLD);
        BaseMod.addRelicToCustomPool(new ReplicaTornado(), Enum.CHRONO_GOLD);
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
        BaseMod.addCard(new SwitchSharpShooter("FastForwardS"));
    }

    public void setGameSwitchCards() {
        BaseMod.removeCard("Reap", Enum.CHRONO_GOLD);
        BaseMod.removeCard("ReapSow", Enum.CHRONO_GOLD);

        BaseMod.removeCard("Exocoating", Enum.CHRONO_GOLD);
        BaseMod.removeCard("ExoVibe", Enum.CHRONO_GOLD);

        BaseMod.removeCard("AcidicGoo", Enum.CHRONO_GOLD);
        BaseMod.removeCard("ThickGoo", Enum.CHRONO_GOLD);
        BaseMod.removeCard("Goo", Enum.CHRONO_GOLD);

        BaseMod.removeCard("SpringForward", Enum.CHRONO_GOLD);
        BaseMod.removeCard("DaylightSavings", Enum.CHRONO_GOLD);

        BaseMod.removeCard("ClockandLoad", Enum.CHRONO_GOLD);
        BaseMod.removeCard("SharpShooter", Enum.CHRONO_GOLD);

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
        // BaseMod.addCard(new SwitchSharpShooter());

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
        BaseMod.addCard(new LockedAnchor());
        BaseMod.addCard(new LockedAstrolabe());
        BaseMod.addCard(new LockedBell());
        BaseMod.addCard(new LockedBlood());
        // BaseMod.addCard(new LockedCalipers());
        BaseMod.addCard(new LockedCalendar());
        BaseMod.addCard(new LockedTurtyl());
        BaseMod.addCard(new LockedFlame());
        // BaseMod.addCard(new LockedHand());
        // BaseMod.addCard(new LockedIceCream());
        BaseMod.addCard(new LockedLightning());
        BaseMod.addCard(new LockedMawBank());
        BaseMod.addCard(new LockedMedicine());
        BaseMod.addCard(new LockedMercury());
        // BaseMod.addCard(new LockedNitrogen());
        BaseMod.addCard(new LockedOrichalcum());
        BaseMod.addCard(new LockedPlans());
        BaseMod.addCard(new LockedScales());
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

        // Eventstring
        String eventStrings = Gdx.files.internal("localization/chronoEvents.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
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

    public boolean receivePreMonsterTurn(AbstractMonster m) {
        if (m.id == "TimeEater" && AbstractDungeon.player.chosenClass == Enum.CHRONO_CLASS) {
            switch (AbstractDungeon.actionManager.turn) {
                case 2:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"What did you forget this time?",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"To give you another good slap to the face.",3.5F,3.5F));
                    break;
                case 3:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"Will you put that clock away for one minute?",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"Time is very complicated I wouldn't expect you to understand.",3.5F,3.5F));
                    break;
                case 4:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"1",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"2",3.5F,3.5F));
                    break;
                case 5:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"3",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"4.",3.5F,3.5F));
                    break;
                case 6:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"5?",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"6.",3.5F,3.5F));
                    break;
                case 7:
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true,"7?!",3.5F,3.5F));
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(m,"8.",3.5F,3.5F));
                    break;
            }
        }
        return true;
    }

    public void receivePowersModified() {
    //     if (AbstractDungeon.player.hasRelic(Chronometer.ID))  {
    //         ((Chronometer)AbstractDungeon.player.getRelic(Chronometer.ID)).removeConfusion();
    //     }
    //     if (AbstractDungeon.player.hasRelic(Chronograph.ID))  {
    //         ((Chronograph)AbstractDungeon.player.getRelic(Chronograph.ID)).removeConfusion();
    //     }
    }

    // Disciple Unlocks
    //
    //  Lv1:
    //      Echonomics, Analog, Resonant Call
    //
    //  Lv2:
    //      SharpShooter, LockedBell, Facsimile
    //
    //  Lv3:
    //      PaperTurtyl, Slippery Goo, Metronome
    //
    //  Lv4:
    //      Beats Per Minute, Wake Up Call, Stagnate
    //
    //  Lv5:
    //      Cryopreserver, HangingClock, BlueBox
    //

    @Override
    public void receiveSetUnlocks() {
        // BaseMod.addUnlockBundle(new CustomUnlockBundle(
        //      "Echonomics", "Analog", "ResonantCall"
        //      ), Enum.CHRONO_CLASS, 1);

        // BaseMod.addUnlockBundle(new CustomUnlockBundle(
        //      "SharpShooter", "LockedBell", "Facsimile"
        //      ), Enum.CHRONO_CLASS, 2);

        // BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC,
        //      "PaperTurtyl", "SlipperyGoo", "Metronome"
        //      ), Enum.CHRONO_CLASS, 3);

        // BaseMod.addUnlockBundle(new CustomUnlockBundle(
        //      "BeatsPerMinute", "WakeUpCall", "Stagnate"
        //      ), Enum.CHRONO_CLASS, 4);

        // BaseMod.addUnlockBundle(new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC,
        //      "Cryopreserver", "HangingClock", "BlueBox"
        //      ), Enum.CHRONO_CLASS, 5);
    }
}
