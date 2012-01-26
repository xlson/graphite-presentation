class FeedMe {
    public static void main(args) {
        new FeedMe().run(60, true)
    }
    
    def initializeMetricCreators() {
        ['monster.queue': createAllTimeQueue()]
    }

    def createAllTimeQueue(long startValue = 900000, int increment = 20) {
        def rnd = new Random()
        long total = startValue
        return {->
            total += rnd.nextInt(increment)
        }
    }

    def run(int minutesBack, boolean keepGoing) {
        def metrics = initializeMetricCreators()
        for(i in 0..(-1 * minutesBack)) {
            sendMetrics(metrics, i)
        }
        if(keepGoing) {
            while(42) {
                sleep(60 * 1000)
                sendMetrics(metrics, 0)
            }
        }
    }

    private sendMetrics(LinkedHashMap<String, Closure<Number>> metrics, int i) {
        metrics.each { metric, creator ->
            sendMetric(metric, creator(), now(i))
        }
    }

    def sendMetric(String metric, def value, def time) {
        new Socket('127.0.0.1', 2003).getOutputStream().withWriter {
            it << "$metric $value $time\n"
        }
    }

    def now(int timeBack = 0) {
        System.currentTimeMillis() - (timeBack * 1000 * 60)
    }
}