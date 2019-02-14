__all__ = ["InputOutputTcp"]
import socket
import time
import math
import sys
from input_interface import InputInterface
from output_interface import OutputInterface
from game import GameException

from enum import IntEnum
class Protocol(IntEnum):
    GET_MOVE_RQ = 1
    GET_MOVE_RS = 2
    SHOW_WELCOME = 3
    SHOW_BOARD = 4
    SHOW_ERROR = 6
    SHOW_ERROR_CONTINUE = 7
    SHOW_DRAW = 8
    SHOW_WINNER = 9
    PLAYER_TURN = 10

class InputOutputTcp(InputInterface, OutputInterface):
    def  __init__(self, bind_ip, bind_port):
        self._host = bind_ip
        self._port = bind_port
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._socket.bind((self._host, self._port))
        self._socket.listen(1)

        print("Waiting for connection on port: %s" % self._port)
        conn, addr = self._socket.accept()
        print("Connected %s:%s" % (conn, addr))

        self._conn = conn
        self._socket.close()

    def show_welcome(self):
        self._conn.sendall(self._packer(Protocol.SHOW_WELCOME) + bytes("|",'ascii')+ bytes("\n\r",'ascii'))

    def show_board(self, game_board, row_cols): #IN_ROW**2 * byte(x,o,' ')
        self._conn.sendall(self._packer(Protocol.SHOW_BOARD) + bytes("|",'ascii')+ self._packer(row_cols) + bytes("|",'ascii') + bytes(''.join(game_board), 'ascii') +bytes("|",'ascii')+ bytes("\n\r",'ascii'))

    def show_player_turn(self, player_name):#player name
        self._conn.sendall(self._packer(Protocol.PLAYER_TURN) + bytes("|",'ascii')+ bytes(player_name, 'utf-8') + bytes("|",'ascii') + bytes("\n\r",'ascii'))

    def show_move_error(self, player_name):
        self._conn.sendall(self._packer(Protocol.SHOW_ERROR) + bytes("|",'ascii')+ bytes(player_name, 'utf-8') + bytes("|",'ascii') + bytes("\n\r",'ascii'))

    def show_move_error_game_continue(self, player_name):    
        self._conn.sendall(self._packer(Protocol.SHOW_ERROR_CONTINUE) + bytes("|",'ascii')+ bytes(player_name, 'utf-8')+ bytes("|",'ascii') + bytes("\n\r",'ascii'))

    def show_winner(self, player_name):
        self._conn.sendall(self._packer(Protocol.SHOW_WINNER) + bytes("|",'ascii')+ bytes(player_name, 'utf-8')+ bytes("|",'ascii') + bytes("\n\r",'ascii'))

    def show_draw(self):
        self._conn.sendall(self._packer(Protocol.SHOW_DRAW+ bytes("|",'ascii')+ bytes("\n\r",'ascii')))

    def get_move(self, max_idx):
        self._conn.sendall(self._packer(Protocol.GET_MOVE_RQ)+ bytes("|",'ascii')+ bytes("\n\r",'ascii'))
        
        response = int.from_bytes(self._conn.recv(1), byteorder='big')    

        if(Protocol.GET_MOVE_RS != response):
             raise GameException("Unknown response'%i'" % response)

        nbr = ''
        while(True):
            current = self._conn.recv(1)
            if current == b'|':
                break        

            nbr += current.decode("utf-8")    

        move = int(nbr)  # recv b''
      
        if move < 0 or move > max_idx:
            return False   

        return move    

    def _packer(self, data):   
        return b'%d' % data 
