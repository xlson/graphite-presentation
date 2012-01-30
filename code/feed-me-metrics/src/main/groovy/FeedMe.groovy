class FeedMe {

    private Socket socket
    private OutputStream out
    
    public static void main(args) {
      def serverIp = '127.0.0.1'
      if(args.size() == 1) { 
        serverIp = args[0]
      }
        new FeedMe(serverIp, 2003).run(60, true)
    }
    
    def FeedMe(String address, int port) {
        socket = new Socket(address, port)
        out = socket.outputStream
    }
    
    def initializeMetricCreators() {
        ['monster.darth.queue': createAllTimeQueue(),
         'logins.leia.successful': createMoving(100, 100),
         'logins.luke.successful': createMoving(100, 100),
         'logins.jabba.failed': createMoving(10, 50),
         'logins.obiwan.failed': createMoving(10, 50)]
    }

    def createAllTimeQueue(long startValue = 900000, int increment = 20) {
        long total = startValue
        return {->
            total += nextInt(increment)
        }
    }

    def createMoving(int base = 100, int moving = 100) {
        return {->
            base + nextInt(moving)
        }
    }

    private nextInt(value) {
        new Random().nextInt(value)
    }

    def run(int minutesBack, boolean keepGoing) {
        def metrics = initializeMetricCreators()
        for(i in minutesBack..0) {
            sendMetrics(metrics, i)
        }
        if(keepGoing) {
            while(42) {
                sleep(60 * 1000)
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

    long now(int timeBack = 0) {
      (System.currentTimeMillis() - (timeBack * 1000 * 60)) / 1000
    }
}
