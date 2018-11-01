package roiattia.com.tax.utils;

import java.text.DecimalFormat;

public class FormatsUtil {

    private FormatsUtil(){}

    public static String getStringFormatFromDouble(double amount){
        DecimalFormat formatter = new DecimalFormat("#,###,###.###");
        return formatter.format(amount);
    }
}
