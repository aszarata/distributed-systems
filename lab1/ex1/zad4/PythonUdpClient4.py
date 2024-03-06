import socket;

serverIP = "127.0.0.1"
serverPort = 9008
msg = "Ping Python Udp!"
msg = bytes(msg, 'cp1250')


print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg, (serverIP, serverPort))

# potwierdzenie
client.settimeout(5)  
try:
    msg, addr = client.recvfrom(1024)
    print('received msg:', msg.decode('utf-8'))
except socket.timeout:
    print('timeout')


