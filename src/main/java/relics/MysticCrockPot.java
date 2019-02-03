package chronomuncher.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basemod.ReflectionHacks;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import basemod.abstracts.CustomRelic;

import chronomuncher.ChronoMod;
import chronomuncher.patches.RetainedForField;

public class MysticCrockPot extends CustomRelic {
    public static final String ID = "MysticCrockPot";

    public static float stewModifier = 0.2F;

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method="applyPowers")
    public static class applyPowers {
        public static void Postfix(AbstractCard self)
        {
            if (!AbstractDungeon.player.hasRelic(MysticCrockPot.ID)) { return; }

            int tmp = self.damage;
            self.damage = self.damage+MathUtils.floor(self.damage * (stewModifier * RetainedForField.retainedFor.get(self)));
            if (self.damage != tmp) {
                self.isDamageModified = true;
            }
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method="applyPowersToBlock")
    public static class applyPowersToBlock {
        public static void Postfix(AbstractCard self)
        {
            if (!AbstractDungeon.player.hasRelic(MysticCrockPot.ID)) { return; }
            int tmp = self.block;
            self.block = self.block+MathUtils.floor(self.block * (stewModifier * RetainedForField.retainedFor.get(self)));
            if (self.block != tmp) {
                self.isBlockModified = true;
            }
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method="calculateCardDamage")
    public static class calculateCardDamage {
        public static void Postfix(AbstractCard self, AbstractMonster mo)
        {
            if (!AbstractDungeon.player.hasRelic(MysticCrockPot.ID)) { return; }
            int tmp = self.damage;
            self.damage = self.damage+MathUtils.floor(self.damage * (stewModifier * RetainedForField.retainedFor.get(self)));

            if ((boolean)ReflectionHacks.getPrivate(self, AbstractCard.class, "isMultiDamage")) {
                for (int i = 0; i < self.multiDamage.length; i++) {
                    self.multiDamage[i] = self.multiDamage[i]+MathUtils.floor(self.multiDamage[i] * (stewModifier * RetainedForField.retainedFor.get(self)));
                }
            }

            if (self.damage != tmp) {
                self.isDamageModified = true;
            }
        }
    }

    public MysticCrockPot() {
        super(ID, new Texture("chrono_images/relics/MysticCrockPot.png"), new Texture("chrono_images/relics/outline/MysticCrockPot.png"), RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }
    
    @Override
    public AbstractRelic makeCopy() {
        return new MysticCrockPot();
    }
}