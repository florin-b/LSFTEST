package helpers;

import lite.sfa.test.R;
import lite.sfa.test.TestVoice;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ListView;

public class HelperCreareComandaVoice {

	private static TestVoice activity;
	private static String lastCommand;

	public static void runCommand(String command, TestVoice act) {

		activity = act;

		if (reservedCommands().contains(command)) {
			animateComponent(getActionComponentByCommand(command));
			lastCommand = command;
		} else
			writeText(lastCommand, command);

	}

	private static void writeText(String lastCommand, String newCommand) {
		if (lastCommand.equalsIgnoreCase("nume")) {
			((EditText) activity.findViewById(getActionComponentByCommand(lastCommand))).setText(newCommand);
		}
		if (lastCommand.equalsIgnoreCase("selecteaza")) {

			int number = -1;
			if (!isNumber(newCommand))
				number = getNumberFromString(newCommand);
			else
				number = Integer.parseInt(newCommand);

			if (number == -1)
				return;

			number--;

			// de verificat pozitia in lista dupa derulare
			ListView lv = (ListView) activity.findViewById(R.id.listResClienti);

			if (lv.getAdapter().getCount() == 0)
				return;

			lv.performItemClick(null, number, lv.getAdapter().getItemId(number));
		}

	}

	private static void animateComponent(int component) {
		activity.findViewById(component).startAnimation(getAnimation());
	}

	private static Animation getAnimation() {
		Animation animation = new AlphaAnimation(1, 0);
		animation.setDuration(200);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(2);
		animation.setRepeatMode(Animation.REVERSE);

		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				performClick(lastCommand);
			}
		});

		return animation;
	}

	private static String reservedCommands() {
		return "nume, cauta, selecteaza, salveaza, sterge";
	}

	private static int getActionComponentByCommand(String command) {
		if (command.equalsIgnoreCase("nume"))
			return R.id.txtNumeClient;

		if (command.equalsIgnoreCase("cauta"))
			return R.id.cautaClientButton;

		if (command.equals("selecteaza"))
			return R.id.labelSelClient;

		if (command.equals("salveaza"))
			return R.id.salveazaClientButton;

		if (command.equals("sterge"))
			return R.id.stergeClientButton;

		return 0;
	}

	private static void performClick(String command) {
		if (command.equalsIgnoreCase("cauta")) {
			activity.findViewById(R.id.cautaClientButton).performClick();
		}

		if (command.equalsIgnoreCase("salveaza")) {
			activity.findViewById(R.id.salveazaClientButton).performClick();
		}

		if (command.equalsIgnoreCase("sterge")) {
			activity.findViewById(R.id.stergeClientButton).performClick();
		}
	}

	private static boolean isNumber(String strNumber) {
		boolean isNumber = true;

		try {
			Integer.parseInt(strNumber);
		} catch (Exception ex) {
			isNumber = false;
		}

		return isNumber;
	}

	private static int getNumberFromString(String strNumber) {
		int number = -1;

		if (strNumber.equalsIgnoreCase("unu"))
			number = 1;
		if (strNumber.equalsIgnoreCase("doi"))
			number = 2;
		if (strNumber.equalsIgnoreCase("trei"))
			number = 3;
		if (strNumber.equalsIgnoreCase("patru"))
			number = 4;
		if (strNumber.equalsIgnoreCase("cinci"))
			number = 5;
		if (strNumber.equalsIgnoreCase("sase"))
			number = 6;
		if (strNumber.equalsIgnoreCase("sapte"))
			number = 7;
		if (strNumber.equalsIgnoreCase("opt"))
			number = 8;
		if (strNumber.equalsIgnoreCase("noua"))
			number = 9;

		return number;
	}

}
