package Chess.UI;

import Chess.Game.GameManager;
import Chess.Time.Chronometer;

import javax.swing.*;


public class TimePanel extends JPanel
{
	private Chronometer chronometer;

	private JLabel labelWhite;
	private JLabel labelBlack;
	private JLabel text;


	private JLabel timeWhite;
	private JLabel timeBlack;

	public TimePanel()
	{
		this.chronometer = GameManager.chronometer;

		this.labelWhite = new JLabel("");
		this.labelBlack = new JLabel("");
		this.text = new JLabel("VS");

		this.labelWhite.setIcon(UIData.WK);
		this.labelBlack.setIcon(UIData.BK);
		this.text.setBackground(UIData.BACKGROUND_COLOR);

		this.timeWhite = this.chronometer.getDisplayWhite();
		this.timeBlack = this.chronometer.getDisplayBlack();

		this.add(timeWhite);
		this.add(labelWhite);

		this.add(text);

		this.add(labelBlack);
		this.add(timeBlack);


		//this.setBorder(UIData.BORDER_BLACK);

	}
}
