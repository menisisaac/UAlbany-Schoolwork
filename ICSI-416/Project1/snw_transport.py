import socket
import struct

def initServer(port):
    sock = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
    sock.bind(('', port))
    return sock

def send(sock, data):
    message = data.encode()
    size = len(message)
    size = 'LEN:' + str(size)
    sock.send(size.encode())
    ack = sock.recv(1024)
    if ack != 'ACK':
        sock.send(size.encode())
    index = 0
    while index < size:    
        sock.send()




    sock.send(message)

def recieve(sock):
    size = struct.unpack('>I', sock.recv(4))[0]
    data = sock.recv(size).decode()
    if len(data) == 0:
        return None
    return data