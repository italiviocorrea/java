package eti.italiviocorrea.kafka.consumer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NF3eTempoMedioAcumulado {

    public int serviceId;
    public String serviceName;
    public Integer min = 0;
    public Integer max = 0;
    public Integer count = 0;
    public Integer sum = 0;
    public Integer avg = 0;

    public NF3eTempoMedioAcumulado updateFrom(NF3eTempoMedio averageTime) {
        this.serviceId = averageTime.getId();
        this.serviceName = averageTime.getServiceName();

        this.count++;
        this.sum += averageTime.getTempoMedio();
        this.avg = BigDecimal.valueOf(this.sum / this.count).setScale(1, RoundingMode.HALF_UP).intValue();

        this.min = Math.min(this.min, averageTime.getTempoMedio());
        this.max = Math.max(this.max, averageTime.getTempoMedio());

        return this;
    }

    @Override
    public String toString() {
        return "NF3eTempoMedioAcumulado{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", count=" + count +
                ", sum=" + sum +
                ", avg=" + avg +
                '}';
    }
}
