package com.example.lmsbackend.service.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.LineStyleEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;

import java.awt.*;
import java.util.List;

public class DynamicReportBuilder {

    //The prefix used in defining the field name that is later used by the JasperFillManager
    public static final String COL_EXPR_PREFIX = "col";

    // The prefix used in defining the column header name that is later used by the JasperFillManager
    public static final String COL_HEADER_EXPR_PREFIX = "header";

    // The page width for a page in portrait mode with 10 pixel margins
    private final static int TOTAL_PAGE_WIDTH = 700;

    // The whitespace between columns in pixels
    private final static int SPACE_BETWEEN_COLS = 0;

    // The height in pixels of an element in a row and column
    private final static int COLUMN_HEIGHT = 12;

    public final static int HEADER_BAND_HEIGHT = 20;

    // The total height of the column header or detail band
    public final static int DETAIL_BAND_HEIGHT = 12;

    // The left and right margin in pixels
    private final static int MARGIN = 0;

    // The JasperDesign object is the internal representation of a report
    private JasperDesign jasperDesign;

    // The number of columns that are to be displayed
    private int numColumns;

    public DynamicReportBuilder(JasperDesign jasperDesign, int numColumns) {
        this.jasperDesign = jasperDesign;
        this.numColumns = numColumns;
    }

    public void addDynamicColumns() throws JRException {

        JRDesignBand detailBand = new JRDesignBand();
        JRDesignBand headerBand = new JRDesignBand();

        JRDesignStyle normalStyle = getNormalStyle();
        JRDesignStyle columnHeaderStyle = getColumnHeaderStyle();
        jasperDesign.addStyle(normalStyle);
        jasperDesign.addStyle(columnHeaderStyle);

        int xPos = MARGIN;
        int ordinalColumnWidth = 50;
        int columnWidth = (TOTAL_PAGE_WIDTH - ordinalColumnWidth) / (numColumns - 1);

        for (int i = 0; i < numColumns; i++) {

            // Create a Column Field
            JRDesignField field = new JRDesignField();
            field.setName(COL_EXPR_PREFIX + i);
            field.setValueClass(java.lang.String.class);
            jasperDesign.addField(field);

            // Create a Header Field
            JRDesignField headerField = new JRDesignField();
            headerField.setName(COL_HEADER_EXPR_PREFIX + i);
            headerField.setValueClass(java.lang.String.class);
            jasperDesign.addField(headerField);

            // Add a Header Field to the headerBand
            headerBand.setHeight(HEADER_BAND_HEIGHT);
            JRDesignTextField colHeaderField = new JRDesignTextField();
            colHeaderField.setX(xPos);
            colHeaderField.setY(0);
            colHeaderField.setHeight(HEADER_BAND_HEIGHT);

            colHeaderField.setMode(ModeEnum.OPAQUE);
            colHeaderField.setBackcolor(new Color(81, 76, 110));
            colHeaderField.setForecolor(Color.WHITE);
            // adapt this more general
            if (i == 0) {
                colHeaderField.setWidth(ordinalColumnWidth);
            } else {
                colHeaderField.setWidth(columnWidth);
            }
            if (i == 1) {
                colHeaderField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            } else {
                colHeaderField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            }
            colHeaderField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            colHeaderField.setStyle(columnHeaderStyle);

            JRDesignExpression headerExpression = new JRDesignExpression();
            headerExpression.setValueClass(java.lang.String.class);
            headerExpression.setText("$F{" + COL_HEADER_EXPR_PREFIX + i + "}");
            colHeaderField.setExpression(headerExpression);
            headerBand.addElement(colHeaderField);

            // Add text field to the detailBand
            detailBand.setHeight(DETAIL_BAND_HEIGHT);
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(xPos);
            textField.setY(0);
            textField.setHeight(COLUMN_HEIGHT);
            if (i == 1) {
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            } else {
                textField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            }
            textField.setStyle(normalStyle);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.lang.String.class);
            if (i == 0) {
                expression.setText("$V{REPORT_COUNT}");
                textField.setWidth(ordinalColumnWidth);
            } else {
                expression.setText("$F{" + COL_EXPR_PREFIX + i + "}");
                textField.setWidth(columnWidth);
            }
            textField.setExpression(expression);
            detailBand.addElement(textField);

            xPos = xPos + (i == 0 ? ordinalColumnWidth : columnWidth) + SPACE_BETWEEN_COLS;

            var box = textField.getLineBox();
            this.designTextBox(box);
        }

        jasperDesign.setColumnHeader(headerBand);
        ((JRDesignSection)jasperDesign.getDetailSection()).addBand(detailBand);
    }

    private JRDesignStyle getNormalStyle() {
        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Sans_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName("SansSerif");
        normalStyle.setFontSize(8F);
        normalStyle.setPdfFontName("Helvetica");
        normalStyle.setPdfEncoding("Cp1252");
        normalStyle.setPdfEmbedded(false);
        return normalStyle;
    }

    private JRDesignStyle getColumnHeaderStyle() {
        JRDesignStyle columnHeaderStyle = new JRDesignStyle();
        columnHeaderStyle.setName("Sans_Header");
        columnHeaderStyle.setDefault(false);
        columnHeaderStyle.setFontName("SansSerif");
        columnHeaderStyle.setFontSize(9F);
        columnHeaderStyle.setBold(true);
        columnHeaderStyle.setPdfFontName("Helvetica");
        columnHeaderStyle.setPdfEncoding("Cp1252");
        columnHeaderStyle.setPdfEmbedded(false);
        return columnHeaderStyle;
    }

    private void designTextBox(JRLineBox box) {
        box.getBottomPen().setLineStyle(LineStyleEnum.SOLID);
        box.getBottomPen().setLineWidth(1F);
        box.getBottomPen().setLineColor(new Color(235, 235, 235));

        box.getTopPen().setLineStyle(LineStyleEnum.SOLID);
        box.getTopPen().setLineWidth(1F);
        box.getTopPen().setLineColor(new Color(235, 235, 235));

        box.getLeftPen().setLineStyle(LineStyleEnum.SOLID);
        box.getLeftPen().setLineWidth(1F);
        box.getLeftPen().setLineColor(new Color(235, 235, 235));

        box.getRightPen().setLineStyle(LineStyleEnum.SOLID);
        box.getRightPen().setLineWidth(1F);
        box.getRightPen().setLineColor(new Color(235, 235, 235));
        box.setRightPadding(Integer.valueOf(3));
    }

}