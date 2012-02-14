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

    collect_metric("meaning.of.life", 42, now())
