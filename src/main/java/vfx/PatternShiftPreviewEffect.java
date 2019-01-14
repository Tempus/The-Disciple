package chronomuncher.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Interpolation.SwingOut;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PatternShiftPreviewEffect
  extends AbstractGameEffect
{
  private float effectDuration;
  private float cx;
  private float cy;
  private float x;
  private float y;
  private float sX;
  private float sY;
  private float tX;
  private float tY;
  private AtlasRegion img = ImageMaster.GLOW_SPARK_2;
  
  public PatternShiftPreviewEffect(float x, float y, float duration, float scale)
  {
    this.effectDuration = duration;
    this.duration = this.effectDuration;
    this.startingDuration = this.effectDuration;
    
    this.cx = x;
    this.cy = y;

    this.x = (x + MathUtils.random(-30.0F, 30.0F) * Settings.scale * scale);
    this.y = (y + MathUtils.random(-30.0F, 30.0F) * Settings.scale * scale);
    this.sX = this.x;
    this.sY = this.y;
    this.tX = x;
    this.tY = y;
    
    this.scale = (MathUtils.random(0.3F, 1.2F) * Settings.scale * scale);
    this.renderBehind = true;

    int tmp = MathUtils.random(5);
    if (tmp == 0) {
      this.color = Color.GOLD.cpy();
      this.scale = this.scale / 4.0F;
    } else if (tmp < 3) {
      this.color = Color.SKY.cpy();
    } else {
      this.color = Color.SLATE.cpy();
    }
    this.color.a = 0.25F;
  }
  
  public void update()
  {
    float s = MathUtils.sin(0.04F);
    float c = MathUtils.cos(0.04F);

    this.rotation += 4.0F;

    // translate point back to origin:
    this.sX -= this.cx;
    this.sY -= this.cy;

    // rotate point
    float xnew = this.sX * c - this.sY * s;
    float ynew = this.sX * s + this.sY * c;

    // translate point back:
    this.sX = xnew + this.cx;
    this.sY = ynew + this.cy;

    this.x = Interpolation.swingOut.apply(this.tX, this.sX, this.duration * 1.5F);
    this.y = Interpolation.swingOut.apply(this.tY, this.sY, this.duration * 1.5F);
    
    super.update();
  }
  
  public void render(SpriteBatch sb)
  {
    sb.setColor(this.color);
    sb.setBlendFunction(770, 771);
    sb.draw(this.img, this.x - this.img.packedWidth / 2.0F, this.y - this.img.packedWidth / 2.0F, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 
    
      MathUtils.random(0.7F, 1.4F), this.scale * 
      MathUtils.random(0.7F, 1.4F), this.rotation);
    
    sb.setBlendFunction(770, 771);
  }

  public void dispose() {}
}
