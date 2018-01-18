package cn.woodyjc.android.netflow;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 2016/8/14.
 */
public abstract class NetFlow {
    protected List<FlowNode> flowNodes = new ArrayList<>();
    protected int COUNT = 2;

    protected double getSpeed() {
        if (flowNodes.size() > COUNT) {
            flowNodes.remove(0);
        }
        // add data
        FlowNode node = new FlowNode();
        node.time = System.currentTimeMillis();
        node.sum = getSum();
        flowNodes.add(node);

        // calc data
        if (flowNodes.size() > COUNT) {
            FlowNode firstNode = flowNodes.get(0);
            FlowNode lastNode = flowNodes.get(flowNodes.size() - 1);
            long deltaTime = lastNode.time - firstNode.time;
            long deltaNum = lastNode.sum - firstNode.sum;
            double speed = (double) deltaNum / deltaTime * 1000;
            Log.d("netspeed", this.getClass().getSimpleName() + "-deltaTime:" + deltaTime + ", deltaNum:" + deltaNum + "---speed--" + speed);
            return speed;
        }
        return 0;
    }

    public String getSpeedWithUnit() {
        double d = getSpeed();
        return ByteUtil.formatLengthWithUnit(d) + "/S";
    }

    protected long getSum(){
        return 0;
    }

    class FlowNode {
        public long sum;
        public long time;
    }

}
