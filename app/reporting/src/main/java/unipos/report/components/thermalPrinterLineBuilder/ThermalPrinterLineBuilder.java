package unipos.report.components.thermalPrinterLineBuilder;

import lombok.extern.slf4j.Slf4j;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11.01.2016.
 */
@Slf4j
public class ThermalPrinterLineBuilder {

    //final byte[] DOUBLE_MODE_DEACTIVATE = {(byte) 27, (byte) 33, (byte) 0};

    private int lineLength;
    private List<Column> columns;
    private List<ThermalPrinterLineFormat> formats;

    public ThermalPrinterLineBuilder(int lineLength) {
        this.lineLength = lineLength;
        this.formats = new ArrayList<>();
        columns = new ArrayList<>();
        columns.add(new Column(0, 1));
    }

    public List<ThermalPrinterLineFormat> getFormats() {
        return formats;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public void setContent(String content) {
        if (columns.size() == 1) {
            columns.get(0).setContent(content);
        } else {
            System.err.println("More than 1 column added. Use setColumnContent instead");
        }
    }

    public void setWeight(int index, int weight) {
        columns.stream().filter(column -> column.getIndex() == index).forEach(column -> {
            column.setWeight(weight);
        });
    }

    public void addColumn(int index, int weight) {
        if (columns.stream().filter(column -> column.getIndex() == index).count() == 0) {
            columns.add(new Column(index, weight));
        } else {
            System.err.println("Index already taken");
        }
    }

    public void setAlignment(int columnIndex, Alignment alignment) {

        if (columns.stream().filter(column -> column.getIndex() == columnIndex).count() == 1) {
            columns.stream().filter(column -> column.getIndex() == columnIndex).forEach(column -> {
                column.setAlignment(alignment);
            });
        } else {
            System.err.println("Occurrence of Index greater than 1");
        }
    }

    public void setColumnContent(int index, String content) {
        if (columns.stream().filter(column -> column.getIndex() == index).count() == 1) {
            columns.stream().filter(column -> column.getIndex() == index).forEach(column -> {
                column.setContent(content);
            });
        } else {
            System.err.println("Occurrence of Index greater than 1");
        }
    }

    public void addFormat(ThermalPrinterLineFormat format) {
        this.formats.add(format);
    }

    public ThermalPrinterLine getLine() {
        if (formats != null && ((formats.contains(ThermalPrinterLineFormat.DOUBLE_HEIGHT) && formats.contains(ThermalPrinterLineFormat.DOUBLE_WIDTH)) || formats.contains(ThermalPrinterLineFormat.DOUBLE_WIDTH))) {
            lineLength /= 2;
        }

        ThermalPrinterLine thermalPrinterLine = new ThermalPrinterLine();
        String line = "";
        int sumWeight = 0;
        int columnLineLength = 0;
        for (Column column : columns) {
            sumWeight += column.getWeight();
        }

        for (Column column : columns) {
            columnLineLength = (lineLength / sumWeight) * column.getWeight();
            if (column.getContent().length() <= columnLineLength) {
                if (column.getAlignment() == Alignment.CENTER) {
                    int emptySpace = columnLineLength - column.getContent().length();
                    boolean odd = false;
                    if (emptySpace % 2 != 0) {
                        odd = true;
                    }
                    emptySpace /= 2;
                    for (int i = 0; i < emptySpace; i++) {
                        line += " ";
                    }
                    line += column.getContent();
                    if (odd) {
                        emptySpace++;
                    }
                    for (int i = 0; i < emptySpace; i++) {
                        line += " ";
                    }
                }
                if (column.getAlignment() == Alignment.LEFT) {
                    line += column.getContent();
                    for (int i = 0; i < columnLineLength - column.getContent().length(); i++) {
                        line += " ";
                    }
                }
                if (column.getAlignment() == Alignment.RIGHT) {
                    int emptySpace = columnLineLength - column.getContent().length();
                    for (int i = 0; i < emptySpace; i++) {
                        line += " ";
                    }
                    line += column.getContent();
                }
            } else {
                log.error("Line too long to print!");
                return null;
            }
        }
        if (formats != null && ((formats.contains(ThermalPrinterLineFormat.DOUBLE_HEIGHT) && formats.contains(ThermalPrinterLineFormat.DOUBLE_WIDTH)) || formats.contains(ThermalPrinterLineFormat.DOUBLE_WIDTH))) {
            lineLength *= 2;
        }

        if (line.length() <= lineLength) {
            thermalPrinterLine.setLine(line);
            if (thermalPrinterLine.getFormats() == null) {
                thermalPrinterLine.setFormats(new ArrayList<>());
            }
            thermalPrinterLine.getFormats().addAll(formats);
            return thermalPrinterLine;
        } else {
            log.error("Line too long to print");
            return null;
        }
    }

    public void clearColumns() {
        columns = new ArrayList<>();
        columns.add(new Column(0, 1));
    }
}
