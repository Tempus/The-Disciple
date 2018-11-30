package chronomuncher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;

import com.badlogic.gdx.graphics.Color;

import basemod.ReflectionHacks;
import chronomuncher.ChronoMod;
import java.lang.reflect.*;

public class PatternShiftAction extends AbstractGameAction {

	private AbstractPlayer p;
	private AbstractMonster m;
	private boolean upgraded;
	private AbstractCard transformToCard;
  	private AbstractCard transformee;
  	public int seed = 0;
  	public static int StabbyMcStabs = 1;
  	public static boolean entangleReset = false;

  	public static boolean champThresholdReached;
  	public static int champNumTurns;
  	public static int forgeTimes;

	public PatternShiftAction(AbstractPlayer p, AbstractMonster m) {
		this.p = p;
		this.m = m;
		// this.seed = seed;
	}

	public void update() {
		this.isDone = this.nextIntent(this.m);
	}

	public static boolean nextIntent(AbstractMonster m) {
		int count;
		int turnCount;
		int slashCount;
		boolean isAttacking;
		boolean firstTurn;
		boolean firstMove;

		// Special Cases
		switch(m.id) {

			// firstTurn only
			case "Chosen":
			case "AwakenedOne":
			case "Snecko":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
				break;

			case "Dagger":
				if (m.nextMove == 2) {
      				AbstractDungeon.actionManager.addToBottom(new LoseHPAction(m, m, m.currentHealth));
      			}

			// Champ has a bunch of hardcoded things since A19
			case "Champ":
				ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", champNumTurns);
				ReflectionHacks.setPrivate(m, m.getClass(), "forgeTimes", forgeTimes);
				ReflectionHacks.setPrivate(m, m.getClass(), "thresholdReached", champThresholdReached);
				break;

			// firstMove only
			case "SnakeMage":
			case "Byrd":
			case "Shelled Parasite":
			case "Cultist":
			case "JawWorm":
			case "Sentry":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstMove", false);
				break;

			// Donu and deca need to swap
			case "Deca":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;
			case "Donu":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;

			// Exploder advances his explosion timer
			case "Exploder":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "turnCount", turnCount + 1);
				if (turnCount+1 < 3) {
					AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(m, m, "Explosive", 1)); 
				} else {
					AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
					
					AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
					AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, new int[]{30,30,30,30,30}, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
     			}
				break;

			// You can skip Time Eater hasting itself =D
			case "TimeEater":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
				if (m.currentHealth < m.maxHealth / 2) { ReflectionHacks.setPrivate(m, m.getClass(), "usedHaste", true); }
				break;

			// Transient's count increases
			case "Transient":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "count");
				ReflectionHacks.setPrivate(m, m.getClass(), "count", count + 1);
				if (m.hasPower("Fading")) {
					if (m.getPower("Fading").amount == 1) {
				        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
				        AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
				    }
   				}
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(m, m, "Fading", 1));
				break;

			// There's no avoiding Hyper Beam charging... but you can avoid Hyper Beam....?!
			case "BronzeAutomaton":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
				if (count >= 4) { count = 0; }
				ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", count);
				break;

			// Bandits are all dumb and hardcoded now.
			case "BanditBear":
			    switch (m.nextMove)
			    {
			    case 2: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 1: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 3: 
			      m.setMove((byte)1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(0)).base);
			      break;
			    }
				m.createIntent();
				return true;

   			case "BanditLeader":
			    switch (m.nextMove)
			    {
			    case 2: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 1: 
			      m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
			      break;
			    case 3: 
      			  if (AbstractDungeon.ascensionLevel >= 17) {
      			    m.setMove((byte)1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(0)).base);
				  } else {
			        m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)m.damage.get(1)).base);
				  }
			      break;
			    }
				m.createIntent();
				return true;
   				
   			// Looter is annoying, just like Mugger
			case "Looter":
			case "Mugger":
				switch (m.nextMove) {
					case 1:
						slashCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "slashCount");
						m.rollMove();

						if (slashCount == 1) {
							m.setMove((byte)4, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						}

						ReflectionHacks.setPrivate(m, m.getClass(), "slashCount", slashCount+1);
						break;
					case 4:
						m.setMove((byte)2, AbstractMonster.Intent.DEFEND);
						break;
					case 2:
						m.setMove((byte)3, AbstractMonster.Intent.ESCAPE);
						break;
					case 3:
						AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
						break;
				}

				m.createIntent();
				return true;

			// The Collector has all sorts of stuff, but you can skip his ult
			case "TheCollector":
				int turnsTaken = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnsTaken") + 1;
				ReflectionHacks.setPrivate(m, m.getClass(), "turnsTaken", turnsTaken);
				ReflectionHacks.setPrivate(m, m.getClass(), "initialSpawn", false);
				if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
					ReflectionHacks.setPrivate(m, m.getClass(), "ultUsed", true);
				}
				break;

			// Writhing Mass test code?
			case "WrithingMass":
				if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
					ReflectionHacks.setPrivate(m, m.getClass(), "usedMegaDebuff", true);
				}
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "AcidSlime_L":
				ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
				break;

			// Gremlin Wizard charges more or goes back to no charge
			case "GremlinWizard":
				int currentCharge = (int)ReflectionHacks.getPrivate(m, m.getClass(), "currentCharge");

				if (currentCharge >= 3) {
					m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
				} 

				else if ((m.intent == AbstractMonster.Intent.ATTACK)) {
					if (AbstractDungeon.ascensionLevel >= 17) {
						m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
     				} else {
						m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
	   					ReflectionHacks.setPrivate(m, m.getClass(), "currentCharge", 0);
					}
				} else {
					m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
	   				ReflectionHacks.setPrivate(m, m.getClass(), "currentCharge", currentCharge+1);
				}
				m.createIntent();
				return true;

			// Hexaghost, ouch
			case "Hexaghost":
      			ChronoMod.log("Move byte: " + Integer.toString(m.nextMove));
      			Hexaghost hexa = (Hexaghost)m;

				// If you skip the first turn, you need to activate him anyway
				if (m.intent == AbstractMonster.Intent.UNKNOWN) {
					AbstractDungeon.actionManager.addToTop(new ChangeStateAction(m, "Activate"));
      				int d = AbstractDungeon.player.currentHealth / 12 + 1;
      
      				((DamageInfo)m.damage.get(2)).base = d;
      
      				m.applyPowers();
					m.setMove((byte)1, AbstractMonster.Intent.ATTACK, d, 6, true);
					m.createIntent();
					return true;
      			} else {
      				hexa = (Hexaghost)m;

					int orbActiveCount = (int)ReflectionHacks.getPrivate(hexa, Hexaghost.class, "orbActiveCount");
					if (orbActiveCount == 6) {
						hexa.changeState("Deactivate");
					} else {
						hexa.changeState("Activate Orb");
					}
      			}
      			break;

      		// Lagavulin wakes up faster, but is otherwise the same
      		case "Lagavulin":
      			switch (m.nextMove) {
      				case 5: // sleep
						int idleCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "idleCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "idleCount", idleCount + 1);
						if (idleCount + 1 >= 3)
						{
							ReflectionHacks.setPrivate(m, m.getClass(), "isOutTriggered", true);
							AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(m, "OPEN"));
							m.setMove((byte)3, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
							m.createIntent();
							return true;
						}
						break;
					case 1:
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", 0);
						break;
					case 3:
						int debuffTurnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "debuffTurnCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", debuffTurnCount + 1);
						break;
            	}
            	break;

			// Slaver should only try to use entangle once
			case "SlaverRed":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstMove", false);
				if (m.nextMove == 2) { ReflectionHacks.setPrivate(m, m.getClass(), "usedEntangle", true); }
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "SlimeBoss":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
				switch (m.nextMove) {
					case 4:
						m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
						break;
					case 2:
						m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						break;
					case 1:
						m.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
					case 3:
						m.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
				}
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "SpikeSlime_L":
				ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
				break;

			case "Maw":
				ReflectionHacks.setPrivate(m, m.getClass(), "roared", true);
				break;

			// Special case guardian stuff
			case "TheGuardian":
				switch (m.nextMove) {
					case 1:
						m.setMove((byte)3, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						break;
					case 2:
						m.setMove((byte)7, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
					case 3:
						m.setMove((byte)4, AbstractMonster.Intent.ATTACK_BUFF, (int)ReflectionHacks.getPrivate(m, m.getClass(), "twinSlamDamage"), 2, true);
						break;
					case 4:
						m.setMove((byte)5, AbstractMonster.Intent.ATTACK, (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
					    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(m, "Offensive Mode"));
					    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, m, "Sharp Hide"));
  						break;
					case 5:
						m.setMove((byte)6, AbstractMonster.Intent.DEFEND);
						break;
					case 6:
						m.setMove((byte)2, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
						break;
					case 7:
						m.setMove((byte)5, AbstractMonster.Intent.ATTACK, (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
						break;
				}
				m.createIntent();
				return true;
		}

		// try {
		// 	Method method = m.getClass().getDeclaredMethod("getMove", int.class);
		// 	method.setAccessible(true);
		// 	method.invoke(m, seed); 
		// }
		// catch (Throwable e) {
		// ChronoMod.log(e.toString());
		// }
 		m.rollMove();
		m.createIntent();

		return true;
	}

	public static boolean restorePreviewedSpecialCases(AbstractMonster m) {
		int count;
		int turnCount;
		int slashCount;
		boolean isAttacking;

		switch(m.id) {

			case "Dagger":
				m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
      			break;
			// Donu and deca need to swap
			case "Deca":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;
			case "Donu":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;

			case "Exploder":
				m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "turnCount", turnCount - 1);
				break;

			case "GiantHead":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "count");
				ReflectionHacks.setPrivate(m, m.getClass(), "count", count + 1);
				break;

			case "Transient":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "count");
				ReflectionHacks.setPrivate(m, m.getClass(), "count", count - 1);
				m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
				break;

			case "BronzeAutomaton":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
				if (count <= 0) { count = 4; }
				ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", count);
				break;

			case "SlaverRed":
				if (entangleReset) { 
					ReflectionHacks.setPrivate(m, m.getClass(), "usedEntangle", false); 
					entangleReset = false;
				}
				break;

			case "Looter":
			case "Mugger":
				m.tint.changeColor(Color.WHITE.cpy(), 0.6F);
				break;

			// Champ has a bunch of hardcoded things since A19
			case "Champ":
				ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", champNumTurns);
				ReflectionHacks.setPrivate(m, m.getClass(), "forgeTimes", forgeTimes);
				ReflectionHacks.setPrivate(m, m.getClass(), "thresholdReached", champThresholdReached);
				break;

			case "BookOfStabbing":
				ReflectionHacks.setPrivate(m, m.getClass(), "stabCount", StabbyMcStabs);
				break;

			case "TheCollector":
				int turnsTaken = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnsTaken");
				ReflectionHacks.setPrivate(m, m.getClass(), "turnsTaken", turnsTaken - 1);
				break;

      		case "Lagavulin":
      			switch (m.nextMove) {
      				case 5: // sleep
						int idleCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "idleCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "idleCount", idleCount - 1);
						break;
					case 1:
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", 2);
						break;
					case 3:
						int debuffTurnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "debuffTurnCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", debuffTurnCount - 1);
						break;
				} 
            	break;
			case "Chosen":
				if (m.nextMove == 4) {
					ReflectionHacks.setPrivate(m, m.getClass(), "usedHex", false); }
				break;
			case "SphericGuardian":
				if (m.nextMove == 4) {
					ReflectionHacks.setPrivate(m, m.getClass(), "secondMove", true); }
				break;
			case "Maw":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "turnCount", turnCount - 1);
				break;
			case "Hexaghost":
      			Hexaghost hexa = (Hexaghost)m;
				int orbActiveCount = (int)ReflectionHacks.getPrivate(hexa, Hexaghost.class, "orbActiveCount");
				if (orbActiveCount == 0) {
					ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", 6);
				} else {
					ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", orbActiveCount-1);
				}
				break;

			case "CorruptHeart":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
				break;

			case "SpireShield":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
				break;
			case "SpireSpear":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "moveCount");
				ReflectionHacks.setPrivate(m, m.getClass(), "moveCount", turnCount - 1);
				break;
            }

        return true;
	}

	public static boolean previewNextIntent(AbstractMonster m) {
		int count;
		int turnCount;
		int slashCount;
		boolean isAttacking;

		// Special Cases
		switch(m.id) {

			case "AwakenedOne":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
				break;

			case "Dagger":
				if (m.nextMove == 2) {
					m.tint.changeColor(Color.CLEAR.cpy(), 0.6F);
  					m.setMove((byte)0, AbstractMonster.Intent.NONE);
  					m.createIntent();
  					return true;
      			}
      			break;

      		// Stupid champ and his junk
			case "Champ":
				champThresholdReached = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "thresholdReached");
				champNumTurns = (int)ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
				forgeTimes = (int)ReflectionHacks.getPrivate(m, m.getClass(), "forgeTimes");
				break;

			// Donu and deca need to swap
			case "Deca":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;
			case "Donu":
				isAttacking = (boolean)ReflectionHacks.getPrivate(m, m.getClass(), "isAttacking");
				ReflectionHacks.setPrivate(m, m.getClass(), "isAttacking", !isAttacking);
				break;

			// Exploder advances his explosion timer
			case "Exploder":
				turnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnCount") + 1;
				ReflectionHacks.setPrivate(m, m.getClass(), "turnCount", turnCount);
				if (turnCount == 3) {
					m.tint.changeColor(Color.RED.cpy(), 0.6F);
					AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
					m.setMove((byte)0, AbstractMonster.Intent.NONE);
					m.createIntent();
					return true;
     			}
				break;

			// You can skip Time Eater hasting itself =D
			case "TimeEater":
				if (m.currentHealth < m.maxHealth / 2) { ReflectionHacks.setPrivate(m, m.getClass(), "usedHaste", true); }
				break;

			// Transient's count increases
			case "Transient":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "count");
				ReflectionHacks.setPrivate(m, m.getClass(), "count", count + 1);

				if (m.hasPower("Fading")) {
					if (m.getPower("Fading").amount == 1) {
				        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
						m.tint.changeColor(Color.CLEAR.cpy(), 0.6F);
						m.setMove((byte)0, AbstractMonster.Intent.NONE);
						m.createIntent();
						return true;
				    }
   				}
				break;

			// There's no avoiding Hyper Beam charging... but you can avoid Hyper Beam....?!
			case "BronzeAutomaton":
				count = (int)ReflectionHacks.getPrivate(m, m.getClass(), "numTurns");
				if (count >= 4) { count = 0; }
				ReflectionHacks.setPrivate(m, m.getClass(), "numTurns", count);
				break;

			// Looter is annoying, just like Mugger
			case "Looter":
			case "Mugger":
				switch (m.nextMove) {
					case 1:
						slashCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "slashCount");
						m.rollMove();

						if (slashCount == 1) {
							m.setMove((byte)4, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						}
						break;
					case 4:
						m.setMove((byte)2, AbstractMonster.Intent.DEFEND);
						break;
					case 2:
						m.setMove((byte)3, AbstractMonster.Intent.ESCAPE);
						break;
					case 3:
						m.tint.changeColor(Color.CLEAR.cpy(), 0.6F);
						m.setMove((byte)0, AbstractMonster.Intent.NONE);
						break;
				}

				m.createIntent();
				return true;

			// Book of Stabbing has some new stuff after A18 that we need to save and restore.
			case "BookOfStabbing":
				StabbyMcStabs = (int)ReflectionHacks.getPrivate(m, m.getClass(), "stabCount");
				break;

			// Bandits are all dumb and hardcoded now.
			case "BanditBear":
			    switch (m.nextMove)
			    {
			    case 2: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 1: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 3: 
			      m.setMove((byte)1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(0)).base);
			      break;
			    }
				m.createIntent();
				return true;

   			case "BanditLeader":
			    switch (m.nextMove)
			    {
			    case 2: 
			      m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)m.damage.get(1)).base);
			      break;
			    case 1: 
			      m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
			      break;
			    case 3: 
      			  if (AbstractDungeon.ascensionLevel >= 17) {
      			    m.setMove((byte)1, AbstractMonster.Intent.ATTACK_DEFEND, ((DamageInfo)m.damage.get(0)).base);
				  } else {
			        m.setMove((byte)3, AbstractMonster.Intent.ATTACK_DEBUFF, ((DamageInfo)m.damage.get(1)).base);
				  }
			      break;
			    }
				m.createIntent();
				return true;

			// The Collector has all sorts of stuff, but you can skip his ult
			case "TheCollector":
				int turnsTaken = (int)ReflectionHacks.getPrivate(m, m.getClass(), "turnsTaken") + 1;
				ReflectionHacks.setPrivate(m, m.getClass(), "turnsTaken", turnsTaken);
				ReflectionHacks.setPrivate(m, m.getClass(), "initialSpawn", false);
				if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
					ReflectionHacks.setPrivate(m, m.getClass(), "ultUsed", true);
				}
				break;

			// Writhing Mass test code?
			case "WrithingMass":
				if (m.intent == AbstractMonster.Intent.STRONG_DEBUFF) {
					ReflectionHacks.setPrivate(m, m.getClass(), "usedMegaDebuff", true);
				}
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "AcidSlime_L":
				ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
				break;

			// Gremlin Wizard charges more or goes back to no charge
			case "GremlinWizard":
				int currentCharge = (int)ReflectionHacks.getPrivate(m, m.getClass(), "currentCharge");

				if (currentCharge >= 3) {
					m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
				} 

				else if ((m.intent == AbstractMonster.Intent.ATTACK)) {
					if (AbstractDungeon.ascensionLevel >= 17) {
						m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
     				} else {
						m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
					}
				} else {
					m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
				}
				m.createIntent();
				return true;

			// Hexaghost, ouch
			case "Hexaghost":
      			ChronoMod.log("Move byte: " + Integer.toString(m.nextMove));
      			Hexaghost hexa = (Hexaghost)m;

				// If you skip the first turn, you need to activate him anyway
				if (m.intent == AbstractMonster.Intent.UNKNOWN) {
      				int d = AbstractDungeon.player.currentHealth / 12 + 1;
      
      				((DamageInfo)m.damage.get(2)).base = d;
      
      				m.applyPowers();
					m.setMove((byte)1, AbstractMonster.Intent.ATTACK, d, 6, true);
					m.createIntent();
					return true;
      			} else {
					int orbActiveCount = (int)ReflectionHacks.getPrivate(hexa, Hexaghost.class, "orbActiveCount");
					if (orbActiveCount == 6) {
						ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", 0);
					} else {
						ReflectionHacks.setPrivate(hexa, Hexaghost.class, "orbActiveCount", orbActiveCount+1);
					}
      			}
      			break;

      		// Lagavulin wakes up faster, but is otherwise the same
      		case "Lagavulin":
      			switch (m.nextMove) {
      				case 5: // sleep
						int idleCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "idleCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "idleCount", idleCount + 1);
						if (idleCount + 1 >= 3)
						{
							m.setMove((byte)3, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
							m.createIntent();
							return true;
						}
						break;
					case 1:
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", 0);
						break;
					case 3:
						int debuffTurnCount = (int)ReflectionHacks.getPrivate(m, m.getClass(), "debuffTurnCount");
						ReflectionHacks.setPrivate(m, m.getClass(), "debuffTurnCount", debuffTurnCount + 1);
						break;
            	}
            	break;

			// Slaver should only try to use entangle once
			case "SlaverRed":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstMove", false);
				if (m.nextMove == 2) { ReflectionHacks.setPrivate(m, m.getClass(), "usedEntangle", true); 
				entangleReset = true; }
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "SlimeBoss":
				ReflectionHacks.setPrivate(m, m.getClass(), "firstTurn", false);
				switch (m.nextMove) {
					case 4:
						m.setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
						break;
					case 2:
						m.setMove((byte)1, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						break;
					case 1:
						m.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
					case 3:
						m.setMove((byte)4, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
				}
				break;

			// Slimes get split triggered reset, so they will do another intent until you hit them again
			case "SpikeSlime_L":
				ReflectionHacks.setPrivate(m, m.getClass(), "splitTriggered", false);
				break;

			case "Maw":
				ReflectionHacks.setPrivate(m, m.getClass(), "roared", true);
				break;

			// Special case guardian stuff
			case "TheGuardian":
				switch (m.nextMove) {
					case 1:
						m.setMove((byte)3, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(1)).base);
						break;
					case 2:
						m.setMove((byte)7, AbstractMonster.Intent.STRONG_DEBUFF);
						break;
					case 3:
						m.setMove((byte)4, AbstractMonster.Intent.ATTACK_BUFF, (int)ReflectionHacks.getPrivate(m, m.getClass(), "twinSlamDamage"), 2, true);
						break;
					case 4:
						m.setMove((byte)5, AbstractMonster.Intent.ATTACK, (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
						break;
					case 5:
						m.setMove((byte)6, AbstractMonster.Intent.DEFEND);
						break;
					case 6:
						m.setMove((byte)2, AbstractMonster.Intent.ATTACK, ((DamageInfo)m.damage.get(0)).base);
						break;
					case 7:
						m.setMove((byte)5, AbstractMonster.Intent.ATTACK, (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindDamage"), (int)ReflectionHacks.getPrivate(m, m.getClass(), "whirlwindCount"), true);
						break;
				}
				m.createIntent();
				return true;
		}

		// try {
		// 	Method method = m.getClass().getDeclaredMethod("getMove", int.class);
		// 	method.setAccessible(true);
		// 	method.invoke(m, seed); 
		// }
		// catch (Throwable e) {
    	//		ChronoMod.log(e.toString());
 		// 	}
		m.rollMove();
		m.createIntent();

		return true;
	}
}