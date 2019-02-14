import input_interface
import sys

__all__ = ["InputCon"]

class InputCon(input_interface.InputInterface):
     def get_move(self, max_idx):
        move = sys.stdin.readline().strip()
        try:
            move = int(move)
        except ValueError:
            return False

        if move < 0 or move > max_idx:
            return False

        return move    

