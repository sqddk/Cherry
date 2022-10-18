package cn.hdudragonking.cherry;


import cn.hdudragonking.cherry.bootstrap.CherryLocalStarter;
import cn.hdudragonking.cherry.engine.base.TimePoint;
import cn.hdudragonking.cherry.engine.task.Task;

public class Main {

    public static void main(String[] args) {
        CherryLocalStarter localStarter = CherryLocalStarter.getInstance();
        localStarter.initial();
        for (int i = 57; i < 59; i++) {
            int finalI = i;
            for (int j = 0; j < 5; j++) {
                int finalJ = j;
                localStarter.submit(new Task() {
                    @Override
                    public TimePoint getTimePoint() {
                        return TimePoint.parse("2022101817" + finalI);
                    }

                    @Override
                    public void execute() {
                        System.out.println(finalI + ", " + finalJ);
                    }
                });
            }
        }
    }

}
