package loadBalance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpPool {
    public static Map<String, Integer> ipMap = new ConcurrentHashMap<>();
    /*
    3.89.221.148:32000 → Math services
    3.92.25.81:35000 → Math services
    184.73.139.196 → Load balancer
    */
    static {
        ipMap.put("3.89.221.148", 32000);
        ipMap.put("3.92.25.81", 35000);
    }
}