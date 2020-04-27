package ss.projectt10.model;

public class Category {
    private String nameCard;
    private String imageIcon;
    private String cardType;

    public Category() {
    }

    public Category(String nameCard, String imageIcon, String cardType) {
        this.nameCard = nameCard;
        this.imageIcon = imageIcon;
        this.cardType = cardType;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
