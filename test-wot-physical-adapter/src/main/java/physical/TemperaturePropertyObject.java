package physical;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class TemperaturePropertyObject {
    public final double temperature;
    public final int hour;
    public final List<Boolean> alarms;

    public TemperaturePropertyObject(double temperature, int hour, List<Boolean> alarms) {
        this.temperature = temperature;
        this.hour = hour;
        this.alarms = alarms;
    }

    public double getTemperature() {
        return temperature;
    }
    public int getHour() {
        return hour;
    }
    public List<Boolean> getAlarms() {
        return alarms;
    }

}
