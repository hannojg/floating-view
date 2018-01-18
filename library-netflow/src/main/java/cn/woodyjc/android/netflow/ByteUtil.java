package cn.woodyjc.android.netflow;

import java.text.DecimalFormat;

/**
 * Created by June on 2016/8/14.
 */
public class ByteUtil {
    public static final int KB = 1024;
    public static final int MB = KB * 1024;
    public static final int GB = MB * 1024;

    /**
     * GB、MB、KB、B <br/>
     * 占用空间 用不同的单位表示
     *
     * @param bit
     * @return
     */
    public static String formatLengthWithUnit(double bit) {
        DecimalFormat format = new DecimalFormat("0.#");
        if (bit > GB) {
            double g = bit / GB;
            return format.format(g) + "G";

        } else if (bit > MB) {
            double m = bit / MB;
            return format.format(m) + "M";

        } else if (bit > KB) {
            double k = bit / KB;
            return format.format(k) + "K";

        } else {
            return format.format(bit) + "B";
        }
    }
}
