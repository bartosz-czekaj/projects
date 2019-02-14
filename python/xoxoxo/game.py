from enum import IntEnum
from math import pow
from game_utils import GameUtils
__all__ = ["Game", "GameState", "GameException"]

class GameState(IntEnum):
    DRAW = 1
    WIN = 2
    END = int(DRAW) | int(WIN)
    UNRESOLVED = 4
    BAD_MOVE = 8

class GameException(Exception):
    pass



class Player():
    def __init__(self, name, turn, pawn):
        self._player_name = name
        self._turn = turn
        self._pawn = pawn

    def get_name(self):
        return self._player_name    

    def my_turn(self):    
        return self._turn

    def switch_turn(self):    
        self._turn  = not self._turn

    def get_pawn(self):        
        return self._pawn

class Board():
    
    def __init__(self):
        self.board = [GameUtils.EMPTY for _  in range(int(pow(GameUtils.IN_ROW, 2)))]

    def get(self):
        return self.board[:]  # copy.copy(self.board)

    def put(self, where, what):
        assert GameUtils.is_proper_pawn(what)
        assert GameUtils.is_proper_index(where)
        if self.board[where] != GameUtils.EMPTY:
            return False
        self.board[where] = what
        return True

    def _collect_cols(self, index):
        row = []
        while index < GameUtils.IN_ROW**2: 
            row.append(self.board[index])
            index += GameUtils.IN_ROW
        return row    

    def _collect_diagonals(self, start_index, step):   
        diagonal = []
        steps = GameUtils.IN_ROW
        while steps > 0: 
            diagonal.append(self.board[start_index+ ((steps - 1) * step)])
            steps -= 1
            
        return diagonal    

    def get_states_for_significant_positions(self):
        slices = []
        index = 0
        max_index = GameUtils.IN_ROW**2

        while index < max_index:
            slices.append(self.board[index:index+GameUtils.IN_ROW])
            index += GameUtils.IN_ROW

        index = 0

        while index < GameUtils.IN_ROW:
            slices.append(self._collect_cols(index))
            index += 1

        slices.append(self._collect_diagonals(0,GameUtils.IN_ROW+1))
        slices.append(self._collect_diagonals(0,GameUtils.IN_ROW-1))

        return slices
    
    def move_available(self):
        return any(x == GameUtils.EMPTY for x in self.board)

    def max_index(self):
        return GameUtils.IN_ROW**2

    def max_index_in_row(self):
        return GameUtils.IN_ROW
    
    
class Game():

    _players = [Player('Alozjy', True, GameUtils.X), Player('2', False, GameUtils.O)]

    def __init__(self):
        self._board = Board()
        self._game_state = GameState.UNRESOLVED
        self._winner = None

    def max_index(self):
        return self._board.max_index()    

    def max_index_in_row(self):
        return self._board.max_index_in_row()        

    def get_game_state(self):
        return self._game_state

    def is_winner(self):
        return self._game_state == GameState.WIN

    def game_finished(self):
        return self._game_state & GameState.END

    def _do_change_player(self):
        if self.game_finished():
            return False

        return self._game_state == GameState.UNRESOLVED    

    def bad_move(self, game_state):
        #print(game_state)
        return game_state ==  GameState.BAD_MOVE
    
    def get_winner(self):    
        return self._winner

    def get_winners_name(self):    
        return self._winner.get_name()

    def get_next_turn_player(self):
        return next((x for x in self._players if x.my_turn()))

    def get_next_turn_player_name(self):
        return next((x for x in self._players if x.my_turn())).get_name()

    def get_players_name(self):    
        ret = []
        for player in self._players:
            ret.append(player.get_name())
        return ret    

    def is_my_turn(self, player_name):
        try:
            player = next((x for x in self._players if x.get_name() == player_name))
            return player.my_turn()
        except:
            raise GameException("Unknown player name'%s'" % player_name)

    def get_board(self):     
        return self._board.get()

    def _change_player(self):
        change_player = lambda player : player.switch_turn()
        [change_player(item) for item in self._players]

    def _check_winner(self):
        for sub_set in self._board.get_states_for_significant_positions():
            if sub_set[0] != GameUtils.EMPTY and len(set(sub_set)) == 1:
                return True
        return False        

    def make_move(self, where):
        if self.game_finished():
            #print("FINISZ")
            return GameState.END

        assert GameUtils.is_proper_index(where)

        player = self.get_next_turn_player()

        if not self._board.put(where, player.get_pawn()):
            self._game_state = GameState.BAD_MOVE
        elif self._check_winner():
            self._winner = player
            self._game_state = GameState.WIN
        elif not self._board.move_available():
            self._game_state = GameState.DRAW
        else:
            self._game_state = GameState.UNRESOLVED   

        if(self._do_change_player()):
            self._change_player()
            player = self.get_next_turn_player()

        return self._game_state


if __name__ == "__main__":
    game = Game()
    #print(game.get_board())
    game.make_move(0)#X
    game.make_move(1)#0
    #print(game.get_board())
    game.make_move(4)#X
    #print(game.get_board())
    game.make_move(2)#X
    game.make_move(8)
    print(game.get_board())
    
