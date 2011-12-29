!SLIDE bullets
# "getting the data in" #

.notes Graphite does not do it for you, but it is really easy

!SLIDE code smaller
# "Hello metric" #
(Python)
    @@@ python
    import time
    import socket
    def collect_metric(name, value, timestamp):
        sock = socket.socket()
        sock.connect( ("localhost", 2003) )
        sock.send("%s %d %d\n" % (name, value, timestamp))
        sock.close()

    def now():
        int(time.time())

    collect_metric("metric.name", 42, now())

!SLIDE code smaller
# "Hello metric" #
(Clojure)
    @@@ clojure
    (ns clj-client.core
      (import [java.net Socket]
              [java.io PrintWriter]))

    (defn now []
      (int (/ (System/currentTimeMillis) 1000)))

    (defn write-metric [name value timestamp]
      (with-open [socket (Socket. "localhost" 2003)
                  os (.getOutputStream socket)]
        (binding [*out* (PrintWriter. os)]
          (println name value timestamp))))

!SLIDE
[ demo: collection-hello-world ]

