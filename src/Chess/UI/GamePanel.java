package Chess.UI;

import Chess.Game.*;
import Chess.Utils.Parser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class GamePanel extends JFrame implements ActionListener, MouseListener
{
	private GameManager gameManager;

	private JPanel panel;
	private Board board;
	private SidePanel sidePanel;

	public GamePanel(GameManager gameManager)
	{
		 super("Chezz!");
		 this.gameManager = gameManager;
		 //this.gameManager.init();
		 panel = new JPanel();
		 board = new Board(this);
		 sidePanel = new SidePanel(this);

		 panel.setBackground(UIData.BACKGROUND_COLOR);

		 this.setSize(UIData.GAMEPANEL_DIMENSION);
		 this.setResizable(false);
		 this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		panel.add(board);
		panel.add(sidePanel);

		this.setContentPane(panel);
		//this.add(panel);
		this.initBoard();

		this.setResizable(false);
		//this.setVisible(true);

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

		if(gameManager.getActiveColorByte() == PieceData.WHITE_BYTE)
		{
			board.setBoardWhite();
		}
		else
		{
			board.setBoardBlack();
		}

		//this.testAI();
	}


	/**
	 * Called when a button is pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{


		if (e.getSource() == sidePanel.getPause())                                                    //PAUSE
		{
			GameManager.chronometer.pause();
			System.out.println("Pause");
		}

		else if (e.getSource() == sidePanel.getUndo())                                                //UNDO
		{
			if(gameManager.hasLastMove())
			{

				HistoryMove historyMove = gameManager.getLastMove();
				historyMove = new HistoryMove(new Move(historyMove.getDst(), historyMove.getSrc(), 0),historyMove.getCapturedPiece());
				board.makeMove(historyMove, PieceData.getOpponentColor(gameManager.getActiveColorByte()));

				gameManager.undo();

				if (historyMove.isCapture())
				{
					board.setPiece(historyMove.getCapturedPiece());
				}

			}

		}
		else if (e.getSource() == sidePanel.getSave())                                                //SAVE
		{
			System.out.println("Save");
			Parser.saveToFile(gameManager.getMoveHistory(), this);
		}
		else if (e.getSource() == sidePanel.getMoveInput())
		{
			String input = sidePanel.getMoveInput().getText();
			Move move = Parser.stringToMove(input, gameManager);
			if (move != null)
			{
				gameManager.makeMove(move);
				board.makeMove(move, gameManager.getActiveColorByte());
				System.out.println("Move input: " + input);
				sidePanel.setMoveInput("");
			}
			else
			{
				sidePanel.setMoveInput("Invalid Move");
			}
		}
		//else if (e.getSource() == sidePanel.getLoad())												//LOAD
		//{
			//Parser.readFromFile(gameManager, this);

		//}
		else if (gameManager.getCachedMoves().isEmpty())                                            //SELECT SOURCE
		{
			int[] indexArr;
			ArrayList<Move> moves;
			Piece piece;
			for (int i = 0; i < UIData.NUMBER_TILES * UIData.NUMBER_TILES; i++)
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
		else                                                                                        //SELECT DESTINATION
		{
			ArrayList<Move> moves;

			moves = gameManager.getCachedMoves();

			board.setNormalTileColor(moves.get(0).get2DSrc());

			for (int i = 0; i < UIData.TOTAL_TILES; i++)
			{
				if (e.getSource() == board.getTile(i))
				{

					for (Move move : moves)
					{
						board.setNormalTileColor(move.get2DDst());
						if (i == board.getIndex(move.get2DDst()))
						{
							//System.out.println("Hash before: " + Long.toBinaryString(gameManager.getZobristHash()));

							//System.out.println(Parser.moveToString(move));
							//System.out.println("Hash after: " + Long.toBinaryString(gameManager.getZobristHash()));
							this.makeMove(move);
							if (gameManager.isCheckMate(PieceData.getOpponentColor(gameManager.getActiveColorByte())))
							{
								this.handleCheckMate();
							}
							if (move.isPromotion())
							{
								board.getTile(i).setIcon(this.handlePromotion(board.get2DCoord(i)));
							}
						}

					}
				}
			}
			gameManager.resetCachedMoves();
		}
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

	public void handleCheckMate()
	{
		Object[] options = { "Exit","Save & Exit","Save & Start New Game","Start New Game"};
		String winner;
		if(gameManager.getActiveColorByte() == PieceData.WHITE_BYTE)
		{
			winner = "Black";
		}
		else
		{
			winner = "White";
		}

		int choice = JOptionPane.showOptionDialog(this,"Checkmate! " + winner + " wins!", "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,  UIData.BK, options, null);
	}

	public void makeMove(Move move)
	{
		gameManager.makeMove(move);
		board.makeMove(move, gameManager.getActiveColorByte());

		String history = "";

		for(Move move_history : gameManager.getMoveHistory())
		{
			history += Parser.moveToString(move_history) + "\n";
		}

		sidePanel.setHistory(history);
	}

	public JButton getExit()
	{
		return sidePanel.getExit();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getSource()==sidePanel.getMoveInput())
		{
			sidePanel.setMoveInput("");
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}
}
