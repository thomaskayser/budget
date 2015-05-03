package ch.tkayser.budget.swing.service;

import ch.tkayser.budget.service.BudgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tom on 03.05.15.
 */
public class ServiceWatchdog {

    private Thread checkServerThread;
    private static final int pollIntervall = 60*1000;

    private Logger logger = LoggerFactory.getLogger("Watchdog");

    public ServiceWatchdog(final BudgetService service){
        checkServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Boolean isAlive = service.isAlive();
                        logger.info("is Alive: {}", isAlive);
                        Thread.sleep(pollIntervall);
                    }
                } catch (Exception e) {
                    logger.error("Error polling server: ", e);
                }
            }
        });

        checkServerThread.start();

    }
}
