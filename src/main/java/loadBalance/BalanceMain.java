package loadBalance;

public class BalanceMain {
    public static void main(String[] args) {
        run();
    }
    public static void run(){
        loadBalance();
    }

    public static void loadBalance(){
        doGetServer(new RoundRobin());
    }

    public static void doGetServer(LoadBalance loadBalance){
        doGetServer(loadBalance, 100);
    }

    private static void doGetServer(LoadBalance loadBalance, int times){
        for (int i = 0; i < times; i++){
            String serverId = loadBalance.getServer(String.valueOf(i));
            System.out.println(String.format("[%s] index: %s, %s",
                    loadBalance.getClass().getSimpleName(), i, serverId));
        }
    }
}
