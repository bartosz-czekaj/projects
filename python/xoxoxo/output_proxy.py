import sys
from output_con import OutputCon
__all__ = ["OtputProxy"]

class OtputProxy():
    def __init__(self):
        self._outputs = []

    def register_output(self, output):
        self._outputs.append(output)

    def show_welcome(self):
        self._funner()
    def show_board(self, game_board, row_cols):
        self._funner(game_board, row_cols)    
    def show_player_turn(self, player_name):
        self._funner(player_name)
    def show_move_error(self, player_name):
        self._funner(player_name)
    def show_move_error_game_continue(self, player_name):    
        self._funner(player_name)
    def show_winner(self, player_name):
        self._funner(player_name)
    def show_draw(self):
        self._funner()

    def _funner(self, *args):
        _currentFuncName = lambda n=0: sys._getframe(n + 1).f_code.co_name
        parent = _currentFuncName(1)

        for outputs in self._outputs:
            getattr(outputs, parent)(*args)
