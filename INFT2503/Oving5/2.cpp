#include <iostream>
#include <memory>
#include <string>
#include <vector>

using namespace std;

class ChessBoard {
public:
  enum class Color { WHITE,
                     BLACK };

  class Piece {
  public:
    Piece(Color color) : color(color) {}
    virtual ~Piece() {}

    Color color;
    std::string color_string() const {
      if (color == Color::WHITE)
        return "white";
      else
        return "black";
    }

    /// Return color and type of the chess piece
    virtual std::string type() const = 0;

    /// Returns true if the given chess piece move is valid
    virtual bool valid_move(int from_x, int from_y, int to_x, int to_y) const = 0;

		virtual string text_desc() const = 0;
  };

  class King : public Piece {
    public:
		King(Color color_): Piece(color_) {};
		string type() const{
			string color_str = (color == Color::WHITE) ? "white" : "black";
			return color_str + " king";
		};

		bool valid_move(int from_x, int from_y, int to_x, int to_y) const{
			// if(from_x < 0 || from_y < 0 || to_x < 0 || to_y < 0) return false;
			// if(from_x > 7 || from_y > 7 || to_x > 7 || to_y > 7) return false;
			int absolute_x_change = abs(from_x - to_x);
			int absolute_y_change = abs(from_y - to_y);
			if((absolute_x_change == 1 && absolute_x_change < 2) || 
			(absolute_y_change == 1 && absolute_y_change < 2)) return true;

			return false;
		}

		string text_desc() const{
			return (color == Color::WHITE) ? "K" :  "K";
		};
  };

  class Knight : public Piece {
	 public:
	 	Knight(Color color_): Piece(color_) {};
		string type() const{
			string color_str = (color == Color::WHITE) ? "white" : "black";
			return color_str + " knight";
		}

		bool valid_move(int from_x, int from_y, int to_x, int to_y) const{
			// if(from_x < 0 || from_y < 0 || to_x < 0 || to_y < 0) return false;
			// if(from_x > 7 || from_y > 7 || to_x > 7 || to_y > 7) return false;
			int absolute_x_change = abs(from_x - to_x);
			int absolute_y_change = abs(from_y - to_y);
			if(absolute_x_change == 2 && absolute_y_change == 1) return true;
			if(absolute_y_change == 2 && absolute_x_change == 1) return true;

			return false;
		}

		string text_desc() const{
			return (color == Color::WHITE) ? "N" :  "N";
		};
  };

  ChessBoard() {
    // Initialize the squares stored in 8 columns and 8 rows:
    squares.resize(8);
    for (auto &square_column : squares)
      square_column.resize(8);
  }

  /// 8x8 squares occupied by 1 or 0 chess pieces
  vector<vector<unique_ptr<Piece>>> squares;

  /// Move a chess piece if it is a valid move.
  /// Does not test for check or checkmate.
  bool move_piece(const std::string &from, const std::string &to) {
    int from_x = from[0] - 'a';
    int from_y = stoi(string() + from[1]) - 1;
    int to_x = to[0] - 'a';
    int to_y = stoi(string() + to[1]) - 1;

    auto &piece_from = squares[from_x][from_y];
    if (piece_from) {
      if (piece_from->valid_move(from_x, from_y, to_x, to_y)) {
        cout << piece_from->type() << " is moving from " << from << " to " << to << endl;
        auto &piece_to = squares[to_x][to_y];
        if (piece_to) {
          if (piece_from->color != piece_to->color) {
            cout << piece_to->type() << " is being removed from " << to << endl;
            if (auto king = dynamic_cast<King *>(piece_to.get()))
              cout << king->color_string() << " lost the game" << endl;
          } else {
            // piece in the from square has the same color as the piece in the to square
            cout << "can not move " << piece_from->type() << " from " << from << " to " << to << endl;
            return false;
          }
        }
        piece_to = move(piece_from);
        return true;
      } else {
        cout << "can not move " << piece_from->type() << " from " << from << " to " << to << endl;
        return false;
      }
    } else {
      cout << "no piece at " << from << endl;
      return false;
    }
  }

  friend ostream &operator<<(ostream &os, const ChessBoard &chess_board){
		for(int row = 7; row >= 0; row--){
			os << to_string(row + 1) << " ";
			for(int col = 0; col < 8; col++){
				auto &piece = chess_board.squares[col][row];
				os << (piece ? piece->text_desc() + " " : "- ");
			}
			os << "\n";
		}
		os << "  a b c d e f g h";
		return os;
  }
};


int main() {
  ChessBoard board;

  board.squares[4][0] = make_unique<ChessBoard::King>(ChessBoard::Color::WHITE);
  board.squares[1][0] = make_unique<ChessBoard::Knight>(ChessBoard::Color::WHITE);
  board.squares[6][0] = make_unique<ChessBoard::Knight>(ChessBoard::Color::WHITE);

  board.squares[4][7] = make_unique<ChessBoard::King>(ChessBoard::Color::BLACK);
  board.squares[1][7] = make_unique<ChessBoard::Knight>(ChessBoard::Color::BLACK);
  board.squares[6][7] = make_unique<ChessBoard::Knight>(ChessBoard::Color::BLACK);

  cout << "Invalid moves:" << endl;
  board.move_piece("e3", "e2");
  board.move_piece("e1", "e3");
  board.move_piece("b1", "b2");
  cout << endl;


  cout << "A simulated game:" << endl;
	cout << board << endl;
  board.move_piece("e1", "e2");
	cout << board << endl;
  board.move_piece("g8", "h6");
	cout << board << endl;
  board.move_piece("b1", "c3");
	cout << board << endl;
  board.move_piece("h6", "g8");
	cout << board << endl;
  board.move_piece("c3", "d5");
	cout << board << endl;
  board.move_piece("g8", "h6");
	cout << board << endl;
  board.move_piece("d5", "f6");
	cout << board << endl;
  board.move_piece("h6", "g8");
	cout << board << endl;
  board.move_piece("f6", "e8");
	cout << board << endl;

  return 0;
}