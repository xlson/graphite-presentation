class FeedMe {

    private Socket socket
    private OutputStream out
    
    public static void main(args) {
        new FeedMe('127.0.0.1', 2003).run(60, true)
    }
    
    def FeedMe(String address, int port) {
        socket = new Socket(address, port)
        out = socket.outputStream
    }
    
    def initializeMetricCreators() {
        ['monster.queue': createAllTimeQueue(),
         'logins.successful': createMoving(100, 100),
         'logins.failed': createMoving(10, 50)]
    }

    def createAllTimeQueue(long startValue = 900000, int increment = 20) {
        long total = startValue
        return {->
            total += nextInt(increment)
        }
    }

    def createMoving(int base = 100, int moving = 100) {
        def rnd = new Random()
        return {->
            base + nextInt(moving)
        }
    }

    private nextInt(value) {
        new Random().nextInt(value)
    }

    def run(int minutesBack, boolean keepGoing) {
        def metrics = initializeMetricCreators()
        for(i in 0..(-1 * minutesBack)) {
            sendMetrics(metrics, i)
        }
        if(keepGoing) {
            while(42) {
                sleep(60000 * 1000)
                sendMetrics(metrics, 0)
            }
        }

        socket.close()
    }

    private sendMetrics(LinkedHashMap<String, Closure<Number>> metrics, int i) {
        metrics.each { metric, creator ->
            sendMetric(metric, creator(), now(i))
        }
    }

    def sendMetric(String metric, def value, def time) {
        out << "$metric $value $time\n"
    }

    def now(int timeBack = 0) {
        System.currentTimeMillis() - (timeBack * 1000 * 60)
    }
}