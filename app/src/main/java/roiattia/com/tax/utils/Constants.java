package roiattia.com.tax.utils;

public class Constants {

    private Constants(){}

    public static final String EMPTY_STRING = "--";
    public static final String DOT = ".";

    // VATCalculator bundle
    public static final String AMOUNT = "amount";
    public static final String CALCULATED_AMOUNT = "calculated amount";
    public static final String VAT_AMOUNT = "vat amount";

    // Shared preferences
    public static final String KEY_VAT_RATE = "vat_rate";
    static final int DEFAULT_VAT_RATE = 17;
    static final String KEY_VERSION_UPDATE = "version_update";
    static final boolean DEFAULT_VERSION_UPDATE = true;

    public static final String CALCULATOR_FRAGMENT_TAG = "cft";

}
