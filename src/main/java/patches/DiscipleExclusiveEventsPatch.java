package chronomuncher.patches;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import com.megacrit.cardcrawl.dungeons.*;
import chronomuncher.events.*;
import chronomuncher.patches.Enum;
import chronomuncher.ChronoMod;

public class DiscipleExclusiveEventsPatch {
    @SpirePatch(cls = "com.megacrit.cardcrawl.helpers.EventHelper", method = "getEvent")
    public static class GetEvent{
        public static AbstractEvent Postfix(AbstractEvent __result, String key) {
            switch (key)
            {
            case "Artifactor": 
              return new Artifactor();
            case "Relicator": 
              return new Relicator();
            case "Replicator": 
              return new Replicator();
            }
            return __result;
        }
    }
    
    @SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.Exordium", method = "initializeEventList")
    public static class initializeEventListEx{
     public static void Postfix(AbstractDungeon dungeon) {
            if (dungeon.player.chosenClass == Enum.CHRONO_CLASS) {
                 ChronoMod.log("Adding additional Artifactor.");
             dungeon.eventList.add(Artifactor.ID);
             dungeon.eventList.add(Artifactor.ID);
         }
     }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.TheCity", method = "initializeEventList")
    public static class initializeEventListCity{
        public static void Postfix(AbstractDungeon dungeon) {
            if (dungeon.player.chosenClass == Enum.CHRONO_CLASS) {
                ChronoMod.log("Adding Replicator.");
                dungeon.eventList.add(Replicator.ID);
            }
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.dungeons.TheBeyond", method = "initializeEventList")
    public static class initializeEventListBeyond{
        public static void Postfix(AbstractDungeon dungeon) {
            if (dungeon.player.chosenClass == Enum.CHRONO_CLASS) {
                ChronoMod.log("Adding Relicator.");
                dungeon.eventList.add(Relicator.ID);
            }
        }
    }
}