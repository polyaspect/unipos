package unipos.report.components.thermalPrinterLineBuilder;

/**
 * Created by Thomas on 11.01.2016.
 */
public class Column {

    private int index;
    private int weight;
    private Alignment alignment;
    public String content;

    public Column(int index, int weight) {
        this.index = index;
        this.weight = weight;
        this.alignment = Alignment.LEFT;
        this.content = null;
    }

    public int getIndex() {
        return index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
