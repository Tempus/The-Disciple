package chronomuncher.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

public class Enum {

    @SpireEnum
    public static AbstractCard.CardColor CHRONO_GOLD;

    @SpireEnum
    public static AbstractPlayer.PlayerClass CHRONO_CLASS;

	@SpireEnum 
	public static AbstractCard.CardTags TEMPO_CARD;

	@SpireEnum 
	public static AbstractCard.CardTags SWITCH_CARD;

	@SpireEnum 
	public static AbstractCard.CardTags REPLICA_CARD;
    
} 