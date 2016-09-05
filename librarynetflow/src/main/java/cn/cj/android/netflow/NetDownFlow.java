package cn.cj.android.netflow;

import android.net.TrafficStats;

/**
 * Created by June on 2016/8/14.
 */
public class NetDownFlow extends NetFlow {
    @Override
    protected long getSum() {
        return TrafficStats.getTotalRxBytes();
    }
}
