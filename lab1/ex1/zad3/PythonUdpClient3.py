import socket;

serverIP = "127.0.0.1"
serverPort = 9008
msg_bytes = (300).to_bytes(4, byteorder='big')


print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(msg_bytes, (serverIP, serverPort))

# potwierdzenie
client.settimeout(5)  
try:
    msg, addr = client.recvfrom(1024)
    print('received msg:', int.from_bytes(msg, byteorder='big'))
except socket.timeout:
    print('timeout')


