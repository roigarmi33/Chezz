package Chess.UI;

import Chess.Game.GameManager;
import Chess.Game.Move;
import Chess.Game.Piece;
import Chess.Game.PieceData;
import Chess.Utils.Parser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//Created by Astrid on 22/02/2017.

public class GamePanel extends JFrame implements ActionListener
{
	private GameManager gameManager;

	private JPanel panel;
	private Board board;
	private SidePanel sidePanel;

	public GamePanel()
	{
		 super("Chezz!");
		 this.gameManager = new GameManager();
		 this.gameManager.init();
		 panel = new JPanel();
		 board = new Board(this);
		 sidePanel = new SidePanel(this);

		 panel.setBackground(UIData.BACKGROUND_COLOR);

		 this.setSize(UIData.WINDOW_DIMENSION);
		 this.setResizable(false);
		 this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		panel.add(board);
		panel.add(sidePanel);

		this.add(panel);
		this.initBoard();

		this.setResizable(true);
		this.setVisible(true);


		Parser parser = new Parser();
		parser.moveFromString("E2-E4", gameManager);
	}

	/**
	 * Sets all the pieces in the right place at the beginning of the game
	 */
	private void initBoard()
	{
		ArrayList<Piece> pieces;
		pieces = this.gameManager.getAllPieces(PieceData.WHITE_BYTE);

		for (Piece piece: pieces)
		{
			board.setPiece(piece);
		}

		pieces = this.gameManager.getAllPieces(PieceData.BLACK_BYTE);
		for (Piece piece: pieces)
		{
			board.setPiece(piece);
		}

		this.testAI();
	}


	/**
	 * Called when a button is pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		int i;
		int[] indexArr;
		ArrayList<Move> moves;
		Piece piece;

		if (e.getSource() == sidePanel.getPause())													//PAUSE
		{
			GameManager.chronometer.pause();
			System.out.println("Pause");
		}

		else if (e.getSource() == sidePanel.getUndo())												//UNDO
		{
			Move move = gameManager.getLastMove();
			move = new Move(move.getDst(), move.getSrc(), 0);
			board.makeMove(move, PieceData.getOpponentColor(gameManager.getActiveColorByte()));

			gameManager.undo();

		}
		else if (gameManager.getCachedMoves().isEmpty())											//SELECT SOURCE
		{
			for (i = 0; i < UIData.NUMBER_TILES * UIData.NUMBER_TILES; i++)
			{
				if (e.getSource() == board.getTile(i))
				{
					indexArr = board.get2DCoord(i);
					piece = gameManager.get(indexArr[0], indexArr[1]);
					moves = gameManager.getLegalMoves(piece);

					if (!moves.isEmpty())
					{
						board.setActive(i);
					}


					gameManager.setCachedMoves(moves);
					for (Move move : moves)
					{
						board.highlightMove(move);
					}
				}
			}
		}
		else																						//SELECT DESTINATION
		{
			moves = gameManager.getCachedMoves();

			board.setNormalTileColor(moves.get(0).get2DSrc());

			for (i = 0; i < UIData.NUMBER_TILES * UIData.NUMBER_TILES; i++)
			{
				if (e.getSource() == board.getTile(i))
				{
					for (Move move : moves)
					{
						board.setNormalTileColor(move.get2DDst());
						if (i == board.getIndex(move.get2DDst()))
						{
							System.out.println("Hash before: " + Long.toBinaryString(gameManager.getHash()));
							gameManager.makeMove(move);
							System.out.println("Hash after: " + Long.toBinaryString(gameManager.getHash()));
							board.makeMove(move, gameManager.getActiveColorByte());

							if (move.isPromotion())
							{
								board.getTile(i).setIcon(this.handlePromotion(board.get2DCoord(i)));
							}

							board.getTile(i).setIcon(handlePromotion(board.get2DCoord(i)));
						}
					}
				}
			}
			gameManager.resetCachedMoves();
		}
	}

	public void testAI ()
	{

	}

	public Icon handlePromotion(int[] position)
	{
		final int QUEEN = 0;
		final int ROOK = 1;
		final int BISHOP = 2;
		final int KNIGHT = 3;
		Object[] options = {"Queen","Rook","Bishop","Knight"};

		int choice = JOptionPane.showOptionDialog(this, "Which piece do you want your pawn to become?", "Promotion", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,UIData.BK,options,null);

		if (PieceData.getOpponentColor(gameManager.getActiveColorByte()) == PieceData.WHITE_BYTE)
		{
			switch (choice)
			{
				case QUEEN :
					gameManager.handlePromotion(position, PieceData.QUEEN_BYTE|PieceData.WHITE_BYTE);
					return UIData.WQ;
				case ROOK :
					gameManager.handlePromotion(position, PieceData.ROOK_BYTE|PieceData.WHITE_BYTE);
					return UIData.WR;
				case BISHOP :
					gameManager.handlePromotion(position, PieceData.BISHOP_BYTE|PieceData.WHITE_BYTE);
					return UIData.WB;
				case KNIGHT :
					gameManager.handlePromotion(position, PieceData.KNIGHT_BYTE|PieceData.WHITE_BYTE);
					return UIData.WN;
				default:
					return UIData.WP;
			}


		}
		else
		{
			switch (choice)
			{
				case QUEEN :
					gameManager.handlePromotion(position, PieceData.QUEEN_BYTE|PieceData.BLACK_BYTE);
					return UIData.BQ;
				case ROOK :
					gameManager.handlePromotion(position, PieceData.ROOK_BYTE|PieceData.BLACK_BYTE);
					return UIData.BR;
				case BISHOP :
					gameManager.handlePromotion(position, PieceData.BISHOP_BYTE|PieceData.BLACK_BYTE);
					return UIData.BB;
				case KNIGHT :
					gameManager.handlePromotion(position, PieceData.KNIGHT_BYTE|PieceData.BLACK_BYTE);
					return UIData.BN;
				default:
					return UIData.BP;
			}
		}
	}
}
