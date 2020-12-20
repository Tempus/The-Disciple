package chronomuncher.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.buttons.Button;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.FontHelper;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import chronomuncher.ChronoMod;
import basemod.ReflectionHacks;

public class UnlockButton extends Button
{
  public boolean hidden = true;
  public boolean disabled = false;

  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("UnlockButton");
  public static final String[] TEXT = uiStrings.TEXT;

  public UnlockButton(float x, float y, Texture img)
  {
    super(x, y, img);
  }
  
  public void update()
  {
    this.hb.update(this.x, this.y);
    if ((this.hb.hovered) && (InputHelper.justClickedLeft))
    {
      this.pressed = true;
      InputHelper.justClickedLeft = false;
      if (disabled) {return;}
      // When we click the button, the below happens.

      // Unlock all ascensions
      for (CharacterOption o : CardCrawlGame.mainMenuScreen.charSelectScreen.options)
      {
        if (o.name == TEXT[0])
        {
          Prefs pref = o.c.getPrefs();
          ReflectionHacks.setPrivate(o, o.getClass(), "maxAscensionLevel", 20);
          o.c.getCharStat().incrementVictory();
          pref.putInteger("WIN_COUNT", 1);
          pref.putInteger("ASCENSION_LEVEL", 20);
          pref.putInteger("LAST_ASCENSION_LEVEL", 20);
          pref.flush();
          ChronoMod.log ("All Disciple Ascensions unlocked");
          CardCrawlGame.sound.playA("UNLOCK_PING", 0.0F);
          disabled = true;
        }
      }

      // Unlock cards and relics?!
      // UnlockTracker

    }
  }

  public void render(SpriteBatch sb) {
    if (hidden) {return;}

    if (disabled) {
      this.activeColor = new Color(0.2F, 0.2F, 0.2F, 1.0F);
      this.inactiveColor = new Color(0.2F, 0.2F, 0.2F, 1.0F);
    }

    super.render(sb);

    Color fc = Settings.GOLD_COLOR.cpy();
    if (disabled) {
      fc = new Color(0.2F, 0.2F, 0.2F, 1.0F);
    }

    FontHelper.renderFont(sb, FontHelper.cardTitleFont, TEXT[1], this.x+96.0F, 80.0F * Settings.scale, fc);
  }
}
