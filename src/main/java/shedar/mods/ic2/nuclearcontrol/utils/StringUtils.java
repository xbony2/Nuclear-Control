package shedar.mods.ic2.nuclearcontrol.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;

import shedar.mods.ic2.nuclearcontrol.api.CardState;
import shedar.mods.ic2.nuclearcontrol.api.PanelString;

public class StringUtils {
	private static DecimalFormat formatter = null;

	private static DecimalFormat getFormatter() {
		if (formatter == null) {
			DecimalFormat lFormatter = new DecimalFormat("#,###.###");
			DecimalFormatSymbols smb = new DecimalFormatSymbols();
			smb.setGroupingSeparator(' ');
			lFormatter.setDecimalFormatSymbols(smb);
			formatter = lFormatter;
		}
		return formatter;
	}

	public static String getFormatted(String resourceName, String value,
			boolean showLabels) {
		if (showLabels)
			return String.format(LanguageHelper.translate(resourceName), value);
		else
			return value;
	}

	public static String getFormatted(String resourceName, double value,
			boolean showLabels) {
		return getFormatted(resourceName, getFormatter().format(value),
				showLabels);
	}

	public static String getFormattedKey(String resourceName,
			Object... arguments) {
		return String.format(LanguageHelper.translate(resourceName), arguments);
	}

	public static List<PanelString> getStateMessage(CardState state) {
		List<PanelString> result = new LinkedList<PanelString>();
		PanelString line = new PanelString();
		switch (state) {
		case OUT_OF_RANGE:
			line.textCenter = LanguageHelper
					.translate("msg.nc.InfoPanelOutOfRange");
			break;
		case INVALID_CARD:
			line.textCenter = LanguageHelper
					.translate("msg.nc.InfoPanelInvalidCard");
			break;
		case NO_TARGET:
			line.textCenter = LanguageHelper
					.translate("msg.nc.InfoPanelNoTarget");
			break;
		case CUSTOM_ERROR:
			break;
		case OK:
			break;
		default:
			break;
		}
		result.add(line);
		return result;
	}

}
