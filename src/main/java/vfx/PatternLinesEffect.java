package chronomuncher.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.ShineLinesEffect;
import com.badlogic.gdx.math.Interpolation.*;

public class PatternLinesEffect
  extends ShineLinesEffect
{  
  public float rotationSpeed;

  public PatternLinesEffect(float x, float y)
  {
  	super(x,y);
  	this.scale = this.scale * MathUtils.random(0.5F, 4.0F);

  	this.rotationSpeed = MathUtils.random(180.0F, 960.0F);
    this.duration = 0.5F;
    this.startingDuration = 0.5F;

    int tmp = MathUtils.random(5);
    if (tmp == 0) {
      this.color = Color.GOLD.cpy();
    } else if (tmp < 3) {
      this.color = Color.WHITE.cpy();
    } else {
      this.color = Color.SKY.cpy();
    }
    this.color.a = 0.75F;
  }
 
  @Override 
  public void update()
  {
    super.update();
    // this.scale -= Gdx.graphics.getDeltaTime() * 22.0F;
    this.rotation += Gdx.graphics.getDeltaTime() * this.rotationSpeed;
  }

  public void dispose() {}
}
