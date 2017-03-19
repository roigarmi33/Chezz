package Chess.Game;

/**
 * @author Thomas
 * @since 22/02/2017
 * <p>
 * Project: ChessGame
 * Package: Chess.Game
 *
 * All moves are generated in a file-first fashion
 */
public final class Movesets
{
	/**
	 * Move-offsets for a Pawn
	 */
	public static final int[] PAWN_MOVE_WHITE =		{0x10,	0x20};
	public static final int[] PAWN_MOVE_BLACK =		{-0x10,	-0x20};
	public static final int[] PAWN_CAPTURE_WHITE =	{0x0F,	0x11};
	public static final int[] PAWN_CAPTURE_BLACK =	{-0x0F,	-0x11};

	/*
	 * Rook
	 */

	public static final int[] ROOK_MOVE =	{	0x10,	0x20,	0x30,	0x40,	0x50,	0x60,	0x70,	 0x80,		// Moving away from the white player
												-0x10,	-0x20,	-0x30,	-0x40,	-0x50,	-0x60,	-0x70,	-0x80,		 // Moving towards the white player
												0x01,	0x02,	0x03,	0x04,	0x05,	0x06,	0x07,	0x08,		// Moving to the white player's right
												-0x01,	-0x02,	-0x03,	-0x04,	-0x05,	-0x06,	-0x07,	-0x08};		// Moving to the white player's left

	/*
	 * Knight
	 * Knight moves are first listed in an "up" (away from the white player) fashion (first 4 moves)
	 * 								Followed by 4 moves in a down fashion (away from the black player)
	*/

	public static final int[] KNIGHT_MOVE =	{	0x21,	0x1F,		// |_ _|	Up
												0x0E,	0x12,		// |__ __|	Up
												-0x21,	-0x1F,		// |_ _|	Down
												-0x0E,	-0x12};		// |__ __|	Down
	/*
	 * Bishop
	*/
	public static final int[] BISHOP_MOVE =	{	0x11,	0x22,	0x33,	0x44,	0x55,	0x66,	0x77,	0x88,	// Bottom Left to top right
												0x0F,	0x1E,	0x2D,	0x3C,	0x4B,	0x5A,	0x69,	0x78,	// Bottom right to top left
												-0x11,	-0x22,	-0x33,	-0x44,	-0x55,	-0x66,	-0x77,	-0x88,	// Top right to bottom left
												-0x0F,	-0x1E,	-0x2D,	-0x3C,	-0x4B,	-0x5A,	-0x69,	-0x78};	// Top left to bottom right

	/*
	 * Queen = Rook + Bishop
	*/
	public static final int[] QUEEN_MOVE = 	{	0x10,	0x20,	0x30,	0x40,	0x50,	0x60,	0x70,	 0x80,
												-0x10,	-0x20,	-0x30,	-0x40,	-0x50,	-0x60,	-0x70,	-0x80,
												0x01,	0x02,	0x03,	0x04,	0x05,	0x06,	0x07,	0x08,
												-0x01,	-0x02,	-0x03,	-0x04,	-0x05,	-0x06,	-0x07,	-0x08,
												0x11,	0x22,	0x33,	0x44,	0x55,	0x66,	0x77,	 0x88,	// Bottom Left to top right
												0x0F,	0x1E,	0x2D,	0x3C,	0x4B,	0x5A,	0x69,	 0x78,	// Bottom right to top left
												-0x11,	-0x22,	-0x33,	-0x44,	-0x55,	-0x66,	-0x77,	 -0x88,	// Top right to bottom left
												-0x0F,	-0x1E,	-0x2D,	-0x3C,	-0x4B,	-0x5A,	-0x69,	 -0x78};	// Top left to bottom right

	/*
	 King
	*/
	public static final int[] KING_MOVE =	{	0x10,	-0x10,	0x01,	-0x01,
												0x11,	0x0F,	-0x11,	-0x0F};
}
