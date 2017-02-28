package Chess.Game;

import Chess.Exceptions.Unchecked.IllegalPieceException;
import Chess.Exceptions.Unchecked.IllegalSideException;
import Chess.Exceptions.Unchecked.IllegalSquareException;

import java.util.ArrayList;

/**
 * @author Thomas
 * @since 20/02/2017
 *
 * Project: ChessGame
 * Package: Chess.Game
 */

public class Piece
{
	private int pieceByte;
	private int positionByte;

	/**
	 * Constructor, takes a pieceByte and a 0x88 positionByte
	 * @param pieceByte a piece byte
	 * @param positionByte a (0x88) position byte
	 */
	public Piece (int pieceByte, int positionByte)
	{
		this.pieceByte = pieceByte;
		this.positionByte = positionByte;
	}

	/**
	 * Constructor, takes a pieceByte and a file and rank byte
	 * @param pieceByte a pieceByte
	 * @param file a file byte
	 * @param rank a rank byte
	 */
	public Piece (int pieceByte, int file, int rank)
	{
		this.pieceByte = pieceByte;
		this.positionByte = ChessBoard.get0x88Index(file, rank);
	}

	/**
	 * Constructor, takes a pieceString, localeByte and (0x88) positionByte
	 * The sting can be either in long, human-readable format or in short
	 * 1-character format
	 * @param pieceString can be either in long, human-readable format or in short 1-character format
	 * @param positionByte a 0x88 position byte
	 * @param localeByte a locale that goes with pieceString
	 */
	public Piece (String pieceString, int positionByte, int localeByte)
	{
		switch (pieceString.length())
		{
			case 1:
				this.pieceByte = PieceData.toNumFromShort(pieceString, localeByte);
				break;
			default:
				this.pieceByte = PieceData.toNumFromString(pieceString, localeByte);
				break;
		}

		this.positionByte = positionByte;
	}

	/**
	 * Constructor, takes a pieceString, localeByte and a file and rank byte.
	 * The sting can be either in long, human-readable format or in short
	 * 1-character format
	 * @param pieceString can be either in long, human-readable format or in short 1-character format
	 * @param file  piece file
	 * @param rank  piece rank
	 * @param localeByte locale that goes with pieceString
	 */
	public Piece (String pieceString, int file, int rank, int localeByte)
	{
		switch (pieceString.length())
		{
			case 1:
				this.pieceByte = PieceData.toNumFromShort(pieceString, localeByte);
				break;
			default:
				this.pieceByte = PieceData.toNumFromString(pieceString, localeByte);
				break;
		}

		this.positionByte = ChessBoard.get0x88Index(file,rank);
	}

	public int getPieceByte ()
	{
		return this.pieceByte;
	}

	public int getPositionByte ()
	{
		return this.positionByte;
	}

	/**
	 * The only factor taken into account is wheter or not the moves would land the piece off the board
	 * @return All moves that won't land the piece outside the board
	 */
	public ArrayList<Move> getAllPossibleMoves ()
	{
		int size = 0;
		int j;
		ArrayList<Move> moves;

		switch (this.pieceByte & (0x07))
		{
			case PieceData.PAWN_BYTE:
				if ((this.pieceByte & (PieceData.WHITE_MASK | PieceData.BLACK_MASK)) == PieceData.WHITE_MASK)
				{
					moves = new ArrayList <Move> (Movesets.PAWN_MOVE_WHITE.length);

					for (int i = 0; i < Movesets.PAWN_MOVE_WHITE.length; i++)
					{
						if (((this.positionByte + Movesets.PAWN_MOVE_WHITE[i]) & 0x88) == 0)
						{
							moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.PAWN_MOVE_WHITE[i]),  0x0));
						}
					}
				}
				else if ((this.pieceByte & (PieceData.WHITE_MASK | PieceData.BLACK_MASK)) == PieceData.BLACK_MASK)
				{
					moves = new ArrayList <Move> (Movesets.PAWN_MOVE_BLACK.length);

					for (int i = 0; i < Movesets.PAWN_MOVE_BLACK.length; i++)
					{
						if (((this.positionByte + Movesets.PAWN_MOVE_BLACK[i]) & 0x88) == 0)
						{
							moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.PAWN_MOVE_BLACK[i]),  0x0));
						}
					}
				}
				else
				{
					throw new IllegalSideException(Integer.toString((this.pieceByte & (PieceData.WHITE_MASK | PieceData.BLACK_MASK))) + " is not a valid side-byte.");
				}

				moves.trimToSize();

				return moves;

			case PieceData.ROOK_BYTE:
				moves = new ArrayList <Move> (Movesets.ROOK_MOVE.length);

				for (int i = 0; i < Movesets.ROOK_MOVE.length; i++)
				{
					if (((this.positionByte + Movesets.ROOK_MOVE[i]) & 0x88) == 0)
					{
						moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.ROOK_MOVE[i]),  0x0));
					}
				}

				moves.trimToSize();

				return moves;

			case PieceData.KNIGHT_BYTE:
				moves = new ArrayList <Move> (Movesets.KNIGHT_MOVE.length);

				for (int i = 0; i < Movesets.KNIGHT_MOVE.length; i++)
				{
					if (((this.positionByte + Movesets.KNIGHT_MOVE[i]) & 0x88) == 0)
					{
						moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.KNIGHT_MOVE[i]),  0x0));
					}
				}

				moves.trimToSize();

				return moves;

			case PieceData.BISHOP_BYTE:
				moves = new ArrayList <Move> (Movesets.BISHOP_MOVE.length);

				for (int i = 0; i < Movesets.BISHOP_MOVE.length; i++)
				{
					if (((this.positionByte + Movesets.BISHOP_MOVE[i]) & 0x88) == 0)
					{
						moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.BISHOP_MOVE[i]),  0x0));
					}
				}

				moves.trimToSize();

				return moves;

			case PieceData.QUEEN_BYTE:
				moves = new ArrayList <Move> (Movesets.QUEEN_MOVE.length);

				for (int i = 0; i < Movesets.QUEEN_MOVE.length; i++)
				{
					if (((this.positionByte + Movesets.QUEEN_MOVE[i]) & 0x88) == 0)
					{
						moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.QUEEN_MOVE[i]),  0x0));
					}
				}

				moves.trimToSize();

				return moves;

			case PieceData.KING_BYTE:
				moves = new ArrayList <Move> (Movesets.KING_MOVE.length);

				for (int i = 0; i < Movesets.KING_MOVE.length; i++)
				{
					if (((this.positionByte + Movesets.KING_MOVE[i]) & 0x88) == 0)
					{
						moves.add(new Move (this.positionByte,  (this.positionByte + Movesets.KING_MOVE[i]),  0x0));
					}
				}

				moves.trimToSize();

				return moves;


			default:
				throw new IllegalPieceException(Integer.toString(this.pieceByte) + " is not a valid piece-byte.");
		}
	}

	public int getColorByte ()
	{
		return  (this.pieceByte & (PieceData.WHITE_MASK | PieceData.BLACK_MASK));
	}

	public int getPieceWithoutColorByte ()
	{
		return  (this.pieceByte & ~(PieceData.WHITE_MASK | PieceData.BLACK_MASK));
	}

	@Override
	public String toString ()
	{
		int[] coords = ChessBoard.get2DCoord(this.positionByte);
		return PieceData.toStringFromNum( (this.pieceByte & 0x07), PieceData.EN_UK.LOCALE_BYTE) + " @ " + ((char) (coords[0] + ('A'-1))) + Integer.toString(coords[1]);
	}
}
