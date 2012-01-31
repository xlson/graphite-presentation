from contextlib import contextmanager
import socket
import time

@contextmanager
def client_socket(addr):
    s = socket.socket()
    s.connect(addr)
    yield s
    s.close

def now():
    return int(time.time())

def write_metric(name, value, timestamp):
    with client_socket( ("localhost", 2003) ) as sock:
        sock.send("%s %d %d\n" % (name, value, timestamp) )
        
write_metric("meaning.of.life", 42, now())

