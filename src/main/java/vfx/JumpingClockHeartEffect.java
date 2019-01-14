package chronomuncher.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Interpolation.SwingIn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class JumpingClockHeartEffect
  extends AbstractGameEffect
{
  private float x;
  private float y;
  private float xPercent;
  private float yPercent;
  private static TextureAtlas.AtlasRegion img = null;
  
  // xPercent is from 0.0 (far left) to 1.0 (far right)
  // yPercent is from 0.0 (off screen to the bottom) to 1.0 (top of graphic touching the top of the screen)
  public JumpingClockHeartEffect(float xPercent, float yPercent, float scale)
  {
    if (img == null) {
      img = AbstractPower.atlas.findRegion("128/time");
    }
    this.startingDuration = 1.25F;
    this.duration = this.startingDuration;
    this.scale = (Settings.scale * 3.0F) * scale;
    this.x = (Settings.WIDTH * xPercent - img.packedWidth);
    this.y = (img.packedHeight / 2.0F);
    this.yPercent = yPercent * 0.7F;
    this.color = Color.WHITE.cpy();
  }
  
  public void update()
  {
    this.duration -= Gdx.graphics.getDeltaTime();
    if (this.duration < 0.0F) {
      this.isDone = true;
    }
    if (this.duration < 0.5F) {
      this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration * 2.0F);
    } else {
      this.y = Interpolation.swingIn.apply(Settings.HEIGHT * this.yPercent - img.packedHeight / 2.0F, -img.packedHeight / 2.0F, this.duration - 0.25F);
    }
  }
  
  public void render(SpriteBatch sb)
  {
    sb.setColor(this.color);
    sb.draw(img, this.x, this.y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, this.scale, this.scale, this.duration * 360.0F);
  }

  public void dispose() {}
}
