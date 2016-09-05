package cn.cj.android.netflow;

import android.net.TrafficStats;

/**
 * Created by June on 2016/8/14.
 */
public class NetUpFlow extends NetFlow {

    @Override
    protected long getSum() {
        return TrafficStats.getTotalTxBytes();
    }
}
