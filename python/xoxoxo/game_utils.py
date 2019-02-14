__all__ = ["GameUtils"]

class GameUtils():
    X = 'X'
    O = 'O'
    EMPTY = " "
    IN_ROW = 5

    @staticmethod
    def is_proper_pawn(what):
        return what in [GameUtils.X, GameUtils.O]

    @staticmethod
    def is_proper_index(where):
        return  0 <= where <= (GameUtils.IN_ROW**2 - 1)