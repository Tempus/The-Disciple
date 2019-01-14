package chronomuncher.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect;

public class OracleScreenEffect
  extends DeathScreenFloatyEffect
{
  public OracleScreenEffect()
  {
    super();

    this.duration = MathUtils.random(2.0F, 3.0F);
    this.startingDuration = this.duration;

    float tmp = MathUtils.random(0.4F, 0.6F);
    this.color = new Color();
    this.color.r = tmp;
    this.color.g = (tmp + MathUtils.random(0.0F, 0.2F));
    this.color.b = (tmp + MathUtils.random(0.2F, 0.4F));
    this.renderBehind = MathUtils.randomBoolean(0.8F);
    this.scale = (MathUtils.random(12.0F, 20.0F) * Settings.scale);
  }

  public void dispose() {}
}
