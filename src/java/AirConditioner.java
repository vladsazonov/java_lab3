class AirConditioner extends TemperatureChanger {

    public String refrigerantType;
    public String pipelineHeight;
    public String airflowRate;

    @Override
    public String toString() {
        return getId() + ";" +
                getName() + ";" +
                getPrice() + ";" +
                getColor() + ";" +
                getType() + ";" +
                getRefrigerantType() + ";" +
                getPipelineHeight() + ";" +
                getAirflowRate() +
                System.lineSeparator();
    }

    public AirConditioner(int id, String name, String price, String color, String type, String refrigerantType, String pipelineHeight, String airflowRate) {
        super(id, name, price, color, type);
        this.refrigerantType = refrigerantType;
        this.pipelineHeight = pipelineHeight;
        this.airflowRate = airflowRate;
    }

    public String getRefrigerantType() {
        return refrigerantType;
    }

    public String getPipelineHeight() {
        return pipelineHeight;
    }

    public String getAirflowRate() {
        return airflowRate;
    }
}