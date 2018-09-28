package chronomuncher.patches;

import com.megacrit.cardcrawl.metrics.Metrics;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.screens.DeathScreen;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chronomuncher.ChronoMod;
import chronomuncher.patches.Enum;

public class customMetrics implements Runnable {

  private HashMap<Object, Object> params = new HashMap();
  private Gson gson = new Gson();
  private long lastPlaytimeEnd;
  public static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

  public static final String URL = "http://www.chronometry.ca/metrics/";


  private void addData(Object key, Object value)
  {
    this.params.put(key, value);
  }
  
  private void sendPost()
  {
    HashMap<String, Serializable> event = new HashMap();
    event.put("event", this.params);
    event.put("name", CardCrawlGame.playerName);
    event.put("alias", CardCrawlGame.alias);
    
    event.put("time", Long.valueOf(System.currentTimeMillis() / 1000L));
    String data = this.gson.toJson(event);
    HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
    
    Net.HttpRequest httpRequest = requestBuilder.newRequest().method("POST").url(URL).header("Content-Type", "application/json").header("Accept", "application/json").header("User-Agent", "curl/7.43.0").build();
    httpRequest.setContent(data);
    Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener()
    {
      public void handleHttpResponse(Net.HttpResponse httpResponse) {
      }
      
      public void failed(Throwable t) {
      }
      
      public void cancelled() {
      }
    });
  }

  private void gatherAllData()
  {
    Boolean death = AbstractDungeon.deathScreen.isVictory;
    addData("play_id", UUID.randomUUID().toString());
    addData("build_version", CardCrawlGame.TRUE_VERSION_NUM);
    addData("seed_played", Settings.seed.toString());
    addData("chose_seed", Boolean.valueOf(Settings.seedSet));
    addData("seed_source_timestamp", Long.valueOf(Settings.seedSourceTimestamp));
    addData("is_daily", Boolean.valueOf(Settings.isDailyRun));
    addData("special_seed", Settings.specialSeed);
    addData("is_trial", Boolean.valueOf(Settings.isTrial));
    addData("is_endless", Boolean.valueOf(Settings.isEndless));
    if (death)
    {
      AbstractPlayer player = AbstractDungeon.player;
      CardCrawlGame.metricData.current_hp_per_floor.add(Integer.valueOf(player.currentHealth));
      CardCrawlGame.metricData.max_hp_per_floor.add(Integer.valueOf(player.maxHealth));
      CardCrawlGame.metricData.gold_per_floor.add(Integer.valueOf(player.gold));
    }
    addData("is_ascension_mode", Boolean.valueOf(AbstractDungeon.isAscensionMode));
    addData("ascension_level", Integer.valueOf(AbstractDungeon.ascensionLevel));
    
    addData("neow_bonus", CardCrawlGame.metricData.neowBonus);
    addData("neow_cost", CardCrawlGame.metricData.neowCost);
    
    addData("is_beta", Boolean.valueOf(Settings.isBeta));
    addData("is_prod", Boolean.valueOf(Settings.isDemo));
    addData("victory", Boolean.valueOf(!death));
    addData("floor_reached", Integer.valueOf(AbstractDungeon.floorNum));
    addData("score", Integer.valueOf(DeathScreen.calcScore(!death)));
    this.lastPlaytimeEnd = (System.currentTimeMillis() / 1000L);
    addData("timestamp", Long.valueOf(this.lastPlaytimeEnd));
    addData("local_time", timestampFormatter.format(Calendar.getInstance().getTime()));
    addData("playtime", Float.valueOf(CardCrawlGame.playtime));
    addData("player_experience", Long.valueOf(Settings.totalPlayTime));
    addData("master_deck", AbstractDungeon.player.masterDeck.getCardIdsForMetrics());
    addData("relics", AbstractDungeon.player.getRelicNames());
    addData("gold", Integer.valueOf(AbstractDungeon.player.gold));
    addData("campfire_rested", Integer.valueOf(CardCrawlGame.metricData.campfire_rested));
    addData("campfire_upgraded", Integer.valueOf(CardCrawlGame.metricData.campfire_upgraded));
    addData("purchased_purges", Integer.valueOf(CardCrawlGame.metricData.purchased_purges));
    addData("potions_floor_spawned", CardCrawlGame.metricData.potions_floor_spawned);
    addData("potions_floor_usage", CardCrawlGame.metricData.potions_floor_usage);
    addData("current_hp_per_floor", CardCrawlGame.metricData.current_hp_per_floor);
    addData("max_hp_per_floor", CardCrawlGame.metricData.max_hp_per_floor);
    addData("gold_per_floor", CardCrawlGame.metricData.gold_per_floor);
    addData("path_per_floor", CardCrawlGame.metricData.path_per_floor);
    addData("path_taken", CardCrawlGame.metricData.path_taken);
    addData("items_purchased", CardCrawlGame.metricData.items_purchased);
    addData("item_purchase_floors", CardCrawlGame.metricData.item_purchase_floors);
    addData("items_purged", CardCrawlGame.metricData.items_purged);
    addData("items_purged_floors", CardCrawlGame.metricData.items_purged_floors);
    addData("character_chosen", AbstractDungeon.player.chosenClass.name());
    addData("card_choices", CardCrawlGame.metricData.card_choices);
    addData("event_choices", CardCrawlGame.metricData.event_choices);
    addData("boss_relics", CardCrawlGame.metricData.boss_relics);
    addData("damage_taken", CardCrawlGame.metricData.damage_taken);
    addData("potions_obtained", CardCrawlGame.metricData.potions_obtained);
    addData("relics_obtained", CardCrawlGame.metricData.relics_obtained);
    addData("campfire_choices", CardCrawlGame.metricData.campfire_choices);
    addData("card_uses", ChronoMod.card_uses);
    addData("card_discards", ChronoMod.card_discards);
  }  
  
  public void run()
  {
    if (AbstractDungeon.player.chosenClass == Enum.CHRONO_CLASS) { 
      if ((Settings.UPLOAD_DATA) && (Settings.isStandardRun())) {
        gatherAllData();
        sendPost();
      }
    }
  }
}
