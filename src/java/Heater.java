public class Heater extends TemperatureChanger {

    private String heatedArea;
    private String modesNumber;
    private String power;

    @Override
    public String toString() {
        return getId() + ";" +
                getName() + ";" +
                getPrice() + ";" +
                getColor() + ";" +
                getType() + ";" +
                getHeatedArea() + ";" +
                getModesNumber() + ";" +
                getPower() +
                System.lineSeparator();
    }

    public Heater(int id, String name, String price, String color, String type, String heatedArea, String modesNumber, String power) {
        super(id, name, price, color, type);
        this.heatedArea = heatedArea;
        this.modesNumber = modesNumber;
        this.power = power;
    }

    public String getHeatedArea() {
        return heatedArea;
    }

    public String getModesNumber() {
        return modesNumber;
    }

    public String getPower() {
        return power;
    }
}
