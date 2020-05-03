package ss.projectt10.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Card implements Serializable {
    private String cardCode;
    private String cardName;
    private String cardFrontImage;
    private String cardBackImage;
    private String codeType;


    private String category;
    private String cardAvatar;
    private String note;

    private boolean isFavorite;
    public Card() {
    }

    public Card(String cardCode, String cardName, String cardFrontImage, String cardBackImage, String codeType, String category, String cardAvatar, String note, boolean isFavorite) {
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.cardFrontImage = cardFrontImage;
        this.cardBackImage = cardBackImage;
        this.codeType = codeType;
        this.category = category;
        this.cardAvatar = cardAvatar;
        this.note = note;
        this.isFavorite = isFavorite;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardFrontImage() {
        return cardFrontImage;
    }

    public void setCardFrontImage(String cardFrontImage) {
        this.cardFrontImage = cardFrontImage;
    }

    public String getCardBackImage() {
        return cardBackImage;
    }

    public void setCardBackImage(String cardBackImage) {
        this.cardBackImage = cardBackImage;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getcardAvatar() {
        return cardAvatar;
    }

    public void setcardAvatar(String cardAvatar) {
        this.cardAvatar = cardAvatar;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cardCode", cardCode);
        result.put("cardName", cardName);
        result.put("cardFrontImage", cardFrontImage);
        result.put("cardBackImage", cardBackImage);
        result.put("codeType", codeType);
        result.put("isFavorite", isFavorite);
        result.put("category",category);
        result.put("cardAvatar",cardAvatar);
        result.put("note",note);
        return result;
    }
}
