package chronomuncher.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import java.util.ArrayList;

import basemod.abstracts.CustomEnergyOrb;

public class EnergyOrbGold
  extends CustomEnergyOrb
{
  private static final int ORB_W = 128;
  public static float fontScale = 1.0F;
  private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
  public static int totalCount = 0;
  
  private float w = 0F;
  private float h = 0F;

  public float angle1 = 0F;
  public float angle2 = 0F;
  public float angle3 = 0F;
  public float angle4 = 0F;
  public float angle5 = 0F;
  public float angle6 = 0F;
  public float angle7 = 0F;

  public static final ArrayList<Texture> energyActiveLayers = new ArrayList<Texture>();
  public static final ArrayList<Texture> energyDisabledLayers = new ArrayList<Texture>();

  private Texture defaultOrb = ImageMaster.loadImage("chrono_images/char/orb/neworb/vfx.png");

  public EnergyOrbGold() {
      super(null, null, null);
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer1.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer2.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer3.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer4.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer5.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer6.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer7.png"));
      this.energyActiveLayers.add(new Texture("chrono_images/char/orb/neworb/active/layer8.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer1.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer2.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer3.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer4.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer5.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer6.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer7.png"));
      this.energyDisabledLayers.add(new Texture("chrono_images/char/orb/neworb/disabled/layer8.png"));

      for (Texture tex : this.energyActiveLayers) {
          tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      }
      for (Texture tex : this.energyDisabledLayers) {
          tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
      }
  }
  
  @Override
  public void updateOrb(int energy)
  {
    if (energy > 0){
        this.angle7 += Gdx.graphics.getDeltaTime() * 40.0F;
        this.angle6 += Gdx.graphics.getDeltaTime() * 20.0F;
        this.angle5 += Gdx.graphics.getDeltaTime() * -10.0F;
        this.angle4 += Gdx.graphics.getDeltaTime() * -20.0F;
        this.angle3 += Gdx.graphics.getDeltaTime() * -30.0F;
        this.angle2 += Gdx.graphics.getDeltaTime() * -80.0F;
        this.angle1 += Gdx.graphics.getDeltaTime() * -160.0F;
    } else {
        this.angle7 += Gdx.graphics.getDeltaTime() * 10.0F;
        this.angle6 += Gdx.graphics.getDeltaTime() * 5.0F;
        this.angle5 += Gdx.graphics.getDeltaTime() * -2.5F;
        this.angle4 += Gdx.graphics.getDeltaTime() * -5.0F;
        this.angle3 += Gdx.graphics.getDeltaTime() * -7.5F;
        this.angle2 += Gdx.graphics.getDeltaTime() * -10.0F;
        this.angle1 += Gdx.graphics.getDeltaTime() * -20.0F;
    }
  }
  
  public void saneDraw(float current_x, float current_y, SpriteBatch sb, Texture tex, float xOffset, float yOffset, float rotation) {
      this.w = tex.getWidth();
      this.h = tex.getHeight();

      sb.draw(tex, current_x - (this.w/2.0F) + xOffset, current_y - (this.h/2.0F) + yOffset, this.w/2.0F, this.h/2.0F, this.w, this.h, ORB_IMG_SCALE, ORB_IMG_SCALE, rotation, 0, 0, (int)this.w, (int)this.h, false, false);
  }

  @Override
  public Texture getEnergyImage()
  {
    return defaultOrb;
  }

  @Override
  public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y)
  {
    if (enabled)
    {
        sb.setColor(Color.WHITE);
        
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(0), 0F, 0F, this.angle4);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(1), -3.0F, -4.0F, this.angle3);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(2), 0F, -2.0F, this.angle5);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(3), 0F, -2.0F, this.angle1);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(4), -10.0F, 0F, this.angle6);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(5), 0F, -2.0F, this.angle2);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(6), 0F, 0F, 0);
        this.saneDraw(current_x, current_y, sb, this.energyActiveLayers.get(7), 0F, 0F, 0);
    }
    else
    {
        sb.setColor(Color.WHITE);
        
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(0), 0F, 0F, this.angle4);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(1), -3.0F, -4.0F, this.angle3);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(2), 0F, -2.0F, this.angle5);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(3), 0F, -2.0F, this.angle1);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(4), -10.0F, 0F, this.angle6);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(5), 0F, -2.0F, this.angle2);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(6), 0F, 0F, 0);
        this.saneDraw(current_x, current_y, sb, this.energyDisabledLayers.get(7), 0F, 0F, 0);
    }
  }
}
