package com.test.demo.ui.components;

import com.test.demo.ui.util.FontSize;
import com.test.demo.ui.util.FontWeight;
import com.test.demo.ui.util.LumoStyles;
import com.test.demo.ui.util.UIUtils;
import com.test.demo.ui.util.css.BorderRadius;

public class Initials extends FlexBoxLayout {

	private String CLASS_NAME = "initials";

	public Initials(String initials) {
		setAlignItems(Alignment.CENTER);
		setBackgroundColor(LumoStyles.Color.Contrast._10);
		setBorderRadius(BorderRadius.L);
		setClassName(CLASS_NAME);
		UIUtils.setFontSize(FontSize.S, this);
		UIUtils.setFontWeight(FontWeight._600, this);
		setHeight(LumoStyles.Size.M);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setWidth(LumoStyles.Size.M);

		add(initials);
	}

}
