import socket
import struct

def initServer(port):
    sock = socket.socket()
    sock.bind(('', port))
    return sock

def initClient(ip, port):
    sock = socket.socket()
    sock.connect((ip, port))
    return sock

def send(sock, data):
    message = struct.pack('>I', len(data)) + data.encode()    
    sock.send(message)

def recieve(sock):
    size = struct.unpack('>I', sock.recv(4))[0]
    data = sock.recv(size).decode()
    if len(data) == 0:
        return None
    return data