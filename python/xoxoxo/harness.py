from game import Game
from input_con import InputCon
from output_con import OutputCon
from output_proxy import OtputProxy
from input_output_tcp import InputOutputTcp
from network_utils import NetworkUtils
from random import shuffle

class Harness():
    def __init__ (self, inputs, output):
        self._game = Game()
        self._output = output
        names = self._game.get_players_name()
        self._inputs = {}
        for idx, name in enumerate(names):
            self._inputs[name] = inputs[idx]


    def start_game(self):
        self._output.show_welcome()
        while not self._game.game_finished():
            self._output.show_board(self._game.get_board(), self._game.max_index_in_row())

            player_name = self._game.get_next_turn_player_name()
            self._output.show_player_turn(player_name)

            while True:            
                move = self._inputs[player_name].get_move(self._game.max_index())
                if move is None:
                    self._output.show_move_error(player_name)
                    continue
                move_res = self._game.make_move(move-1)
                if self._game.bad_move(move_res):
                    self._output.show_move_error_game_continue(player_name)
                    continue

                break    

        self._output.show_board(self._game.get_board(), self._game.max_index_in_row())
        if self._game.is_winner():
            winner = self._game.get_winners_name()
            self._output.show_winner(winner)
        else:  
            self._output.show_draw()

def main():
    input_con1 = InputCon()
    #input_con2 = InputCon()

    output_con = OutputCon()
    input_output_tcp = InputOutputTcp(NetworkUtils.HOST, NetworkUtils.PORT)

    player_inputs = [input_con1, input_output_tcp]
    #shuffle(player_inputs)

    output_proxy = OtputProxy()
    output_proxy.register_output(output_con)
    output_proxy.register_output(input_output_tcp)
  
    h = Harness(player_inputs, output_proxy)
    h.start_game()
if __name__ == "__main__":
    main()