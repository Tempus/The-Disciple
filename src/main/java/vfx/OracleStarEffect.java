package chronomuncher.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class OracleStarEffect
  extends AbstractGameEffect
{
  private float effectDuration;
  private float x;
  private float y;
  private float sX;
  private float sY;
  private float tX;
  private float tY;
  private TextureAtlas.AtlasRegion img = ImageMaster.GLOW_SPARK_2;
  
  public OracleStarEffect()
  {
    this.effectDuration = 1.0F;
    this.duration = this.effectDuration;
    this.startingDuration = this.effectDuration;
    
    this.x = (MathUtils.random(0.0F, Settings.WIDTH) - this.img.packedWidth / 2.0F);
    this.y = (MathUtils.random(0.0F, Settings.HEIGHT) - this.img.packedHeight / 2.0F);
    this.sX = this.x + MathUtils.random(-30.0F, 30.0F) * Settings.scale;
    this.sY = this.y + MathUtils.random(-30.0F, 30.0F) * Settings.scale;
    this.tX = x;
    this.tY = y;
    
    int tmp = MathUtils.random(2);
    if (tmp == 0) {
      this.color = Settings.LIGHT_YELLOW_COLOR.cpy();
    } else if (tmp == 1) {
      this.color = Color.SKY.cpy();
    } else {
      this.color = Color.WHITE.cpy();
    }
    this.scale = (MathUtils.random(0.3F, 1.2F) * Settings.scale);
    this.renderBehind = MathUtils.randomBoolean(0.3F);
  }
  
  public void update()
  {
    this.x = Interpolation.sine.apply(this.tX, this.sX, this.duration);
    this.y = Interpolation.sine.apply(this.tY, this.sY, this.duration);
    
    super.update();
  }
  
  public void render(SpriteBatch sb)
  {
    sb.setColor(this.color);
    sb.setBlendFunction(770, 1);
    sb.draw(this.img, this.x - this.img.packedWidth / 2.0F, this.y - this.img.packedWidth / 2.0F, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 
    
      MathUtils.random(0.7F, 1.4F), this.scale * 
      MathUtils.random(0.7F, 1.4F), this.rotation);
    
    sb.setBlendFunction(770, 771);
  }

  public void dispose() {}
}
