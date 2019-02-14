
import output_interface
__all__ = ["OutputCon"]

class OutputCon(output_interface.OutputInterface):
    def show_welcome(self):
        print("Welcome to XOX\n")
        
    def show_board(self, game_board, row_cols):
        row_maker = lambda sign, elems : ("%s|" % sign) * elems + ("%s\n" % sign)

        in_row = row_cols-1    
        board_elem = ""    
        while row_cols:
            board_elem += row_maker(" %c ", in_row)
            if row_cols > 1: 
                board_elem += row_maker("---", in_row)
            row_cols -= 1

        print(board_elem % tuple(game_board))

    def show_player_turn(self, player_name):
        print("Now moves '%s'" % player_name)
        
    def show_move_error(self, player_name):
        print("Plz repeat the move '%s'" % player_name)

    def show_move_error_game_continue(self, player_name):    
        print("Plz '%s' repeat the move, to continue the game" % player_name)    

    def show_winner(self, player_name):
        print("CONGRATZ you win '%s'" % player_name)
        
    def show_draw(self):
        print("It's a draw nobody wins")
