package ss.projectt10.model;

import java.io.Serializable;

public class Card implements Serializable {
    private String codeId;
    private String nameCard;
    private String category;
    private String codeType;
    private String imageCard;

    public Card() {
    }

    public Card(String codeId, String nameCard, String category, String codeType, String imageCard) {
        this.codeId = codeId;
        this.nameCard = nameCard;
        this.category = category;
        this.codeType = codeType;
        this.imageCard = imageCard;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getImageCard() {
        return imageCard;
    }

    public void setImageCard(String imageCard) {
        this.imageCard = imageCard;
    }
}
