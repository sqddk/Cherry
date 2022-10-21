package cn.hdudragonking.cherry;


import cn.hdudragonking.cherry.bootstrap.remote.client.CherryClient;
import cn.hdudragonking.cherry.bootstrap.remote.client.Receiver;
import cn.hdudragonking.cherry.engine.base.TimePoint;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CherryClient client = CherryClient.getInstance();
        client.initial("127.0.0.1", 3000, new Receiver() {
            @Override
            public void receiveNotify(TimePoint timePoint, String metaData, String taskID) {
                System.out.println(timePoint.toString() + ", " + metaData + ", " + taskID);
            }

            @Override
            public void receiveError(String errorMessage) {
                System.out.println(errorMessage);
            }

            @Override
            public void receiveAddResult(String metaData, String taskID, boolean result) {
                System.out.println(metaData + ", " + taskID + ", " + result);
            }

            @Override
            public void receiveRemoveResult(String taskID, boolean result) {
                System.out.println(taskID + ", " + result);
            }
        });
        Thread.sleep(500);
        TimePoint timePoint = TimePoint.parse("202210211640");
        client.submit(timePoint, "ddd");
    }

}
