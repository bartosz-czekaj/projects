__all__ = ["OutputInterface"]

class OutputInterface():
    def show_welcome(self):
        pass
    def show_board(self, game_board, row_cols):
        pass    
    def show_player_turn(self, player_name):
        pass
    def show_move_error(self, player_name):
        pass
    def show_move_error_game_continue(self, player_name):    
        pass
    def show_winner(self, player_name):
        pass
    def show_draw(self):
        pass